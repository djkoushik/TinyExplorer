package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FlashcardCategory

@Composable
fun CategorySelectorBar(
    selectedCategory: FlashcardCategory,
    onCategorySelected: (FlashcardCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FlashcardCategory.entries.forEach { category ->
            val isSelected = category == selectedCategory

            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) {
                    when (category) {
                        FlashcardCategory.ALL -> Color(0xFFFF9800)
                        FlashcardCategory.COLORS -> Color(0xFFE91E63)
                        FlashcardCategory.NUMBERS -> Color(0xFF3F51B5)
                        FlashcardCategory.LETTERS -> Color(0xFF009688)
                        FlashcardCategory.ANIMALS -> Color(0xFF4CAF50)
                        FlashcardCategory.VEHICLES -> Color(0xFFFF5722)
                        FlashcardCategory.CUSTOM -> Color(0xFF9C27B0)
                    }
                } else {
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                },
                label = "category_bg"
            )

            val contentColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                label = "category_content"
            )

            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.05f else 1.0f,
                animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
                label = "category_scale"
            )

            Surface(
                onClick = { onCategorySelected(category) },
                modifier = Modifier
                    .scale(scale)
                    .height(48.dp)
                    .testTag("category_pill_${category.id}"),
                shape = RoundedCornerShape(24.dp),
                color = backgroundColor,
                shadowElevation = if (isSelected) 5.dp else 2.dp,
                tonalElevation = if (isSelected) 3.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = category.iconEmoji,
                        fontSize = 20.sp
                    )
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = contentColor
                    )
                }
            }
        }
    }
}
