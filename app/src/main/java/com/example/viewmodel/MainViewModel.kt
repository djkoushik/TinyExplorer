package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DataStoreManager
import com.example.data.FlashcardRepository
import com.example.data.model.Flashcard
import com.example.data.model.FlashcardCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class MainUiState(
    val currentCategory: FlashcardCategory = FlashcardCategory.ALL,
    val currentCard: Flashcard? = null,
    val customPhotos: List<Flashcard.CustomCard> = emptyList(),
    val hapticEnabled: Boolean = true,
    val showLabels: Boolean = true,
    val immersiveMode: Boolean = true,
    val isParentSettingsOpen: Boolean = false,
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FlashcardRepository(application)
    private val dataStoreManager = DataStoreManager(application)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val history = mutableListOf<Flashcard>()
    private var historyIndex = -1
    private val recentCardIds = ArrayDeque<String>()

    init {
        loadInitialState()
    }

    private fun loadInitialState() {
        viewModelScope.launch {
            val customCards = repository.getCustomCards()
            val savedCategory = dataStoreManager.selectedCategoryFlow.first()
            val haptic = dataStoreManager.hapticEnabledFlow.first()
            val labels = dataStoreManager.showLabelsFlow.first()
            val immersive = dataStoreManager.immersiveModeFlow.first()

            _uiState.value = _uiState.value.copy(
                currentCategory = savedCategory,
                customPhotos = customCards,
                hapticEnabled = haptic,
                showLabels = labels,
                immersiveMode = immersive
            )

            pickRandomCard(savedCategory, avoidSameId = false)
        }
    }

    fun selectCategory(category: FlashcardCategory) {
        viewModelScope.launch {
            dataStoreManager.setSelectedCategory(category)
            _uiState.value = _uiState.value.copy(currentCategory = category)
            recentCardIds.clear()
            pickRandomCard(category, avoidSameId = false)
        }
    }

    fun onScreenTap() {
        pickRandomCard(_uiState.value.currentCategory, avoidSameId = true)
    }

    fun nextCard() {
        if (historyIndex < history.size - 1) {
            historyIndex++
            _uiState.value = _uiState.value.copy(currentCard = history[historyIndex])
        } else {
            pickRandomCard(_uiState.value.currentCategory, avoidSameId = true)
        }
    }

    fun previousCard() {
        if (historyIndex > 0) {
            historyIndex--
            _uiState.value = _uiState.value.copy(currentCard = history[historyIndex])
        }
    }

    private fun pickRandomCard(category: FlashcardCategory, avoidSameId: Boolean) {
        val cardToSelect: Flashcard = if (category == FlashcardCategory.ALL) {
            // Fair balanced category selection: evenly distributes Animals, Vehicles, Colors, Numbers, Letters
            val subCategories = mutableListOf(
                FlashcardCategory.ANIMALS,
                FlashcardCategory.VEHICLES,
                FlashcardCategory.COLORS,
                FlashcardCategory.NUMBERS,
                FlashcardCategory.LETTERS,
            )
            if (repository.getCustomCards().isNotEmpty()) {
                subCategories.add(FlashcardCategory.CUSTOM)
            }

            val currentCat = _uiState.value.currentCard?.category
            val candidateCats = if (currentCat != null && subCategories.size > 1) {
                subCategories.filter { it != currentCat }
            } else {
                subCategories
            }

            val targetCat = candidateCats.random()
            val cards = repository.getCardsForCategory(targetCat)
            val freshCards = cards.filter { it.id !in recentCardIds }
            if (freshCards.isNotEmpty()) freshCards.random() else cards.random()
        } else {
            val availableCards = repository.getCardsForCategory(category)
            if (availableCards.isEmpty()) {
                _uiState.value = _uiState.value.copy(currentCard = null)
                return
            }
            val freshCards = availableCards.filter { it.id !in recentCardIds }
            if (freshCards.isNotEmpty()) {
                freshCards.random()
            } else {
                val currentId = _uiState.value.currentCard?.id
                val filtered = if (avoidSameId && availableCards.size > 1) {
                    availableCards.filter { it.id != currentId }
                } else {
                    availableCards
                }
                filtered.random()
            }
        }

        recentCardIds.addLast(cardToSelect.id)
        if (recentCardIds.size > 15) {
            recentCardIds.removeFirst()
        }

        history.add(cardToSelect)
        if (history.size > 50) {
            history.removeAt(0)
        }
        historyIndex = history.size - 1

        _uiState.value = _uiState.value.copy(currentCard = cardToSelect)
    }

    fun addCustomPhoto(uri: Uri) {
        viewModelScope.launch {
            val newCard = repository.saveCustomImage(uri)
            if (newCard != null) {
                val updatedPhotos = repository.getCustomCards()
                _uiState.value = _uiState.value.copy(
                    customPhotos = updatedPhotos,
                    currentCard = newCard
                )
            }
        }
    }

    fun deleteCustomPhoto(filePath: String) {
        viewModelScope.launch {
            repository.deleteCustomImage(filePath)
            val updatedPhotos = repository.getCustomCards()
            _uiState.value = _uiState.value.copy(customPhotos = updatedPhotos)
            if (_uiState.value.currentCard?.id?.contains(filePath.substringAfterLast("/")) == true) {
                pickRandomCard(_uiState.value.currentCategory, avoidSameId = false)
            }
        }
    }

    fun toggleHaptic(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setHapticEnabled(enabled)
            _uiState.value = _uiState.value.copy(hapticEnabled = enabled)
        }
    }

    fun toggleShowLabels(show: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setShowLabels(show)
            _uiState.value = _uiState.value.copy(showLabels = show)
        }
    }

    fun toggleImmersiveMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setImmersiveMode(enabled)
            _uiState.value = _uiState.value.copy(immersiveMode = enabled)
        }
    }

    fun openParentSettings() {
        _uiState.value = _uiState.value.copy(isParentSettingsOpen = true)
    }

    fun closeParentSettings() {
        _uiState.value = _uiState.value.copy(isParentSettingsOpen = false)
    }
}
