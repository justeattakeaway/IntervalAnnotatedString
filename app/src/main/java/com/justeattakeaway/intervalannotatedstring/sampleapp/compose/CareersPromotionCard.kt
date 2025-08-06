package com.justeattakeaway.intervalannotatedstring.sampleapp.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justeattakeaway.intervalannotatedstring.asAnnotatedString
import com.justeattakeaway.intervalannotatedstring.intervalAnnotatedString
import com.justeattakeaway.intervalannotatedstring.sampleapp.R
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme.SampleAppTheme
import com.justeattakeaway.intervalannotatedstring.sampleapp.utils.openWebpage

private const val URL_ANNOTATION_ID = "1"
private const val URL_CAREERS_WEBSITE = "https://bit.ly/TechBlogFooter"

@Composable
fun CareersPromotionCard(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp, 10.dp),
        ) {
            Icon(
                Icons.Rounded.Lightbulb,
                contentDescription = null,
            )
            Spacer(
                modifier = Modifier.width(16.dp),
            )
            Text(
                text = intervalAnnotatedString(R.string.careers_promotion_card_text)
                    .asAnnotatedString { id, startsAt, length ->
                        val endsAt = startsAt + length

                        if (id == URL_ANNOTATION_ID) {
                            addLink(
                                clickable = LinkAnnotation.Clickable(
                                    tag = "careers-website",
                                    styles = TextLinkStyles(
                                        style = SpanStyle(
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontWeight = FontWeight.Bold,
                                            textDecoration = TextDecoration.Underline,
                                        )
                                    ),
                                    linkInteractionListener = { openWebpage(context, URL_CAREERS_WEBSITE) }
                                ),
                                start = startsAt,
                                end = endsAt,
                            )
                        }
                    },
            )
        }
    }
}

@Preview
@Composable
fun CareersPromotionCardPreview() {
    SampleAppTheme {
        CareersPromotionCard(
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
