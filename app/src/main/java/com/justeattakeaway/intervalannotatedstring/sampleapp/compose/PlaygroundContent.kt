package com.justeattakeaway.intervalannotatedstring.sampleapp.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme.SampleAppTheme

@Composable
internal fun PlaygroundContent(
    modifier: Modifier = Modifier,
) {
    Playground(
        modifier = modifier
            .padding(12.dp, 8.dp),
    )
}

@Preview
@Composable
internal fun PlaygroundContentPreview() {
    SampleAppTheme {
        PlaygroundContent()
    }
}
