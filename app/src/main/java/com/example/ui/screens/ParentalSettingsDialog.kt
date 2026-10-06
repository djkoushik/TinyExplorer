package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.FlashcardRepository
import com.example.data.model.Flashcard
import com.example.data.model.FlashcardCategory
import java.io.File

@Composable
fun ParentalSettingsDialog(
    customPhotos: List<Flashcard.CustomCard>,
    customCardNames: Map<String, String>,
    repository: FlashcardRepository,
    onAddPhoto: (Uri) -> Unit,
    onDeletePhoto: (String) -> Unit,
    onEditCardName: (Flashcard) -> Unit,
    hapticEnabled: Boolean,
    onToggleHaptic: (Boolean) -> Unit,
    showLabels: Boolean,
    onToggleLabels: (Boolean) -> Unit,
    immersiveMode: Boolean,
    onToggleImmersive: (Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onAddPhoto(uri)
            }
        }
    )

    var selectedEditCategory by remember { mutableStateOf(FlashcardCategory.ANIMALS) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f)
                .clip(RoundedCornerShape(32.dp))
                .testTag("parental_settings_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Parent Controls",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Customize names & app settings",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_settings_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Card Names & Labels Editor Section
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Spellcheck, contentDescription = null, tint = Color(0xFFE91E63))
                                Text(
                                    text = "Edit Card Names & Labels",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Text(
                                text = "Tap any card below to change its name and save it permanently:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
                            )

                            // Category chips for editor
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(listOf(
                                    FlashcardCategory.ANIMALS,
                                    FlashcardCategory.VEHICLES,
                                    FlashcardCategory.COLORS,
                                    FlashcardCategory.NUMBERS,
                                    FlashcardCategory.LETTERS,
                                )) { cat ->
                                    val isSel = cat == selectedEditCategory
                                    Surface(
                                        onClick = { selectedEditCategory = cat },
                                        shape = RoundedCornerShape(16.dp),
                                        color = if (isSel) MaterialTheme.colorScheme.primary else Color.White,
                                        shadowElevation = if (isSel) 3.dp else 1.dp
                                    ) {
                                        Text(
                                            text = "${cat.iconEmoji} ${cat.displayName}",
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = if (isSel) Color.White else Color(0xFF1E293B)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Horizontal list of cards to edit
                            val cardsToEdit = repository.getCardsForCategory(selectedEditCategory)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(cardsToEdit, key = { it.id }) { card ->
                                    val customName = customCardNames[card.id]
                                    val displayName = customName ?: when (card) {
                                        is Flashcard.LetterCard -> card.exampleWord
                                        is Flashcard.NumberCard -> card.wordName
                                        else -> card.title
                                    }

                                    Surface(
                                        onClick = { onEditCardName(card) },
                                        shape = RoundedCornerShape(16.dp),
                                        color = Color.White,
                                        border = androidx.compose.foundation.BorderStroke(
                                            width = if (customName != null) 2.dp else 1.dp,
                                            color = if (customName != null) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                                        ),
                                        shadowElevation = 2.dp,
                                        modifier = Modifier.width(130.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            when (card) {
                                                is Flashcard.AnimalCard -> {
                                                    AsyncImage(
                                                        model = card.imageAssetPath,
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.size(54.dp).clip(RoundedCornerShape(10.dp))
                                                    )
                                                }
                                                is Flashcard.VehicleCard -> {
                                                    AsyncImage(
                                                        model = card.imageAssetPath,
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.size(54.dp).clip(RoundedCornerShape(10.dp))
                                                    )
                                                }
                                                is Flashcard.LetterCard -> {
                                                    Text(text = card.emoji, fontSize = 34.sp)
                                                }
                                                is Flashcard.NumberCard -> {
                                                    Text(text = card.title, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(card.accentColorHex))
                                                }
                                                is Flashcard.ColorCard -> {
                                                    Box(
                                                        modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(card.colorHex))
                                                    )
                                                }
                                                else -> {
                                                    Text("📷", fontSize = 28.sp)
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Text(
                                                text = displayName.uppercase(),
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                maxLines = 1,
                                                color = if (customName != null) MaterialTheme.colorScheme.primary else Color(0xFF1E293B)
                                            )

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                modifier = Modifier.padding(top = 2.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Text(
                                                    text = if (customName != null) "Edited" else "Rename",
                                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Custom Photos section
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Custom Photos (${customPhotos.size})",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Add photos of family, pets & toys",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Button(
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                                    modifier = Modifier.testTag("add_photo_button")
                                ) {
                                    Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Add")
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            if (customPhotos.isEmpty()) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "No custom photos yet. Tap Add above to pick from your phone gallery.",
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            } else {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(customPhotos, key = { it.filePath }) { photoCard ->
                                        Box(
                                            modifier = Modifier
                                                .size(110.dp)
                                                .clip(RoundedCornerShape(18.dp))
                                                .background(Color.White)
                                                .border(2.dp, Color(0xFF3B82F6), RoundedCornerShape(18.dp))
                                        ) {
                                            AsyncImage(
                                                model = File(photoCard.filePath),
                                                contentDescription = "Custom Photo",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            IconButton(
                                                onClick = { onDeletePhoto(photoCard.filePath) },
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .size(32.dp)
                                                    .padding(2.dp)
                                                    .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Preferences section
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Toddler Controls",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            // Haptic Feedback
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Default.Vibration, contentDescription = null, tint = Color(0xFFFF9800))
                                    Column {
                                        Text(text = "Haptic Vibration", fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = "Gentle buzz on screen taps",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Switch(
                                    checked = hapticEnabled,
                                    onCheckedChange = onToggleHaptic,
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFF9800))
                                )
                            }

                            // Show Word Labels
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Default.Label, contentDescription = null, tint = Color(0xFF4CAF50))
                                    Column {
                                        Text(text = "Word Labels", fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = "Show big text labels under pictures",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Switch(
                                    checked = showLabels,
                                    onCheckedChange = onToggleLabels,
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF4CAF50))
                                )
                            }

                            // Immersive mode
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Default.Fullscreen, contentDescription = null, tint = Color(0xFF9C27B0))
                                    Column {
                                        Text(text = "Immersive Full Screen", fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = "Hide navigation & status bars",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Switch(
                                    checked = immersiveMode,
                                    onCheckedChange = onToggleImmersive,
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF9C27B0))
                                )
                            }
                        }
                    }

                    // Security / privacy reassurance
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🛡️", fontSize = 22.sp)
                            Text(
                                text = "100% Offline & Private. No internet access, no tracking, and no ads. Made for toddlers.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF1B5E20)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("settings_done_button"),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Back to Game", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
