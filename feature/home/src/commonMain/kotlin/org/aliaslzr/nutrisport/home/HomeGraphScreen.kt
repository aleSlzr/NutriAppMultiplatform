package org.aliaslzr.nutrisport.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.alia.nutrisport.shared.Surface
import org.aliaslzr.nutrisport.home.component.BottomBar

@Composable
fun HomeGraphScreen() {
    Scaffold(
        containerColor = Surface,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                )
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier
                .padding(all = 12.dp)
            ) {
                BottomBar(
                    selected = false,
                    onSelect = { destination ->

                    }
                )
            }
        }
    }
}