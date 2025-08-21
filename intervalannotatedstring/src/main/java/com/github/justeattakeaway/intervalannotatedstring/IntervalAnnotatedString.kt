package com.github.justeattakeaway.intervalannotatedstring

import androidx.annotation.AnyThread
import androidx.annotation.CheckResult
import com.github.justeattakeaway.intervalannotatedstring.InlineIntervalSyntaxParser.EmptyInlineTextException
import com.github.justeattakeaway.intervalannotatedstring.InlineIntervalSyntaxParser.NoIdException

/**
 * Builds a target rendering string from the cleared version of the annotated string.
 */
typealias OnTransformText<T> = (text: String) -> T

/**
 * Applies interval transformation on the target rendering string implementation.
 */
typealias OnTransformInterval<T> = T.(id: String, startsAt: Int, length: Int) -> Unit

/**
 * Primary class for working with interval annotated strings.
 * Wraps a raw annotated string, allowing to perform transformation to the target rendering string implementation.
 */
@JvmInline
value class IntervalAnnotatedString(
    val rawString: String,
) {
    /**
     * Performs a transformation of raw text (with embedded intervals metadata) to the target rendering string
     * implementation. Can be used to transform the string to an instance of
     * [androidx.compose.ui.text.AnnotatedString] or [com.jet.pie2.utils.StyledString].
     *
     * @param T The target type for the transformed string. This is typically a class
     * designed to represent a rendering text, such as
     * [androidx.compose.ui.text.AnnotatedString] or [com.jet.pie2.utils.StyledString].
     * The chosen type [T] should generally be constructible from a plain string (via [onTransformText]) and
     * should allow for range-based modifications, styling, or annotations
     * (via [onTransformInterval]) to be useful with this transformation.
     *
     * @throws NoIdException if the id subsection is empty
     * @throws EmptyInlineTextException if the raw text within the interval annotated string area is empty
     */
    @AnyThread
    @CheckResult
    @Throws(NoIdException::class, EmptyInlineTextException::class)
    inline fun <T> transform(
        onTransformText: OnTransformText<T>,
        onTransformInterval: OnTransformInterval<T>,
    ): T {
        val (clearedText, intervals) = InlineIntervalSyntaxParser.parseMetadata(rawString)
        val outResult = onTransformText(clearedText)
        for (interval in intervals) {
            outResult.onTransformInterval(interval.id, interval.startsAt, interval.length)
        }
        return outResult
    }
}
