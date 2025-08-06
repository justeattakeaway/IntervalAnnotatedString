package com.justeattakeaway.intervalannotatedstring.sampleapp.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Preview
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.justeattakeaway.intervalannotatedstring.sampleapp.R
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme.Colors
import com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme.SampleAppTheme
import com.justeattakeaway.intervalannotatedstring.sampleapp.utils.openWebpage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var currentRoute by remember { mutableStateOf(Routes.PLAYGROUND) }

    SampleAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.app_name)) },
                    actions = {
                        IconButton(onClick = {
                            openWebpage(
                                context = context,
                                url = "https://github.com/justeattakeaway/IntervalAnnotatedString"
                            )
                        }) { Icon(Icons.Rounded.Star, contentDescription = null) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        actionIconContentColor = Colors.jetBrand,
                    ),
                    modifier = Modifier
                        .shadow(
                            elevation = 8.dp,
                            shape = RectangleShape,
                            clip = false,
                        )
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.PLAYGROUND,
                        onClick = { currentRoute = Routes.PLAYGROUND },
                        icon = { Icon(painterResource(R.drawable.ic_playground_24px), contentDescription = null) },
                        label = { Text(stringResource(R.string.main_screen_navigation_preview)) },
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.SAMPLES,
                        onClick = { currentRoute = Routes.SAMPLES },
                        icon = { Icon(painterResource(R.drawable.ic_lab_panel_24px), contentDescription = null) },
                        label = { Text(stringResource(R.string.main_screen_navigation_examples)) },
                    )
                }
            },
            modifier = modifier.fillMaxSize(),
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                when (currentRoute) {
                    Routes.PLAYGROUND -> PlaygroundContent(
                        modifier =
                            Modifier
                                .fillMaxSize()
                    )

                    Routes.SAMPLES -> SamplesContent(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}