package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Flashcard
import com.example.data.model.FlashcardCategory
import com.example.ui.components.AnimalCardVisual
import com.example.ui.components.CategorySelectorBar
import com.example.ui.components.ColorCardVisual
import com.example.ui.components.CustomCardVisual
import com.example.ui.components.EmptyCustomPhotosPrompt
import com.example.ui.components.LetterCardVisual
import com.example.ui.components.NumberCardVisual
import com.example.ui.components.ParentalGateButton
import com.example.ui.components.SparkleEffect
import com.example.ui.components.VehicleCardVisual
import kotlinx.coroutines.launch

@Composable
fun FlashcardScreen(
    currentCategory: FlashcardCategory,
    currentCard: Flashcard?,
    showLabels: Boolean,
    hapticEnabled: Boolean,
    onCategorySelected: (FlashcardCategory) -> Unit,
    onCardTapped: () -> Unit,
    onPreviousCard: () -> Unit,
    onNextCard: () -> Unit,
    onOpenParentSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    var tapPosition by remember { mutableStateOf<Offset?>(null) }
    var lastTapTime by remember { mutableLongStateOf(0L) }
    val cardScale = remember { Animatable(1f) }

    fun triggerTapInteraction(offset: Offset) {
        val now = System.currentTimeMillis()
        // Multi-touch & debounce guard: ignore taps closer than 220ms
        if (now - lastTapTime < 220) return
        lastTapTime = now

        tapPosition = offset
        if (hapticEnabled) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }

        // Bouncy spring scale animation
        coroutineScope.launch {
            cardScale.snapTo(0.95f)
            cardScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f)
            )
        }

        onCardTapped()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Sleek Top Bar (Category selector + Parent lock)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategorySelectorBar(
                    selectedCategory = currentCategory,
                    onCategorySelected = onCategorySelected,
                    modifier = Modifier.weight(1f)
                )

                ParentalGateButton(
                    onUnlocked = onOpenParentSettings,
                    modifier = Modifier.padding(end = 6.dp)
                )
            }

            // Maximum Screen Interactive Flashcard Display
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("flashcard_container")
                    .pointerInput(currentCard?.id) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                // Baby-proof multi-touch suppression:
                                // If more than 1 pointer is on screen, ignore to prevent palm resting bugs
                                if (event.type == PointerEventType.Press && event.changes.size == 1) {
                                    val change = event.changes.first()
                                    if (!change.isConsumed) {
                                        change.consume()
                                        triggerTapInteraction(change.position)
                                    }
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (currentCard != null) {
                    val cardBgColor = Color(currentCard.backgroundColorHex)

                    AnimatedContent(
                        targetState = currentCard,
                        transitionSpec = {
                            (fadeIn(animationSpec = spring(stiffness = 600f)) +
                                    scaleIn(initialScale = 0.92f, animationSpec = spring(stiffness = 600f)))
                                .togetherWith(
                                    fadeOut(animationSpec = spring(stiffness = 800f)) +
                                            scaleOut(targetScale = 1.05f, animationSpec = spring(stiffness = 800f))
                                )
                        },
                        label = "card_transition"
                    ) { targetCard ->
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(cardScale.value)
                                .testTag("active_flashcard_${targetCard.id}"),
                            shape = RoundedCornerShape(36.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBgColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                when (targetCard) {
                                    is Flashcard.ColorCard -> ColorCardVisual(
                                        card = targetCard,
                                        showLabels = showLabels
                                    )
                                    is Flashcard.NumberCard -> NumberCardVisual(
                                        card = targetCard,
                                        showLabels = showLabels
                                    )
                                    is Flashcard.LetterCard -> LetterCardVisual(
                                        card = targetCard,
                                        showLabels = showLabels
                                    )
                                    is Flashcard.AnimalCard -> AnimalCardVisual(
                                        card = targetCard,
                                        showLabels = showLabels
                                    )
                                    is Flashcard.VehicleCard -> VehicleCardVisual(
                                        card = targetCard,
                                        showLabels = showLabels
                                    )
                                    is Flashcard.CustomCard -> CustomCardVisual(
                                        card = targetCard,
                                        showLabels = showLabels,
                                        onOpenParentSettings = onOpenParentSettings
                                    )
                                }
                            }
                        }
                    }
                } else {
                    EmptyCustomPhotosPrompt(onOpenParentSettings = onOpenParentSettings)
                }

                // Joyful Particle Sparkles on Tap
                SparkleEffect(tapOffset = tapPosition)
            }

            // Compact Floating Bottom Controls (Takes minimal vertical space)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onPreviousCard,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    shadowElevation = 3.dp,
                    modifier = Modifier.size(48.dp).testTag("prev_card_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Card",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    shadowElevation = 1.dp
                ) {
                    Text(
                        text = "👆 Tap anywhere to explore!",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    onClick = onNextCard,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    shadowElevation = 3.dp,
                    modifier = Modifier.size(48.dp).testTag("next_card_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Card",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}
