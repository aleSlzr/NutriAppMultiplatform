package org.alia.nutrisport

import androidx.compose.ui.window.ComposeUIViewController
import org.alia.nutrisport.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initializeKoin()
    }
) {
    App()
}