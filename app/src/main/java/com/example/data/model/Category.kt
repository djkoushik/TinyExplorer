package com.example.data.model

enum class FlashcardCategory(
    val id: String,
    val displayName: String,
    val iconEmoji: String,
) {
    ALL("all", "All", "🌈"),
    COLORS("colors", "Colors", "🎨"),
    NUMBERS("numbers", "1 2 3", "🔢"),
    LETTERS("letters", "A B C", "🔤"),
    ANIMALS("animals", "Animals", "🐶"),
    VEHICLES("vehicles", "Vehicles", "🚗"),
    CUSTOM("custom", "My Photos", "📷");

    companion object {
        fun fromId(id: String): FlashcardCategory {
            return entries.firstOrNull { it.id == id } ?: ALL
        }
    }
}
