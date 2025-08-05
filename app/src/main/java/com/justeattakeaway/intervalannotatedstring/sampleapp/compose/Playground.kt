package com.justeattakeaway.intervalannotatedstring.sampleapp.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justeattakeaway.intervalannotatedstring.InlineIntervalSyntaxParser
import com.justeattakeaway.intervalannotatedstring.IntervalAnnotatedString
import com.justeattakeaway.intervalannotatedstring.asAnnotatedString
import com.justeattakeaway.intervalannotatedstring.sampleapp.R
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme.SampleAppTheme
import kotlin.random.Random

@Composable
fun Playground(
    modifier: Modifier = Modifier,
) {
    var rawText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
    ) {
        val annotatedStringResult = buildAnnotatedStringPreview(
            text = rawText
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (annotatedStringResult.isFailure) Color.Red else Color.Unspecified,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp),
        ) {
            if (annotatedStringResult.isSuccess) {
                val annotatedString = annotatedStringResult.getOrThrow()

                if (annotatedString.isNotBlank()) {
                    Text(
                        text = annotatedString,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )
                }
            } else {
                Text(
                    text = annotatedStringResult.exceptionOrNull()?.message ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )
            }
        }

        OutlinedTextField(
            value = rawText,
            label = {
                Text(
                    text = stringResource(R.string.main_screen_playground_input_field)
                )
            },
            onValueChange = { rawText = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 96.dp)
                .padding(8.dp),
        )
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
