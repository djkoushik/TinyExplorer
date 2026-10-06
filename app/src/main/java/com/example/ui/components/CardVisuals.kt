package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.model.Flashcard
import java.io.File

@Composable
fun ColorCardVisual(
    card: Flashcard.ColorCard,
    showLabels: Boolean,
    modifier: Modifier = Modifier,
) {
    val targetColor = Color(card.colorHex)
    val isLightColor = card.title == "WHITE" || card.title == "YELLOW"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(targetColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = size.maxDimension * 0.45f,
                center = center
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.08f),
                radius = size.maxDimension * 0.65f,
                center = center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Surface(
                modifier = Modifier
                    .size(230.dp)
                    .shadow(16.dp, RoundedCornerShape(70.dp)),
                shape = RoundedCornerShape(70.dp),
                color = if (card.title == "WHITE") Color(0xFFF8FAFC) else targetColor,
                border = androidx.compose.foundation.BorderStroke(
                    width = 8.dp,
                    color = if (card.title == "WHITE") Color(0xFF94A3B8) else Color.White.copy(alpha = 0.85f)
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.35f),
                            radius = size.minDimension * 0.22f,
                            center = Offset(size.width * 0.3f, size.height * 0.3f)
                        )
                    }
                    Text(
                        text = when (card.title) {
                            "RED" -> "❤️"
                            "BLUE" -> "🌊"
                            "YELLOW" -> "☀️"
                            "GREEN" -> "🍀"
                            "ORANGE" -> "🍊"
                            "PURPLE" -> "🍇"
                            "PINK" -> "🌸"
                            "BROWN" -> "🌰"
                            "BLACK" -> "🌙"
                            "WHITE" -> "☁️"
                            "TEAL" -> "💎"
                            else -> "🎨"
                        },
                        fontSize = 80.sp
                    )
                }
            }

            if (showLabels) {
                Spacer(modifier = Modifier.height(36.dp))
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    color = if (isLightColor) Color(0xFF1E293B) else Color.White,
                    shadowElevation = 8.dp
                ) {
                    Text(
                        text = card.title,
                        modifier = Modifier.padding(horizontal = 44.dp, vertical = 14.dp),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 48.sp
                        ),
                        color = if (isLightColor) Color.White else targetColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumberCardVisual(
    card: Flashcard.NumberCard,
    showLabels: Boolean,
    modifier: Modifier = Modifier,
) {
    val accentColor = Color(card.accentColorHex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = card.number.toString(),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 130.sp,
                lineHeight = 130.sp
            ),
            color = accentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (card.number > 0) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
                maxItemsInEachRow = 5
            ) {
                repeat(card.number) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(accentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        } else {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Zero (None)",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = accentColor,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }
        }

        if (showLabels) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = accentColor,
                shadowElevation = 6.dp,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = card.wordName.uppercase(),
                    modifier = Modifier.padding(horizontal = 36.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun LetterCardVisual(
    card: Flashcard.LetterCard,
    showLabels: Boolean,
    modifier: Modifier = Modifier,
) {
    val accentColor = Color(card.accentColorHex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Surface(
            modifier = Modifier
                .size(210.dp)
                .shadow(12.dp, CircleShape),
            shape = CircleShape,
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(8.dp, accentColor)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = card.letter,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 120.sp
                    ),
                    color = accentColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = card.emoji,
                    fontSize = 52.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                if (showLabels) {
                    Column {
                        Text(
                            text = card.exampleWord.uppercase(),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 32.sp
                            ),
                            color = accentColor
                        )
                        Text(
                            text = "${card.letter} is for ${card.exampleWord}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimalCardVisual(
    card: Flashcard.AnimalCard,
    showLabels: Boolean,
    modifier: Modifier = Modifier,
) {
    val accentColor = Color(card.accentColorHex)
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Complete Image Display Container (Never cropped, completely visible!)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shadow(8.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(3.dp, accentColor.copy(alpha = 0.25f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(card.imageAssetPath)
                        .crossfade(true)
                        .build(),
                    contentDescription = card.title,
                    // ContentScale.Fit ensures the COMPLETE animal is displayed without being cut off!
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(color = accentColor)
                        }
                    },
                    error = {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(text = card.emoji, fontSize = 110.sp)
                        }
                    }
                )

                // Top corner badge
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.92f),
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = card.emoji, fontSize = 28.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Large Bottom Information Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.95f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showLabels) {
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 38.sp
                        ),
                        color = accentColor
                    )
                }

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = accentColor.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, accentColor)
                ) {
                    Text(
                        text = "“ ${card.sound} ”",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        ),
                        color = accentColor
                    )
                }
            }
        }
    }
}

@Composable
fun VehicleCardVisual(
    card: Flashcard.VehicleCard,
    showLabels: Boolean,
    modifier: Modifier = Modifier,
) {
    val accentColor = Color(card.accentColorHex)
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Complete Image Display Container (Never cropped, completely visible!)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shadow(8.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(3.dp, accentColor.copy(alpha = 0.25f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(card.imageAssetPath)
                        .crossfade(true)
                        .build(),
                    contentDescription = card.title,
                    // ContentScale.Fit ensures the COMPLETE vehicle is displayed without being cut off!
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(color = accentColor)
                        }
                    },
                    error = {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(text = card.emoji, fontSize = 110.sp)
                        }
                    }
                )

                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.92f),
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = card.emoji, fontSize = 28.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Large Bottom Information Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.95f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showLabels) {
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 34.sp
                        ),
                        color = accentColor
                    )
                }

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = accentColor.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, accentColor)
                ) {
                    Text(
                        text = "“ ${card.sound} ”",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        ),
                        color = accentColor
                    )
                }
            }
        }
    }
}

@Composable
fun CustomCardVisual(
    card: Flashcard.CustomCard,
    showLabels: Boolean,
    onOpenParentSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val file = File(card.filePath)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (file.exists()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .shadow(8.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(3.dp, Color(0xFF3B82F6))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = file,
                        contentDescription = card.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                CircularProgressIndicator(color = Color(0xFF3B82F6))
                            }
                        }
                    )
                }
            }

            if (showLabels) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF3B82F6),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "FAMILY & PETS",
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = Color.White
                    )
                }
            }
        } else {
            EmptyCustomPhotosPrompt(onOpenParentSettings = onOpenParentSettings)
        }
    }
}

@Composable
fun EmptyCustomPhotosPrompt(
    onOpenParentSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(0.94f)
            .padding(20.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color.White.copy(alpha = 0.98f),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "📷",
                fontSize = 72.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Photos Yet",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp
                ),
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add real pictures of family members, pets, or favorite toys for your child to discover!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onOpenParentSettings,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                modifier = Modifier.height(54.dp)
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Add Photos Now",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
