package com.justeattakeaway.intervalannotatedstring

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

/**
 * Creates an [IntervalAnnotatedString] from a [String] instance.
 *
 * @param value raw string resource.
 *
 * @see String
 * @see IntervalAnnotatedString
 */
@Composable
fun intervalAnnotatedString(value: String): IntervalAnnotatedString =
    IntervalAnnotatedString(value)

/**
 * Loads an [IntervalAnnotatedString] from a string resource.
 *
 * @param stringRes the resource identifier
 *
 * @see IntervalAnnotatedString
 */
@Composable
fun intervalAnnotatedString(@StringRes stringRes: Int): IntervalAnnotatedString =
    IntervalAnnotatedString(stringResource(stringRes))

/**
 * Loads an [IntervalAnnotatedString] from a string resource with given format arguments.
 *
 * @param stringRes the resource identifier
 * @param formatArgs the format arguments
 *
 * @see IntervalAnnotatedString
 */
@Composable
fun intervalAnnotatedString(@StringRes stringRes: Int, vararg formatArgs: Any): IntervalAnnotatedString =
    IntervalAnnotatedString(stringResource(stringRes, *formatArgs))

/**
 * Loads an [IntervalAnnotatedString] from a plural string resource.
 *
 * @param stringRes the resource identifier
 * @param count the count
 *
 * @see IntervalAnnotatedString
 */
@Composable
fun pluralIntervalAnnotatedString(@PluralsRes stringRes: Int, count: Int): IntervalAnnotatedString =
    IntervalAnnotatedString(pluralStringResource(stringRes, count))

/**
 * Loads an [IntervalAnnotatedString] from a plural string resource with given format arguments.
 *
 * @param stringRes the resource identifier
 * @param count the count
 * @param formatArgs the format arguments
 *
 * @see IntervalAnnotatedString
 */
@Composable
fun pluralIntervalAnnotatedString(
    @PluralsRes stringRes: Int,
    count: Int,
    vararg formatArgs: Any
): IntervalAnnotatedString =
    IntervalAnnotatedString(
        pluralStringResource(stringRes, count, *formatArgs)
    )
