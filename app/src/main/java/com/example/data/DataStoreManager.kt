package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.FlashcardCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tiny_explorer_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val KEY_SELECTED_CATEGORY = stringPreferencesKey("selected_category")
        val KEY_HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val KEY_SHOW_LABELS = booleanPreferencesKey("show_labels")
        val KEY_IMMERSIVE_MODE = booleanPreferencesKey("immersive_mode")
    }

    val selectedCategoryFlow: Flow<FlashcardCategory> = context.dataStore.data.map { prefs ->
        val categoryId = prefs[KEY_SELECTED_CATEGORY] ?: FlashcardCategory.ALL.id
        FlashcardCategory.fromId(categoryId)
    }

    val hapticEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_HAPTIC_ENABLED] ?: true
    }

    val showLabelsFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_SHOW_LABELS] ?: true
    }

    val immersiveModeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_IMMERSIVE_MODE] ?: true
    }

    suspend fun setSelectedCategory(category: FlashcardCategory) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SELECTED_CATEGORY] = category.id
        }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HAPTIC_ENABLED] = enabled
        }
    }

    suspend fun setShowLabels(show: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SHOW_LABELS] = show
        }
    }

    suspend fun setImmersiveMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IMMERSIVE_MODE] = enabled
        }
    }
}
