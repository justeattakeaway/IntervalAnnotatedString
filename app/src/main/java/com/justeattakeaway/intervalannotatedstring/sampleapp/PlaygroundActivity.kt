package com.justeattakeaway.intervalannotatedstring.sampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.Playground
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme.SampleAppTheme

class PlaygroundActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Playground(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
