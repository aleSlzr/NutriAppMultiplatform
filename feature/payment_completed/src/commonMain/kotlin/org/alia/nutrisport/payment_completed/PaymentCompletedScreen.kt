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
import org.alia.nutrisport.shared.component.LoadingCard
import org.alia.nutrisport.shared.component.PrimaryButton
import org.alia.nutrisport.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PaymentCompletedScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<PaymentCompletedViewModel>()
    val screenState = viewModel.screenState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .systemBarsPadding()
            .padding(all = 24.dp)
    ) {
        screenState.DisplayResult(
            onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
            onError = { message ->
                Column {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        InfoCard(
                            title = "Oops!",
                            subtitle = message,
                            image = Resources.Image.Cat,
                        )
                    }
                    PrimaryButton(
                        text = "Go back",
                        icon = Resources.Icon.RightArrow,
                        onClick = navigateBack,
                    )
                }
            },
            onSuccess = {
                Column {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        InfoCard(
                            title = "Success",
                            subtitle = "Your purchase is on the way",
                            image = Resources.Image.Success,
                        )
                    }
                    PrimaryButton(
                        text = "Go back",
                        icon = Resources.Icon.RightArrow,
                        onClick = navigateBack,
                    )
                }
            },
        )
    }
}