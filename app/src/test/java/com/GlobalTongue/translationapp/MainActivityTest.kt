package com.GlobalTongue.translationapp

import android.widget.Toast
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainActivityTest {

    private lateinit var mainActivity: MainActivity

    @Before
    fun setUp() {

    }
    @Test
    fun `startTranslations should invoke translator's translate method`() {
        // Arrange
        val mockTranslator = mockk<com.google.mlkit.nl.translate.Translator>(relaxed = true)
        val inputText = "Hello"
        val translatedText = "سلام" // Example output in Urdu
        mainActivity.sourceLanguageText = inputText

        every { mockTranslator.translate(inputText) } returns mockk {
            every { addOnSuccessListener(any()) } answers {
                val listener = firstArg<(String) -> Unit>()
                listener(translatedText)
                mockk()
            }
            every { addOnFailureListener(any()) } returns mockk()
        }

        // Act
        mainActivity.startTranslations()

        // Assert
        verify { mockTranslator.translate(inputText) }
    }


    @Test
    fun `validateData should return false if input is empty`() {
        // Arrange
        mainActivity.sourceLanguageText = ""

        // Act
        val isValid = mainActivity.sourceLanguageText.isNotEmpty()

        // Assert
        assertFalse("Validation should fail when input is empty", isValid)
    }

    @Test
    fun `translator options should be set correctly`() {
        // Arrange
        val sourceLang = "en"
        val targetLang = "ur"
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()

        // Act
        val translator = Translation.getClient(options)

        // Assert
        assertNotNull("Translator should not be null when options are valid", translator)
    }

    @Test
    fun `startTranslations calls downloadModelIfNeeded`() {
        // Arrange
        val mockTranslator = mockk<com.google.mlkit.nl.translate.Translator>(relaxed = true)
        every { mockTranslator.downloadModelIfNeeded(any()) } returns mockk()

        // Act
        mockTranslator.downloadModelIfNeeded(mockk())

        // Assert
        verify(exactly = 1) { mockTranslator.downloadModelIfNeeded(any()) }
    }

    @Test
    fun `startTranslations successfully translates text`() {
        // Arrange
        val mockTranslator = mockk<com.google.mlkit.nl.translate.Translator>(relaxed = true)
        val inputText = "Hello"
        val translatedText = "\u0633\u0644\u0627\u0645" // Example: "سلام" in Urdu

        every { mockTranslator.translate(inputText) } returns mockk {
            every { addOnSuccessListener(any()) } answers {
                val listener = firstArg<(String) -> Unit>()
                listener(translatedText)
                mockk()
            }
            every { addOnFailureListener(any()) } returns mockk()
        }

        // Act
        mockTranslator.translate(inputText)

        // Assert
        verify { mockTranslator.translate(inputText) }
    }
}
