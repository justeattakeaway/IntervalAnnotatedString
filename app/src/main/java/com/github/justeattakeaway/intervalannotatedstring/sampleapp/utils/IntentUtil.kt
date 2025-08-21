package com.github.justeattakeaway.intervalannotatedstring.sampleapp.utils

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.core.net.toUri

internal fun openWebpage(
    context: Context?,
    url: String,
) {
    val openBrowserIntent = Intent(
        Intent.ACTION_VIEW,
        url.toUri(),
    )
    context?.startApplicationIntent(openBrowserIntent)
}

internal fun openEmail(
    context: Context?,
    subject: String,
) {
    val openEmailIntent = Intent(Intent.ACTION_VIEW)
    val data = ("mailto:?subject=$subject").toUri()
    openEmailIntent.data = data
    context?.startApplicationIntent(openEmailIntent)
}

internal fun openCamera(
    context: Context?,
) {
    val openCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    context?.startApplicationIntent(openCamera)
}

internal fun shareText(
    context: Context?,
    text: String,
) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context?.startApplicationIntent(shareIntent)
}

private fun Context?.startApplicationIntent(
    intent: Intent,
) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this?.startActivity(intent)
}
