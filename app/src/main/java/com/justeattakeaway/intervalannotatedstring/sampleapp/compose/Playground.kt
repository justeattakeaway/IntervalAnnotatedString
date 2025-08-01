package com.justeattakeaway.intervalannotatedstring.sampleapp.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justeattakeaway.intervalannotatedstring.InlineIntervalSyntaxParser
import com.justeattakeaway.intervalannotatedstring.IntervalAnnotatedString
import com.justeattakeaway.intervalannotatedstring.asAnnotatedString
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme.SampleAppTheme
import kotlin.random.Random

@Composable
fun Playground(
    modifier: Modifier = Modifier,
) {
    var rawText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
    ) {
        TextField(
            value = rawText,
            onValueChange = { rawText = it },
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 96.dp)
                .padding(8.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
        )

        val annotatedStringResult = buildAnnotatedStringPreview(
            text = rawText
        )

        if (annotatedStringResult.isSuccess) {
            Text(
                text = annotatedStringResult.getOrThrow(),
            )
        } else {
            Text(
                text = annotatedStringResult.exceptionOrNull()?.message ?: "",
                modifier = Modifier
                    .background(Color.Red)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun buildAnnotatedStringPreview(
    text: String,
): Result<AnnotatedString> {
    val coloursLookup = remember { mutableStateMapOf<String, Color>() }

    return try {
        val annotatedString = IntervalAnnotatedString(text)
            .asAnnotatedString { id, startsAt, length ->
                val endsAt = startsAt + length

                if (!coloursLookup.containsKey(id)) {
                    coloursLookup[id] = Color(
                        Random.nextInt(256),
                        Random.nextInt(256),
                        Random.nextInt(256),
                    )
                }

                addStyle(
                    style = SpanStyle(background = coloursLookup.getValue(id)),
                    start = startsAt,
                    end = endsAt,
                )
            }

        Result.success(annotatedString)
    } catch (exception: InlineIntervalSyntaxParser.NoIdException) {
        Result.failure(exception)
    } catch (exception: InlineIntervalSyntaxParser.EmptyInlineTextException) {
        Result.failure(exception)
    }
}

@Preview
@Composable
fun PlaygroundPreview() {
    SampleAppTheme {
        Playground()
    }
}
