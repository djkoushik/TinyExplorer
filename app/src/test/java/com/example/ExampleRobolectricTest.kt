package com.example

import android.content.Context
import android.graphics.BitmapFactory
import androidx.test.core.app.ApplicationProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.data.DataStoreManager
import com.example.data.FlashcardRepository
import com.example.data.model.FlashcardCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Tiny Explorer", appName)
    }

    @Test
    fun `verify repository content counts`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = FlashcardRepository(context)

        assertEquals("Colors count should be 12", 12, repository.colors.size)
        assertEquals("Numbers count should be 20 (0 to 19)", 20, repository.numbers.size)
        assertEquals("Letters count should be 26 (A to Z)", 26, repository.letters.size)
        assertTrue("Animals count should be >= 20", repository.animals.size >= 20)
        assertTrue("Vehicles count should be >= 15", repository.vehicles.size >= 15)

        val allCards = repository.getAllCards()
        assertTrue("Total cards should be at least 93", allCards.size >= 93)
    }

    @Test
    fun `verify asset images can be opened and decoded`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = FlashcardRepository(context)

        repository.animals.forEach { animal ->
            val relativePath = animal.imageAssetPath.removePrefix("file:///android_asset/")
            val stream = context.assets.open(relativePath)
            val bitmap = BitmapFactory.decodeStream(stream)
            assertNotNull("Animal bitmap for ${animal.title} must decode", bitmap)
        }

        repository.vehicles.forEach { vehicle ->
            val relativePath = vehicle.imageAssetPath.removePrefix("file:///android_asset/")
            val stream = context.assets.open(relativePath)
            val bitmap = BitmapFactory.decodeStream(stream)
            assertNotNull("Vehicle bitmap for ${vehicle.title} must decode", bitmap)
        }
    }

    @Test
    fun `verify coil asset loading`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data("file:///android_asset/animals/dog.jpg")
            .build()
        val result = imageLoader.execute(request)
        assertTrue("Coil should load asset image: $result", result is SuccessResult)
    }

    @Test
    fun `verify custom card name persistence`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dataStoreManager = DataStoreManager(context)

        dataStoreManager.setCardCustomName("ani_dog", "Puppy Dog")
        val names = dataStoreManager.customCardNamesFlow.first()
        assertEquals("Puppy Dog", names["ani_dog"])

        dataStoreManager.resetCardCustomName("ani_dog")
        val resetNames = dataStoreManager.customCardNamesFlow.first()
        assertTrue("ani_dog custom name should be removed", resetNames["ani_dog"] == null)
    }
}
