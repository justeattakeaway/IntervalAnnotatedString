package com.justeattakeaway.intervalannotatedstring.sampleapp.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme.SampleAppTheme

@Composable
internal fun PlaygroundContent(
    modifier: Modifier = Modifier,
) {
    Playground(
        modifier = modifier,
    )
}

@Preview
@Composable
internal fun PlaygroundContentPreview() {
    SampleAppTheme {
        PlaygroundContent()
    }
}
