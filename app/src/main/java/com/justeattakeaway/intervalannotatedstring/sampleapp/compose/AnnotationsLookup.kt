package com.justeattakeaway.intervalannotatedstring.sampleapp.compose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.justeattakeaway.intervalannotatedstring.sampleapp.SampleAppApplication
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme.Colors
import com.justeattakeaway.intervalannotatedstring.sampleapp.utils.openCamera
import com.justeattakeaway.intervalannotatedstring.sampleapp.utils.openEmail
import com.justeattakeaway.intervalannotatedstring.sampleapp.utils.openWebpage
import com.justeattakeaway.intervalannotatedstring.sampleapp.utils.shareText

internal val annotationsLookup = mapOf(
    "bold" to SpanStyle(fontWeight = FontWeight.Bold),
    "italic" to SpanStyle(fontStyle = FontStyle.Italic),
    "underline" to SpanStyle(textDecoration = TextDecoration.Underline),
    "strikethrough" to SpanStyle(textDecoration = TextDecoration.LineThrough),
    "color-red" to SpanStyle(color = Color.Red),
    "color-green" to SpanStyle(color = Color.Green),
    "color-blue" to SpanStyle(color = Color.Blue),
    "color-jet" to SpanStyle(color = Colors.jetBrand, fontWeight = FontWeight.Bold),
    "color-manila" to SpanStyle(color = Color(0xFFE7C9A9)),
    "highlight-yellow" to SpanStyle(background = Color.Yellow),
    "highlight-magenta" to SpanStyle(background = Color.Magenta),
    "highlight-red" to SpanStyle(background = Color.Red),
    "highlight-green" to SpanStyle(background = Color.Green),
    "align-center" to ParagraphStyle(textAlign = TextAlign.Center),
    "link-techblog" to LinkAnnotation.Clickable(
        tag = "techblog",
        styles = TextLinkStyles(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
            ),
        ),
        linkInteractionListener = {
            openWebpage(
                context = SampleAppApplication.applicationContext,
                url = "https://medium.com/justeattakeaway-tech",
            )
        }
    ),
    "link-interval-annotated-string-article" to LinkAnnotation.Clickable(
        tag = "interval-annotated-string-article",
        styles = TextLinkStyles(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
            ),
        ),
        linkInteractionListener = {
            openWebpage(
                context = SampleAppApplication.applicationContext,
                url = "https://medium.com/justeattakeaway-tech/do-androids-dream-of-inlining-links-ce852efc7f87",
            )
        }
    ),
    "link-email" to LinkAnnotation.Clickable(
        tag = "email",
        styles = TextLinkStyles(
            style = SpanStyle(
                color = Color.Black,
                textDecoration = TextDecoration.Underline,
            ),
        ),
        linkInteractionListener = {
            openEmail(
                context = SampleAppApplication.applicationContext,
                subject = "Check out Interval Annotated String library",
            )
        }
    ),
    "link-camera" to LinkAnnotation.Clickable(
        tag = "camera",
        styles = TextLinkStyles(
            style = SpanStyle(
                color = Color.Black,
                textDecoration = TextDecoration.Underline,
            ),
        ),
        linkInteractionListener = {
            openCamera(
                context = SampleAppApplication.applicationContext,
            )
        }
    ),
    "link-share" to LinkAnnotation.Clickable(
        tag = "share",
        styles = TextLinkStyles(
            style = SpanStyle(
                background = Color.Yellow,
            ),
        ),
        linkInteractionListener = {
            shareText(
                context = SampleAppApplication.applicationContext,
                text = "Check out Interval Annotated String: https://github.com/justeattakeaway/IntervalAnnotatedString by Just Eat Takeaway",
            )
        }
    ),
)
