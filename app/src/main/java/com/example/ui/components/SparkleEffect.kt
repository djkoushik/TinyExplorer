package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class SparkleParticle(
    val initialOffset: Offset,
    val angle: Double,
    val speed: Float,
    val color: Color,
    val size: Float,
)

@Composable
fun SparkleEffect(
    tapOffset: Offset?,
    modifier: Modifier = Modifier,
) {
    if (tapOffset == null) return

    val progress = remember(tapOffset) { Animatable(0f) }

    val particles = remember(tapOffset) {
        val colors = listOf(
            Color(0xFFFFD700),
            Color(0xFFFF69B4),
            Color(0xFF00E5FF),
            Color(0xFFFF5252),
            Color(0xFF76FF03),
            Color(0xFFFFAB40),
            Color(0xFFE040FB)
        )
        List(14) {
            val angle = Random.nextDouble(0.0, Math.PI * 2)
            val speed = Random.nextFloat() * 160f + 60f
            val color = colors[Random.nextInt(colors.size)]
            val size = Random.nextFloat() * 12f + 8f
            SparkleParticle(tapOffset, angle, speed, color, size)
        }
    }

    LaunchedEffect(tapOffset) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 450)
        )
    }

    if (progress.value < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val p = progress.value
            val alpha = (1f - p).coerceIn(0f, 1f)
            particles.forEach { particle ->
                val distance = particle.speed * p
                val x = particle.initialOffset.x + (cos(particle.angle) * distance).toFloat()
                val y = particle.initialOffset.y + (sin(particle.angle) * distance).toFloat()
                val currentSize = particle.size * (1f - p * 0.5f)

                drawSparkleStar(
                    center = Offset(x, y),
                    radius = currentSize,
                    color = particle.color.copy(alpha = alpha)
                )
            }
        }
    }
}

private fun DrawScope.drawSparkleStar(
    center: Offset,
    radius: Float,
    color: Color,
) {
    // 4-pointed sparkle star
    drawCircle(
        color = color,
        radius = radius * 0.6f,
        center = center
    )
    drawLine(
        color = color,
        start = Offset(center.x - radius, center.y),
        end = Offset(center.x + radius, center.y),
        strokeWidth = radius * 0.4f
    )
    drawLine(
        color = color,
        start = Offset(center.x, center.y - radius),
        end = Offset(center.x, center.y + radius),
        strokeWidth = radius * 0.4f
    )
}
