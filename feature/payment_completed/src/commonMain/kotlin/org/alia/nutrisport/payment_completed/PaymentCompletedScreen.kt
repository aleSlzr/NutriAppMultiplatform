package org.alia.nutrisport.payment_completed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.component.InfoCard
import org.alia.nutrisport.shared.component.PrimaryButton

@Composable
fun PaymentCompletedScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    isSuccess: Boolean?,
    error: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .systemBarsPadding()
            .padding(all = 24.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            InfoCard(
                title = if (isSuccess != null) "Success" else "Oops!",
                subtitle = if (isSuccess != null) "Your purchase is on the way" else error ?: "Unknown error.",
                image = if (isSuccess != null) Resources.Image.Success else Resources.Image.Cat,
            )
        }
        PrimaryButton(
            text = "Go back",
            icon = Resources.Icon.RightArrow,
            onClick = navigateBack,
        )
    }
}