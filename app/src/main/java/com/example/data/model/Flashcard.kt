package com.example.data.model

sealed class Flashcard(
    open val id: String,
    open val category: FlashcardCategory,
    open val title: String,
    open val subtitle: String?,
    open val backgroundColorHex: Long,
    open val accentColorHex: Long,
    open val textColorHex: Long = 0xFF1E293B,
) {
    data class ColorCard(
        override val id: String,
        override val title: String,
        val colorHex: Long,
        val shapeName: String = "circle",
        override val backgroundColorHex: Long = colorHex,
        override val accentColorHex: Long = colorHex,
        override val textColorHex: Long = 0xFFFFFFFF,
    ) : Flashcard(
        id = id,
        category = FlashcardCategory.COLORS,
        title = title,
        subtitle = null,
        backgroundColorHex = backgroundColorHex,
        accentColorHex = accentColorHex,
        textColorHex = textColorHex
    )

    data class NumberCard(
        override val id: String,
        val number: Int,
        override val title: String = number.toString(),
        val wordName: String,
        override val backgroundColorHex: Long,
        override val accentColorHex: Long,
        override val textColorHex: Long = 0xFF1E293B,
    ) : Flashcard(
        id = id,
        category = FlashcardCategory.NUMBERS,
        title = title,
        subtitle = wordName,
        backgroundColorHex = backgroundColorHex,
        accentColorHex = accentColorHex,
        textColorHex = textColorHex
    )

    data class LetterCard(
        override val id: String,
        val letter: String,
        val exampleWord: String,
        val emoji: String,
        override val backgroundColorHex: Long,
        override val accentColorHex: Long,
        override val textColorHex: Long = 0xFF1E293B,
    ) : Flashcard(
        id = id,
        category = FlashcardCategory.LETTERS,
        title = letter,
        subtitle = "$letter is for $exampleWord",
        backgroundColorHex = backgroundColorHex,
        accentColorHex = accentColorHex,
        textColorHex = textColorHex
    )

    data class AnimalCard(
        override val id: String,
        override val title: String,
        val sound: String,
        val emoji: String,
        val funFact: String,
        val imageAssetPath: String,
        override val backgroundColorHex: Long,
        override val accentColorHex: Long,
        override val textColorHex: Long = 0xFF1E293B,
    ) : Flashcard(
        id = id,
        category = FlashcardCategory.ANIMALS,
        title = title,
        subtitle = sound,
        backgroundColorHex = backgroundColorHex,
        accentColorHex = accentColorHex,
        textColorHex = textColorHex
    )

    data class VehicleCard(
        override val id: String,
        override val title: String,
        val sound: String,
        val emoji: String,
        val environment: String,
        val imageAssetPath: String,
        override val backgroundColorHex: Long,
        override val accentColorHex: Long,
        override val textColorHex: Long = 0xFF1E293B,
    ) : Flashcard(
        id = id,
        category = FlashcardCategory.VEHICLES,
        title = title,
        subtitle = sound,
        backgroundColorHex = backgroundColorHex,
        accentColorHex = accentColorHex,
        textColorHex = textColorHex
    )

    data class CustomCard(
        override val id: String,
        override val title: String,
        val filePath: String,
        override val backgroundColorHex: Long = 0xFFF8FAFC,
        override val accentColorHex: Long = 0xFF3B82F6,
        override val textColorHex: Long = 0xFF0F172A,
    ) : Flashcard(
        id = id,
        category = FlashcardCategory.CUSTOM,
        title = title,
        subtitle = "Photo",
        backgroundColorHex = backgroundColorHex,
        accentColorHex = accentColorHex,
        textColorHex = textColorHex
    )
}
