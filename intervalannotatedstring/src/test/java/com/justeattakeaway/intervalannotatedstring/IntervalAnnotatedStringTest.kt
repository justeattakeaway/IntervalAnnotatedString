package com.github.justeattakeaway.intervalannotatedstring

import android.annotation.SuppressLint
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

typealias TestRenderingString = String

@RunWith(JUnit4::class)
class IntervalAnnotatedStringTest {
    private val onTransformText = mockk<OnTransformText<TestRenderingString>>()
    private val onTransformInterval = mockk<OnTransformInterval<TestRenderingString>>()

    @Before
    fun setUp() {
        mockkObject(InlineIntervalSyntaxParser)

        every { onTransformText.invoke(any()) } returns TEST_RENDERING_STRING
        every { onTransformInterval.invoke(any(), any(), any(), any()) } returns Unit
    }

    @SuppressLint("CheckResult")
    @Test
    fun `test OnTransformText is called once for a string`() {
        // Given
        val text = IntervalAnnotatedString(TEST_INTERVAL_ANNOTATED_STRING)
        every { InlineIntervalSyntaxParser.parseMetadata(eq(TEST_INTERVAL_ANNOTATED_STRING)) } returns
            InlineIntervalSyntaxParser.ParsingResult(
                clearedText = TEST_INTERVAL_CLEARED_STRING,
                intervals = listOf(TEST_INTERVAL_1),
            )

        // When
        text.transform(onTransformText, onTransformInterval)

        // Then
        verify(exactly = 1) { onTransformText.invoke(eq(TEST_INTERVAL_CLEARED_STRING)) }
    }

    @SuppressLint("CheckResult")
    @Test
    fun `test onTransformInterval is called for every interval`() {
        // Given
        val text = IntervalAnnotatedString(TEST_INTERVAL_ANNOTATED_STRING)
        every { InlineIntervalSyntaxParser.parseMetadata(eq(TEST_INTERVAL_ANNOTATED_STRING)) } returns
            InlineIntervalSyntaxParser.ParsingResult(
                clearedText = TEST_INTERVAL_CLEARED_STRING,
                intervals = listOf(TEST_INTERVAL_1, TEST_INTERVAL_2, TEST_INTERVAL_3),
            )

        // When
        text.transform(onTransformText, onTransformInterval)

        // Then
        verify {
            onTransformInterval.invoke(
                eq(TEST_RENDERING_STRING),
                eq(TEST_INTERVAL_1.id),
                eq(TEST_INTERVAL_1.startsAt),
                eq(TEST_INTERVAL_1.length),
            )
            onTransformInterval.invoke(
                eq(TEST_RENDERING_STRING),
                eq(TEST_INTERVAL_2.id),
                eq(TEST_INTERVAL_2.startsAt),
                eq(TEST_INTERVAL_2.length),
            )
            onTransformInterval.invoke(
                eq(TEST_RENDERING_STRING),
                eq(TEST_INTERVAL_3.id),
                eq(TEST_INTERVAL_3.startsAt),
                eq(TEST_INTERVAL_3.length),
            )
        }
    }

    @After
    fun tearDown() {
        unmockkObject(InlineIntervalSyntaxParser)
    }

    private companion object {
        const val TEST_RENDERING_STRING = ""
        const val TEST_INTERVAL_ANNOTATED_STRING = "hello world with [a link](1)"
        const val TEST_INTERVAL_CLEARED_STRING = "hello world with a link"

        val TEST_INTERVAL_1 =
            InlineIntervalSyntaxParser.InlineInterval(
                id = "1",
                startsAt = 1,
                length = 5,
            )

        val TEST_INTERVAL_2 =
            InlineIntervalSyntaxParser.InlineInterval(
                id = "2",
                startsAt = 15,
                length = 23,
            )

        val TEST_INTERVAL_3 =
            InlineIntervalSyntaxParser.InlineInterval(
                id = "3",
                startsAt = 47,
                length = 91,
            )
    }
}
