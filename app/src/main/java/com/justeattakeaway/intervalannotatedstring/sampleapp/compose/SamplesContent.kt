package com.justeattakeaway.intervalannotatedstring.sampleapp.compose

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justeattakeaway.intervalannotatedstring.asAnnotatedString
import com.justeattakeaway.intervalannotatedstring.intervalAnnotatedString
import com.justeattakeaway.intervalannotatedstring.sampleapp.R
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme.SampleAppTheme

@Composable
internal fun SamplesContent(
    modifier: Modifier = Modifier,
    items: Array<String> = stringArrayResource(R.array.main_screen_samples_content_examples),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(12.dp, 8.dp),
    ) {
        for (item in items) {
            SampleContentItem(
                rawText = item,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SampleContentItem(
    rawText: String,
    modifier: Modifier = Modifier,
) {
    var holdingShowCodeButton by remember { mutableStateOf(false) }

    SampleContentItemStateless(
        rawText = rawText,
        shouldShowRawString = holdingShowCodeButton,
        onShowRawString = { holdingShowCodeButton = true },
        onShowRenderingString = { holdingShowCodeButton = false },
        modifier = modifier,
    )
}

@Composable
private fun SampleContentItemStateless(
    rawText: String,
    shouldShowRawString: Boolean,
    onShowRawString: () -> Unit,
    onShowRenderingString: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .weight(7f)
                    .height(IntrinsicSize.Max)
                    .padding(16.dp, 12.dp),
            ) {
                Text(
                    text = rawText,
                    modifier = Modifier
                        // We need to use alpha here and in the case below,
                        // to reserve heights for both of them to avoid view
                        // jumping in the list.
                        .alpha(if (shouldShowRawString) 1f else 0f),
                )

                SampleContentItemRenderingStringViewer(
                    rawText = rawText,
                    modifier = Modifier
                        .alpha(if (shouldShowRawString) 0f else 1f),
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .indication(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = false),
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(onPress = { offset ->
                            onShowRawString()

                            val press = PressInteraction.Press(offset)
                            interactionSource.emit(press)

                            tryAwaitRelease()

                            interactionSource.emit(PressInteraction.Release(press))
                            onShowRenderingString()
                        })
                    }
                    .weight(3f)
                    .padding(8.dp),
            ) {
                Icon(
                    Icons.Rounded.Code,
                    contentDescription = null,
                )
                Text(
                    text = stringResource(R.string.samples_content_show_code_button),
                    maxLines = 1,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun SampleContentItemRenderingStringViewer(
    rawText: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = intervalAnnotatedString(rawText)
            .asAnnotatedString { id, startsAt, length ->
                val endsAt = startsAt + length

                val annotation = annotationsLookup[id]
                when (annotation) {
                    is SpanStyle ->
                        addStyle(
                            style = annotation,
                            start = startsAt,
                            end = endsAt,
                        )
                    is ParagraphStyle ->
                        addStyle(
                            style = annotation,
                            start = startsAt,
                            end = endsAt,
                        )
                    is LinkAnnotation.Clickable ->
                        addLink(
                            clickable = annotation,
                            start = startsAt,
                            end = endsAt,
                        )
                    else -> throw IllegalStateException("Unrecognised id: $id")
                }
            },
        modifier = modifier,
    )
}

@Preview
@Composable
internal fun SamplesContentPreview() {
    SampleAppTheme {
        SamplesContent(
            items = arrayOf(
                "[Interval annotated string](highlight-yellow)",
                "Sample [text](bold)",
                "Another [sample text](color-jet)",
                "[More text](strikethrough)",
            )
        )
    }
}
