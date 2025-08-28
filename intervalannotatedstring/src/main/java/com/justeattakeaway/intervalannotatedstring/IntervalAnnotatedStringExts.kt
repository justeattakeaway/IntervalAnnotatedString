package com.justeattakeaway.intervalannotatedstring

import android.text.SpannableString
import androidx.annotation.AnyThread
import androidx.annotation.CheckResult
import androidx.compose.ui.text.AnnotatedString
import com.justeattakeaway.intervalannotatedstring.InlineIntervalSyntaxParser.EmptyInlineTextException
import com.justeattakeaway.intervalannotatedstring.InlineIntervalSyntaxParser.NoIdException

/**
 * Converts an interval annotated string instance to [SpannableString].
 *
 * @see IntervalAnnotatedString
 * @see SpannableString
 */
@AnyThread
@CheckResult
@Throws(NoIdException::class, EmptyInlineTextException::class)
inline fun IntervalAnnotatedString.asSpannableString(
    onAddIntervalStyle: OnApplyIntervalTransformation<SpannableString>
): SpannableString =
    transform(
        onTransformText = { SpannableString(it) },
        onApplyIntervalTransformation = onAddIntervalStyle,
    )

/**
 * Converts an interval annotated string instance to [AnnotatedString].
 *
 * @see IntervalAnnotatedString
 * @see AnnotatedString
 */
@AnyThread
@CheckResult
@Throws(NoIdException::class, EmptyInlineTextException::class)
inline fun IntervalAnnotatedString.asAnnotatedString(
    onAddIntervalStyle: OnApplyIntervalTransformation<AnnotatedString.Builder>
): AnnotatedString =
    transform(
        onTransformText = { AnnotatedString.Builder(it) },
        onApplyIntervalTransformation = onAddIntervalStyle,
    ).toAnnotatedString()
