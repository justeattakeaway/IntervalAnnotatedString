package com.github.justeattakeaway.intervalannotatedstring

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class InlineIntervalSyntaxParserTest {
    @Test(expected = InlineIntervalSyntaxParser.EmptyInlineTextException::class)
    fun `TEST text with empty inline string throws exception`() {
        InlineIntervalSyntaxParser.parseMetadata(TEST_EXAMPLE_EMPTY_INLINE_STRING)
    }

    @Test(expected = InlineIntervalSyntaxParser.NoIdException::class)
    fun `TEST text with empty interval id throws exception`() {
        InlineIntervalSyntaxParser.parseMetadata(TEST_EXAMPLE_EMPTY_INTERVAL_ID)
    }

    @Test
    @Parameters(method = "parametersForSyntaxClearing")
    fun `TEST string is cleared correctly`(
        originalText: String,
        expectedClearedText: String,
    ) {
        val parsingResult = InlineIntervalSyntaxParser.parseMetadata(originalText)
        assertEquals(expectedClearedText, parsingResult.clearedText)
    }

    @Suppress("detekt:LongMethod")
    fun parametersForSyntaxClearing() =
        arrayOf(
            // No inline parameters.
            arrayOf(
                "",
                "",
            ),
            arrayOf(
                "Hello world",
                "Hello world",
            ),
            arrayOf(
                "No inline markdown here.",
                "No inline markdown here.",
            ),
            // Uncompleted cases.
            arrayOf(
                "Text with no ID specified: [no ID].",
                "Text with no ID specified: [no ID].",
            ),
            arrayOf(
                "Malformed [link without closing parenthesis](123",
                "Malformed [link without closing parenthesis](123",
            ),
            arrayOf(
                "Malformed id [link]((abc)",
                "Malformed id [link]((abc)",
            ),
            arrayOf(
                "Hello world with [a link](1) and another [ [link](2)",
                "Hello world with a link and another [ link",
            ),
            arrayOf(
                "Nested [link [hello [world] [ something(abc) ](something)",
                "Nested [link [hello [world]  something(abc) ",
            ),
            // Regular string.
            arrayOf(
                "This is [some text](abc) with an ID.",
                "This is some text with an ID.",
            ),
            arrayOf(
                "[Start](123) of the string.",
                "Start of the string.",
            ),
            arrayOf(
                "End of the string [here](xyz).",
                "End of the string here.",
            ),
            arrayOf(
                "Multiple [inline](a) [mark](b)downs [in](c) a [row](d).",
                "Multiple inline markdowns in a row.",
            ),
            arrayOf(
                "What about a link with [spaces in the text](my id with spaces)?",
                "What about a link with spaces in the text?",
            ),
            arrayOf(
                "What if the brackets are also [sequential]? Like [this](nested_id)?",
                "What if the brackets are also [sequential]? Like this?",
            ),
            // Balanced nested examples.
            arrayOf(
                "What about a link with [nested parenthesis [in the] text](my id with spaces)?",
                "What about a link with nested parenthesis [in the] text?",
            ),
            arrayOf(
                "This has [text with [inner] square brackets](id).",
                "This has text with [inner] square brackets.",
            ),
            arrayOf(
                "A more complex example: [outer [middle [inner] example] text](complex_id).",
                "A more complex example: outer [middle [inner] example] text.",
            ),
            arrayOf(
                "A more complex example: [outer [middle [inner] example] text and some [extra text]](complex_id).",
                "A more complex example: outer [middle [inner] example] text and some [extra text].",
            ),
            arrayOf(
                "This has [text with (inner) round brackets](id).",
                "This has text with (inner) round brackets.",
            ),
            arrayOf(
                "Another example: [outer (middle (inner) example) text](complex_id_round).",
                "Another example: outer (middle (inner) example) text.",
            ),
            arrayOf(
                "Multiple [instances (of (nested) brackets) here](nested_multiple_round).",
                "Multiple instances (of (nested) brackets) here.",
            ),
            arrayOf(
                "A link with [[parentheses within the text (like this one)]](with_parentheses_id).",
                "A link with [parentheses within the text (like this one)].",
            ),
            arrayOf(
                "text with [a link with [an inner link](2)](1)",
                "text with a link with [an inner link](2)",
            ),
            // Unbalanced nested examples.
            arrayOf(
                "This example has [text with [unbalanced nested brackets](id).",
                "This example has [text with unbalanced nested brackets.",
            ),
            arrayOf(
                "This example has [text with unbalanced nested] brackets](id).",
                "This example has [text with unbalanced nested] brackets](id).",
            ),
            // Misc.
            arrayOf(
                "This has text with (round braces)[square braces].",
                "This has text with (round braces)[square braces].",
            ),
        )

    @Test
    @Parameters(method = "parametersForInlineIntervalsTest")
    fun `TEST intervals are identified correctly`(
        originalText: String,
        expectedIntervals: List<InlineIntervalSyntaxParser.InlineInterval>,
    ) {
        val parsingResult = InlineIntervalSyntaxParser.parseMetadata(originalText)
        assertEquals(expectedIntervals, parsingResult.intervals)
    }

    @Suppress("LongMethod")
    fun parametersForInlineIntervalsTest() =
        arrayOf(
            arrayOf(
                "",
                emptyList<InlineIntervalSyntaxParser.InlineInterval>(),
            ),
            arrayOf(
                "Hello world",
                emptyList<InlineIntervalSyntaxParser.InlineInterval>(),
            ),
            arrayOf(
                "[Start](123) of the string.",
                listOf(
                    InlineIntervalSyntaxParser.InlineInterval(
                        id = "123",
                        startsAt = 0,
                        length = 5,
                    ),
                ),
            ),
            arrayOf(
                "This has [text with [inner] square brackets](id).",
                listOf(
                    InlineIntervalSyntaxParser.InlineInterval(
                        id = "id",
                        startsAt = 9,
                        length = 33,
                    ),
                ),
            ),
            arrayOf(
                "Multiple [instances (of (nested) brackets) here](nested_multiple_round).",
                listOf(
                    InlineIntervalSyntaxParser.InlineInterval(
                        id = "nested_multiple_round",
                        startsAt = 9,
                        length = 37,
                    ),
                ),
            ),
            arrayOf(
                "A more complex example: [outer [middle [inner] example] text](complex_id).",
                listOf(
                    InlineIntervalSyntaxParser.InlineInterval(
                        id = "complex_id",
                        startsAt = 24,
                        length = 35,
                    ),
                ),
            ),
            arrayOf(
                "Check out our docs: [doc 1](1), [doc 2](2), [doc 3](3).",
                listOf(
                    InlineIntervalSyntaxParser.InlineInterval(
                        id = "1",
                        startsAt = 20,
                        length = 5,
                    ),
                    InlineIntervalSyntaxParser.InlineInterval(
                        id = "2",
                        startsAt = 27,
                        length = 5,
                    ),
                    InlineIntervalSyntaxParser.InlineInterval(
                        id = "3",
                        startsAt = 34,
                        length = 5,
                    ),
                ),
            ),
            arrayOf(
                "Sophisticated [really complex!] text example: [doc 1](1), [doc [with nested] 2](2), [doc (oui) 3](3).",
                listOf(
                    InlineIntervalSyntaxParser.InlineInterval(
                        id = "1",
                        startsAt = 46,
                        length = 5,
                    ),
                    InlineIntervalSyntaxParser.InlineInterval(
                        id = "2",
                        startsAt = 53,
                        length = 19,
                    ),
                    InlineIntervalSyntaxParser.InlineInterval(
                        id = "3",
                        startsAt = 74,
                        length = 11,
                    ),
                ),
            ),
        )

    private companion object {
        const val TEST_EXAMPLE_EMPTY_INLINE_STRING = "Text with empty inline string: [](here it is!)"
        const val TEST_EXAMPLE_EMPTY_INTERVAL_ID = "Text with empty inline string: [yo this is empty ->]()"
    }
}
