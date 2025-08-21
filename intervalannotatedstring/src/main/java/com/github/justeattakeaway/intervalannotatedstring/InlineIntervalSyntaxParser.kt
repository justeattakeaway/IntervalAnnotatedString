package com.github.justeattakeaway.intervalannotatedstring

import androidx.annotation.AnyThread
import androidx.annotation.CheckResult

/**
 * Inline interval syntax was introduced to addressed common localisation headaches.
 *
 * Essentially, inline interval syntax enriches a string resource by embedding associated interval metadata
 * directly within it. For instance,
 *
 * I am [an interval inlined](1) string, and I am really useful [with localisations!](2)
 *
 * The class handles the core parsing, making it easy to pull out that metadata.
 *
 * @see IntervalAnnotatedString
 */
object InlineIntervalSyntaxParser {
    private const val TOKEN_SQUARE_OPEN_PARENTHESIS = '['
    private const val TOKEN_SQUARE_CLOSE_PARENTHESIS = ']'
    private const val TOKEN_ROUND_OPEN_PARENTHESIS = '('
    private const val TOKEN_ROUND_CLOSE_PARENTHESIS = ')'
    private const val NO_CORRESPONDING_CLOSING_PARENTHESIS = -1

    /**
     * Raised when the annotated substring is empty.
     */
    class EmptyInlineTextException(
        originalText: String,
        column: Int,
    ) : RuntimeException("Inline text was empty in \"$originalText\" around column $column")

    /**
     * Raised when there is no id for the annotated interval.
     */
    class NoIdException(
        originalText: String,
        column: Int,
    ) : RuntimeException("Id was empty in \"$originalText\" around column $column")

    /**
     * Embedded interval metadata.
     */
    data class InlineInterval(
        /**
         * Id of the interval, does not have to be unique across the string.
         * Id helps to identify specific part of the string and apply the necessary set of transformations.
         */
        val id: String,
        /**
         * Index where the interval starts (inclusive).
         */
        val startsAt: Int,
        /**
         * Length of the interval.
         * Can be used to figure out the end of the annotated section:
         * [startsAt, startsAt + length).
         */
        val length: Int,
    )

    /**
     * The result of a metadata extraction request.
     */
    data class ParsingResult(
        /**
         * "Cleaned" version of the text without any embedded interval metadata.
         */
        val clearedText: String,
        /**
         * A collection of meta-intervals that was used to annotate the original string.
         */
        val intervals: Collection<InlineInterval>,
    )

    /**
     * Parses interval metadata in the string, if presented.
     * Accepts any string as long it is formatted in the proposed syntax, otherwise leaves the
     * string "as is".
     * The method imposes linear running time (O(n) where n is the length of the string) and linear
     * memory constraints on the caller.
     *
     * @return [ParsingResult] includes a "cleaned" version of the string
     * (without any embedded metadata), and a collection of the intervals that were found.
     * This collection will be empty if no metadata was present or if the syntax is malformed.
     *
     * @throws NoIdException if there is an interval annotation but the id is empty
     * @throws EmptyInlineTextException if there is an interval annotation which does not annotate any substring
     *
     * @see ParsingResult
     * @see InlineInterval
     */
    @AnyThread
    @CheckResult
    @Throws(NoIdException::class, EmptyInlineTextException::class)
    fun parseMetadata(rawText: String): ParsingResult {
        val balanceLookup = calculateBalanceLookup(rawText)
        return parseMetadata(rawText, balanceLookup)
    }

    @AnyThread
    @CheckResult
    @Throws(NoIdException::class, EmptyInlineTextException::class)
    private fun parseMetadata(
        rawText: String,
        balanceLookup: Array<Int>,
    ): ParsingResult {
        val builder = StringBuilder()
        val intervals = mutableListOf<InlineInterval>()

        var index = 0
        while (index < rawText.length) {
            val isOpeningSquareBracket = rawText[index] == TOKEN_SQUARE_OPEN_PARENTHESIS
            val hasCorrespondingClosingSquareBracket = balanceLookup[index] != NO_CORRESPONDING_CLOSING_PARENTHESIS
            val hasSymbolAfterClosingSquareBracket = (balanceLookup[index] + 1) < rawText.length
            val isFollowedByOpeningRoundBracket =
                hasSymbolAfterClosingSquareBracket &&
                    (rawText[balanceLookup[index] + 1] == TOKEN_ROUND_OPEN_PARENTHESIS)
            val hasCorrespondingClosingRoundBracket =
                hasSymbolAfterClosingSquareBracket &&
                    (balanceLookup[balanceLookup[index] + 1] != NO_CORRESPONDING_CLOSING_PARENTHESIS)

            // If is opening square bracket, and balanced,
            // and the next symbol after balancing is opening round bracket,
            // and is also balanced, then successfully matched.
            val isPatternMatched =
                isOpeningSquareBracket &&
                    hasCorrespondingClosingSquareBracket &&
                    hasSymbolAfterClosingSquareBracket &&
                    isFollowedByOpeningRoundBracket &&
                    hasCorrespondingClosingRoundBracket
            if (isPatternMatched) {
                val intervalText = rawText.substring(index + 1, balanceLookup[index])

                if (intervalText.isEmpty()) {
                    throw EmptyInlineTextException(
                        originalText = rawText,
                        column = index,
                    )
                }

                val intervalId = rawText.substring(balanceLookup[index] + 2, balanceLookup[balanceLookup[index] + 1])

                if (intervalId.isEmpty()) {
                    throw NoIdException(
                        originalText = rawText,
                        column = balanceLookup[index] + 1,
                    )
                }

                intervals.add(
                    InlineInterval(
                        id = intervalId,
                        startsAt = builder.length,
                        length = intervalText.length,
                    ),
                )

                // Should always be going after interval is constructed,
                // as we use its length to determine where the interval starts.
                builder.append(intervalText)

                index = balanceLookup[balanceLookup[index] + 1] + 1
                continue
            }

            builder.append(rawText[index])
            index += 1
        }

        return ParsingResult(
            clearedText = builder.toString(),
            intervals = intervals,
        )
    }

    @AnyThread
    @CheckResult
    @Suppress("LoopWithTooManyJumpStatements")
    private fun calculateBalanceLookup(rawText: String): Array<Int> {
        val balanceLookup = Array(rawText.length) { NO_CORRESPONDING_CLOSING_PARENTHESIS }

        val stack = ArrayDeque<Int>()
        for (index in rawText.indices) {
            val symbol = rawText[index]

            if (symbol == TOKEN_ROUND_OPEN_PARENTHESIS ||
                symbol == TOKEN_SQUARE_OPEN_PARENTHESIS
            ) {
                stack.addLast(index)
                continue
            }

            // Square parenthesis has the highest weight and can push out all other parenthesis.
            if (symbol == TOKEN_SQUARE_CLOSE_PARENTHESIS) {
                while (stack.isNotEmpty() && rawText[stack.last()] != TOKEN_SQUARE_OPEN_PARENTHESIS) {
                    stack.removeLast()
                }

                if (stack.isNotEmpty()) {
                    balanceLookup[stack.removeLast()] = index
                }

                continue
            }

            // Round parenthesis has highly restricted syntax, so
            // it should not have any nested parenthesis within it.
            if (symbol == TOKEN_ROUND_CLOSE_PARENTHESIS &&
                stack.isNotEmpty() &&
                rawText[stack.last()] == TOKEN_ROUND_OPEN_PARENTHESIS
            ) {
                balanceLookup[stack.removeLast()] = index
            }
        }

        return balanceLookup
    }
}
