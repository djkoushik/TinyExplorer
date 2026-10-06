package com.example

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.FlashcardScreen
import com.example.ui.screens.ParentalSettingsDialog
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Keep screen awake during toddler exploration
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                // Immersive full-screen handling with sticky transient bars
                LaunchedEffect(uiState.immersiveMode) {
                    val controller = WindowCompat.getInsetsController(window, window.decorView)
                    if (uiState.immersiveMode) {
                        controller.hide(WindowInsetsCompat.Type.systemBars())
                        controller.systemBarsBehavior =
                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    } else {
                        controller.show(WindowInsetsCompat.Type.systemBars())
                    }
                }

                // Guard accidental back gestures during toddler play
                BackHandler(enabled = true) {
                    if (uiState.isParentSettingsOpen) {
                        viewModel.closeParentSettings()
                    }
                    // Prevent toddler from accidentally closing app
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    FlashcardScreen(
                        currentCategory = uiState.currentCategory,
                        currentCard = uiState.currentCard,
                        showLabels = uiState.showLabels,
                        hapticEnabled = uiState.hapticEnabled,
                        onCategorySelected = viewModel::selectCategory,
                        onCardTapped = viewModel::onScreenTap,
                        onPreviousCard = viewModel::previousCard,
                        onNextCard = viewModel::nextCard,
                        onOpenParentSettings = viewModel::openParentSettings
                    )

                    if (uiState.isParentSettingsOpen) {
                        ParentalSettingsDialog(
                            customPhotos = uiState.customPhotos,
                            onAddPhoto = viewModel::addCustomPhoto,
                            onDeletePhoto = viewModel::deleteCustomPhoto,
                            hapticEnabled = uiState.hapticEnabled,
                            onToggleHaptic = viewModel::toggleHaptic,
                            showLabels = uiState.showLabels,
                            onToggleLabels = viewModel::toggleShowLabels,
                            immersiveMode = uiState.immersiveMode,
                            onToggleImmersive = viewModel::toggleImmersiveMode,
                            onDismiss = viewModel::closeParentSettings
                        )
                    }
                }
            }
        }
    }
}
