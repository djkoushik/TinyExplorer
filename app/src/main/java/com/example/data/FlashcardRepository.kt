package com.example.data

import android.content.Context
import android.net.Uri
import com.example.data.model.Flashcard
import com.example.data.model.FlashcardCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class FlashcardRepository(private val context: Context) {

    private val customImagesDir: File by lazy {
        File(context.filesDir, "custom_images").apply {
            if (!exists()) mkdirs()
        }
    }

    val colors: List<Flashcard.ColorCard> = listOf(
        Flashcard.ColorCard("col_red", "RED", 0xFFFF2A42, "heart", 0xFFFF2A42, 0xFFFF2A42, 0xFFFFFFFF),
        Flashcard.ColorCard("col_blue", "BLUE", 0xFF1E88E5, "cloud", 0xFF1E88E5, 0xFF1E88E5, 0xFFFFFFFF),
        Flashcard.ColorCard("col_yellow", "YELLOW", 0xFFFFD600, "sun", 0xFFFFD600, 0xFFFFD600, 0xFF3E2723),
        Flashcard.ColorCard("col_green", "GREEN", 0xFF2E7D32, "clover", 0xFF2E7D32, 0xFF2E7D32, 0xFFFFFFFF),
        Flashcard.ColorCard("col_orange", "ORANGE", 0xFFFF6D00, "flower", 0xFFFF6D00, 0xFFFF6D00, 0xFFFFFFFF),
        Flashcard.ColorCard("col_purple", "PURPLE", 0xFF8E24AA, "star", 0xFF8E24AA, 0xFF8E24AA, 0xFFFFFFFF),
        Flashcard.ColorCard("col_pink", "PINK", 0xFFE91E63, "heart", 0xFFE91E63, 0xFFE91E63, 0xFFFFFFFF),
        Flashcard.ColorCard("col_brown", "BROWN", 0xFF6D4C41, "circle", 0xFF6D4C41, 0xFF6D4C41, 0xFFFFFFFF),
        Flashcard.ColorCard("col_black", "BLACK", 0xFF1E293B, "star", 0xFF1E293B, 0xFF1E293B, 0xFFFFFFFF),
        Flashcard.ColorCard("col_white", "WHITE", 0xFFF8FAFC, "cloud", 0xFFFFFFFF, 0xFFCBD5E1, 0xFF1E293B),
        Flashcard.ColorCard("col_teal", "TEAL", 0xFF00897B, "drop", 0xFF00897B, 0xFF00897B, 0xFFFFFFFF),
        Flashcard.ColorCard("col_grey", "GREY", 0xFF64748B, "circle", 0xFF64748B, 0xFF64748B, 0xFFFFFFFF),
    )

    val numbers: List<Flashcard.NumberCard> = listOf(
        Flashcard.NumberCard("num_0", 0, "0", "Zero", 0xFFFFFBEB, 0xFFF59E0B, 0xFF78350F),
        Flashcard.NumberCard("num_1", 1, "1", "One", 0xFFEFF6FF, 0xFF3B82F6, 0xFF1E40AF),
        Flashcard.NumberCard("num_2", 2, "2", "Two", 0xFFF0FDF4, 0xFF22C55E, 0xFF166534),
        Flashcard.NumberCard("num_3", 3, "3", "Three", 0xFFFFF7ED, 0xFFFB923C, 0xFF9A3412),
        Flashcard.NumberCard("num_4", 4, "4", "Four", 0xFFFAF5FF, 0xFFA855F7, 0xFF6B21A8),
        Flashcard.NumberCard("num_5", 5, "5", "Five", 0xFFFFF1F2, 0xFFF43F5E, 0xFF9F1239),
        Flashcard.NumberCard("num_6", 6, "6", "Six", 0xFFF0FDFA, 0xFF14B8A6, 0xFF115E59),
        Flashcard.NumberCard("num_7", 7, "7", "Seven", 0xFFFEFCE8, 0xFFEAB308, 0xFF854D0E),
        Flashcard.NumberCard("num_8", 8, "8", "Eight", 0xFFFDF2F8, 0xFFEC4899, 0xFF9D174D),
        Flashcard.NumberCard("num_9", 9, "9", "Nine", 0xFFF5F3FF, 0xFF8B5CF6, 0xFF5B21B6),
        Flashcard.NumberCard("num_10", 10, "10", "Ten", 0xFFEFF6FF, 0xFF2563EB, 0xFF1E3A8A),
        Flashcard.NumberCard("num_11", 11, "11", "Eleven", 0xFFF0FDF4, 0xFF16A34A, 0xFF14532D),
        Flashcard.NumberCard("num_12", 12, "12", "Twelve", 0xFFFFF7ED, 0xFFEA580C, 0xFF7C2D12),
        Flashcard.NumberCard("num_13", 13, "13", "Thirteen", 0xFFFAF5FF, 0xFF9333EA, 0xFF581C87),
        Flashcard.NumberCard("num_14", 14, "14", "Fourteen", 0xFFFFF1F2, 0xFFE11D48, 0xFF881337),
        Flashcard.NumberCard("num_15", 15, "15", "Fifteen", 0xFFF0FDFA, 0xFF0D9488, 0xFF134E4A),
        Flashcard.NumberCard("num_16", 16, "16", "Sixteen", 0xFFFEFCE8, 0xFFCA8A04, 0xFF713F12),
        Flashcard.NumberCard("num_17", 17, "17", "Seventeen", 0xFFFDF2F8, 0xFFDB2777, 0xFF831843),
        Flashcard.NumberCard("num_18", 18, "18", "Eighteen", 0xFFF5F3FF, 0xFF7C3AED, 0xFF4C1D95),
        Flashcard.NumberCard("num_19", 19, "19", "Nineteen", 0xFFEFF6FF, 0xFF1D4ED8, 0xFF172554),
    )

    val letters: List<Flashcard.LetterCard> = listOf(
        Flashcard.LetterCard("let_a", "A", "Apple", "🍎", 0xFFFFF1F2, 0xFFEF4444, 0xFF991B1B),
        Flashcard.LetterCard("let_b", "B", "Ball", "⚽", 0xFFEFF6FF, 0xFF3B82F6, 0xFF1E40AF),
        Flashcard.LetterCard("let_c", "C", "Cat", "🐱", 0xFFFFF7ED, 0xFFF97316, 0xFF9A3412),
        Flashcard.LetterCard("let_d", "D", "Dog", "🐶", 0xFFFEFCE8, 0xFFEAB308, 0xFF854D0E),
        Flashcard.LetterCard("let_e", "E", "Elephant", "🐘", 0xFFF0FDFA, 0xFF14B8A6, 0xFF115E59),
        Flashcard.LetterCard("let_f", "F", "Fish", "🐟", 0xFFEFF6FF, 0xFF06B6D4, 0xFF155E75),
        Flashcard.LetterCard("let_g", "G", "Giraffe", "🦒", 0xFFFEFCE8, 0xFFF59E0B, 0xFF78350F),
        Flashcard.LetterCard("let_h", "H", "House", "🏠", 0xFFF0FDF4, 0xFF22C55E, 0xFF166534),
        Flashcard.LetterCard("let_i", "I", "Ice Cream", "🍦", 0xFFFDF2F8, 0xFFEC4899, 0xFF9D174D),
        Flashcard.LetterCard("let_j", "J", "Juice", "🧃", 0xFFFFF7ED, 0xFFFB923C, 0xFF9A3412),
        Flashcard.LetterCard("let_k", "K", "Kite", "🪁", 0xFFFAF5FF, 0xFFA855F7, 0xFF6B21A8),
        Flashcard.LetterCard("let_l", "L", "Lion", "🦁", 0xFFFEFCE8, 0xFFEAB308, 0xFF854D0E),
        Flashcard.LetterCard("let_m", "M", "Moon", "🌙", 0xFFF5F3FF, 0xFF8B5CF6, 0xFF5B21B6),
        Flashcard.LetterCard("let_n", "N", "Nest", "🪺", 0xFFF7F5F4, 0xFF8D6E63, 0xFF4E342E),
        Flashcard.LetterCard("let_o", "O", "Orange", "🍊", 0xFFFFF7ED, 0xFFFF7A00, 0xFF9A3412),
        Flashcard.LetterCard("let_p", "P", "Panda", "🐼", 0xFFF1F5F9, 0xFF475569, 0xFF0F172A),
        Flashcard.LetterCard("let_q", "Q", "Queen", "👑", 0xFFFAF5FF, 0xFFA855F7, 0xFF6B21A8),
        Flashcard.LetterCard("let_r", "R", "Rainbow", "🌈", 0xFFEFF6FF, 0xFF3B82F6, 0xFF1E40AF),
        Flashcard.LetterCard("let_s", "S", "Sun", "☀️", 0xFFFEFCE8, 0xFFFACC15, 0xFF713F12),
        Flashcard.LetterCard("let_t", "T", "Tree", "🌳", 0xFFF0FDF4, 0xFF16A34A, 0xFF14532D),
        Flashcard.LetterCard("let_u", "U", "Umbrella", "☂️", 0xFFFDF2F8, 0xFFD946EF, 0xFF701A75),
        Flashcard.LetterCard("let_v", "V", "Van", "🚐", 0xFFEFF6FF, 0xFF0EA5E9, 0xFF0369A1),
        Flashcard.LetterCard("let_w", "W", "Whale", "🐋", 0xFFEFF6FF, 0xFF2563EB, 0xFF1E3A8A),
        Flashcard.LetterCard("let_x", "X", "Xylophone", "🎶", 0xFFF5F3FF, 0xFF8B5CF6, 0xFF5B21B6),
        Flashcard.LetterCard("let_y", "Y", "Yacht", "⛵", 0xFFF0FDFA, 0xFF14B8A6, 0xFF115E59),
        Flashcard.LetterCard("let_z", "Z", "Zebra", "🦓", 0xFFF8FAFC, 0xFF334155, 0xFF0F172A),
    )

    val animals: List<Flashcard.AnimalCard> = listOf(
        Flashcard.AnimalCard("ani_dog", "DOG", "Woof Woof!", "🐶", "Loyal and friendly buddy", "file:///android_asset/animals/dog.jpg", 0xFFFFFBEB, 0xFFEAB308, 0xFF713F12),
        Flashcard.AnimalCard("ani_cat", "CAT", "Meow Meow!", "🐱", "Loves cuddles and purring", "file:///android_asset/animals/cat.jpg", 0xFFFFF7ED, 0xFFF97316, 0xFF9A3412),
        Flashcard.AnimalCard("ani_lion", "LION", "Roaaar!", "🦁", "King of the savanna", "file:///android_asset/animals/lion.jpg", 0xFFFEFCE8, 0xFFF59E0B, 0xFF78350F),
        Flashcard.AnimalCard("ani_elephant", "ELEPHANT", "Toot Toot!", "🐘", "Has a long, playful trunk", "file:///android_asset/animals/elephant.jpg", 0xFFEFF6FF, 0xFF3B82F6, 0xFF1E40AF),
        Flashcard.AnimalCard("ani_duck", "DUCK", "Quack Quack!", "🦆", "Splashes happily in water", "file:///android_asset/animals/duck.jpg", 0xFFFEFCE8, 0xFFFACC15, 0xFF854D0E),
        Flashcard.AnimalCard("ani_cow", "COW", "Moo Moo!", "🐮", "Gives delicious fresh milk", "file:///android_asset/animals/cow.jpg", 0xFFF8FAFC, 0xFF475569, 0xFF0F172A),
        Flashcard.AnimalCard("ani_monkey", "MONKEY", "Ooh Ooh Aah!", "🐵", "Swings high up in trees", "file:///android_asset/animals/monkey.jpg", 0xFFFFF7ED, 0xFFEA580C, 0xFF7C2D12),
        Flashcard.AnimalCard("ani_bear", "BEAR", "Grrr!", "🐻", "Big, warm and fluffy", "file:///android_asset/animals/bear.jpg", 0xFFF7F5F4, 0xFF8D6E63, 0xFF4E342E),
        Flashcard.AnimalCard("ani_frog", "FROG", "Ribbit Ribbit!", "🐸", "Hops over green lily pads", "file:///android_asset/animals/frog.jpg", 0xFFF0FDF4, 0xFF22C55E, 0xFF166534),
        Flashcard.AnimalCard("ani_giraffe", "GIRAFFE", "Munch Munch!", "🦒", "Reaches tall yummy leaves", "file:///android_asset/animals/giraffe.jpg", 0xFFFEFCE8, 0xFFF59E0B, 0xFF713F12),
        Flashcard.AnimalCard("ani_tiger", "TIGER", "Grrrr Roar!", "🐯", "Has brave orange stripes", "file:///android_asset/animals/tiger.jpg", 0xFFFFF7ED, 0xFFFF7A00, 0xFF9A3412),
        Flashcard.AnimalCard("ani_rabbit", "RABBIT", "Hop Hop!", "🐰", "Has long soft ears", "file:///android_asset/animals/rabbit.jpg", 0xFFFDF2F8, 0xFFEC4899, 0xFF9D174D),
        Flashcard.AnimalCard("ani_penguin", "PENGUIN", "Waddle Waddle!", "🐧", "Slides across sparkling ice", "file:///android_asset/animals/penguin.jpg", 0xFFEFF6FF, 0xFF0284C7, 0xFF0C4A6E),
        Flashcard.AnimalCard("ani_sheep", "SHEEP", "Baa Baa!", "🐑", "Wears super fluffy wool", "file:///android_asset/animals/sheep.jpg", 0xFFF8FAFC, 0xFF64748B, 0xFF1E293B),
        Flashcard.AnimalCard("ani_horse", "HORSE", "Neigh Neigh!", "🐴", "Gallops fast across fields", "file:///android_asset/animals/horse.jpg", 0xFFFFF7ED, 0xFFB45309, 0xFF78350F),
        Flashcard.AnimalCard("ani_pig", "PIG", "Oink Oink!", "🐷", "Loves playful muddy puddles", "file:///android_asset/animals/pig.jpg", 0xFFFDF2F8, 0xFFF472B6, 0xFF9D174D),
        Flashcard.AnimalCard("ani_panda", "PANDA", "Nom Nom!", "🐼", "Eats yummy green bamboo", "file:///android_asset/animals/panda.jpg", 0xFFF1F5F9, 0xFF334155, 0xFF0F172A),
        Flashcard.AnimalCard("ani_dolphin", "DOLPHIN", "Click Click!", "🐬", "Leaps high out of the sea", "file:///android_asset/animals/dolphin.jpg", 0xFFEFF6FF, 0xFF0EA5E9, 0xFF075985),
        Flashcard.AnimalCard("ani_fox", "FOX", "Yip Yip!", "🦊", "Has a bushy red tail", "file:///android_asset/animals/fox.jpg", 0xFFFFF7ED, 0xFFF97316, 0xFF9A3412),
        Flashcard.AnimalCard("ani_owl", "OWL", "Hoot Hoot!", "🦉", "Wise night explorer with big eyes", "file:///android_asset/animals/owl.jpg", 0xFFF5F3FF, 0xFF8B5CF6, 0xFF4C1D95),
        Flashcard.AnimalCard("ani_koala", "KOALA", "Yawn Yawn!", "🐨", "Sleeps peacefully in gum trees", "file:///android_asset/animals/koala.jpg", 0xFFF8FAFC, 0xFF64748B, 0xFF334155),
        Flashcard.AnimalCard("ani_whale", "WHALE", "Splash Splash!", "🐳", "Gentle giant of the deep blue", "file:///android_asset/animals/whale.jpg", 0xFFEFF6FF, 0xFF2563EB, 0xFF1E3A8A),
    )

    val vehicles: List<Flashcard.VehicleCard> = listOf(
        Flashcard.VehicleCard("veh_car", "CAR", "Beep Beep!", "🚗", "On the road", "file:///android_asset/vehicles/car.jpg", 0xFFFFF1F2, 0xFFEF4444, 0xFF991B1B),
        Flashcard.VehicleCard("veh_bus", "BUS", "Honk Honk!", "🚌", "School bus ride", "file:///android_asset/vehicles/bus.jpg", 0xFFFEFCE8, 0xFFEAB308, 0xFF713F12),
        Flashcard.VehicleCard("veh_train", "TRAIN", "Choo Choo!", "🚂", "Clickety-clack on tracks", "file:///android_asset/vehicles/train.jpg", 0xFFEFF6FF, 0xFF3B82F6, 0xFF1E40AF),
        Flashcard.VehicleCard("veh_airplane", "AIRPLANE", "Whoooosh!", "✈️", "Soaring above the clouds", "file:///android_asset/vehicles/airplane.jpg", 0xFFEFF6FF, 0xFF0284C7, 0xFF0369A1),
        Flashcard.VehicleCard("veh_bicycle", "BICYCLE", "Ring Ring!", "🚲", "Pedal with your feet", "file:///android_asset/vehicles/bicycle.jpg", 0xFFF0FDF4, 0xFF22C55E, 0xFF166534),
        Flashcard.VehicleCard("veh_fire_truck", "FIRE TRUCK", "Wee-Woo Wee-Woo!", "🚒", "Hurry to the rescue!", "file:///android_asset/vehicles/fire_truck.jpg", 0xFFFFF1F2, 0xFFDC2626, 0xFF7F1D1D),
        Flashcard.VehicleCard("veh_police_car", "POLICE CAR", "Wee-Woo!", "🚓", "Keeping everyone safe", "file:///android_asset/vehicles/police_car.jpg", 0xFFEFF6FF, 0xFF1D4ED8, 0xFF172554),
        Flashcard.VehicleCard("veh_boat", "BOAT", "Toot Toot!", "⛵", "Sailing on the waves", "file:///android_asset/vehicles/boat.jpg", 0xFFF0FDFA, 0xFF14B8A6, 0xFF115E59),
        Flashcard.VehicleCard("veh_helicopter", "HELICOPTER", "Chop Chop Chop!", "🚁", "Spinning blades in the sky", "file:///android_asset/vehicles/helicopter.jpg", 0xFFFAF5FF, 0xFFA855F7, 0xFF6B21A8),
        Flashcard.VehicleCard("veh_rocket", "ROCKET", "3-2-1 Blast Off!", "🚀", "Zooming to the stars", "file:///android_asset/vehicles/rocket.jpg", 0xFFF5F3FF, 0xFF7C3AED, 0xFF4C1D95),
        Flashcard.VehicleCard("veh_tractor", "TRACTOR", "Chug Chug Chug!", "🚜", "Working on the sunny farm", "file:///android_asset/vehicles/tractor.jpg", 0xFFFEFCE8, 0xFFCA8A04, 0xFF713F12),
        Flashcard.VehicleCard("veh_motorcycle", "MOTORCYCLE", "Vroom Vroom!", "🏍️", "Zooming along two wheels", "file:///android_asset/vehicles/motorcycle.jpg", 0xFFFFF7ED, 0xFFEA580C, 0xFF7C2D12),
        Flashcard.VehicleCard("veh_ambulance", "AMBULANCE", "Wee-Woo Help!", "🚑", "Rush to help friends", "file:///android_asset/vehicles/ambulance.jpg", 0xFFFFF1F2, 0xFFE11D48, 0xFF881337),
        Flashcard.VehicleCard("veh_submarine", "SUBMARINE", "Glug Glug Glug!", "🚢", "Exploring deep sea wonders", "file:///android_asset/vehicles/submarine.jpg", 0xFFEFF6FF, 0xFF0284C7, 0xFF082F49),
        Flashcard.VehicleCard("veh_balloon", "HOT AIR BALLOON", "Float Up High!", "🎈", "Drifting with the breeze", "file:///android_asset/vehicles/balloon.jpg", 0xFFFDF2F8, 0xFFEC4899, 0xFF9D174D),
        Flashcard.VehicleCard("veh_excavator", "EXCAVATOR", "Dig Dig Dig!", "🚜", "Scooping up the dirt", "file:///android_asset/vehicles/excavator.jpg", 0xFFFFF7ED, 0xFFFF7A00, 0xFF9A3412),
    )

    fun getCustomCards(): List<Flashcard.CustomCard> {
        val files = customImagesDir.listFiles { file ->
            file.isFile && (file.name.endsWith(".jpg", true) ||
                    file.name.endsWith(".png", true) ||
                    file.name.endsWith(".webp", true) ||
                    file.name.endsWith(".jpeg", true))
        }?.sortedByDescending { it.lastModified() } ?: emptyList()

        return files.mapIndexed { index, file ->
            Flashcard.CustomCard(
                id = "custom_${file.name}",
                title = "PHOTO ${index + 1}",
                filePath = file.absolutePath
            )
        }
    }

    suspend fun saveCustomImage(uri: Uri): Flashcard.CustomCard? = withContext(Dispatchers.IO) {
        try {
            val fileName = "custom_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}.jpg"
            val targetFile = File(customImagesDir, fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }

            if (targetFile.exists() && targetFile.length() > 0) {
                Flashcard.CustomCard(
                    id = "custom_${targetFile.name}",
                    title = "MY PHOTO",
                    filePath = targetFile.absolutePath
                )
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteCustomImage(filePath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val file = File(filePath)
            if (file.exists() && file.parentFile == customImagesDir) {
                file.delete()
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getAllCards(): List<Flashcard> {
        val list = mutableListOf<Flashcard>()
        list.addAll(colors)
        list.addAll(numbers)
        list.addAll(letters)
        list.addAll(animals)
        list.addAll(vehicles)
        list.addAll(getCustomCards())
        return list
    }

    fun getCardsForCategory(category: FlashcardCategory): List<Flashcard> {
        return when (category) {
            FlashcardCategory.ALL -> getAllCards()
            FlashcardCategory.COLORS -> colors
            FlashcardCategory.NUMBERS -> numbers
            FlashcardCategory.LETTERS -> letters
            FlashcardCategory.ANIMALS -> animals
            FlashcardCategory.VEHICLES -> vehicles
            FlashcardCategory.CUSTOM -> getCustomCards()
        }
    }
}
