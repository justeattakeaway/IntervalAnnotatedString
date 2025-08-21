package com.github.justeattakeaway.intervalannotatedstring

import androidx.annotation.AnyThread
import androidx.annotation.CheckResult
import androidx.compose.ui.text.AnnotatedString
import com.github.justeattakeaway.intervalannotatedstring.InlineIntervalSyntaxParser.EmptyInlineTextException
import com.github.justeattakeaway.intervalannotatedstring.InlineIntervalSyntaxParser.NoIdException

/**
 * Converts an interval annotated string instance to AnnotatedString.
 *
 * @see IntervalAnnotatedString
 * @see AnnotatedString
 */
@AnyThread
@CheckResult
@Throws(NoIdException::class, EmptyInlineTextException::class)
inline fun IntervalAnnotatedString.asAnnotatedString(
    onAddIntervalStyle: OnTransformInterval<AnnotatedString.Builder>
): AnnotatedString =
    transform(
        onTransformText = { AnnotatedString.Builder(it) },
        onTransformInterval = onAddIntervalStyle,
    ).toAnnotatedString()
