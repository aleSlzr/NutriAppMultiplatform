package org.aliaslzr.nutrisport.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.alia.nutrisport.shared.BebasNeueFont
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.TextPrimary
import org.alia.nutrisport.shared.TextSecondary
import org.aliaslzr.nutrisport.home.domain.DrawerItem

@Composable
fun CustomDrawer(
    onProfileClick: () -> Unit,
    onContactUs: () -> Unit,
    onSignOut: () -> Unit,
    onAdminPanel: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.6f)
            .padding(horizontal = 12.dp),
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "NUTRISPORT",
            textAlign = TextAlign.Center,
            color = TextSecondary,
            fontFamily = BebasNeueFont(),
            fontSize = FontSize.EXTRA_LARGE,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Healthy Lifestyle",
            textAlign = TextAlign.Center,
            color = TextPrimary,
            fontSize = FontSize.REGULAR,
        )
        Spacer(modifier = Modifier.height(50.dp))
        DrawerItem.entries.take(5).forEach { item ->
            DrawerItemCard(
                drawerItem = item,
                onClick = {
                    when(item) {
                        DrawerItem.Profile -> onProfileClick()
                        DrawerItem.Contact -> onContactUs()
                        DrawerItem.SignOut -> onSignOut()
                        else -> {}
                    }
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        DrawerItemCard(
            drawerItem = DrawerItem.Admin,
            onClick = {
                onAdminPanel()
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}