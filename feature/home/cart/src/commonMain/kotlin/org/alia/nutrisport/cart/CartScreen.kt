package org.alia.nutrisport.cart

import ContentWithMessageBar
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.alia.nutrisport.cart.component.ItemCart
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.component.InfoCard
import org.alia.nutrisport.shared.component.LoadingCard
import org.alia.nutrisport.shared.util.DisplayResult
import org.alia.nutrisport.shared.util.RequestState
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@Composable
fun CartScreen(
) {
    val viewModel = koinViewModel<CartViewModel>()
    val cartItemsWithProducts by viewModel.cartItemsWithProducts.collectAsState(RequestState.Loading)
    val messageBarState = rememberMessageBarState()

    ContentWithMessageBar(
        messageBarState = messageBarState,
        contentBackgroundColor = Surface,
        errorMaxLines = 2,
    ) {
        cartItemsWithProducts.DisplayResult(
            transitionSpec = fadeIn() togetherWith fadeOut(),
            onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
            onError = { message ->
                InfoCard(
                    image = Resources.Image.Cat,
                    title = "Oops!",
                    subtitle = message,
                )
            },
            onSuccess = { data ->
                if (data.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = data,
                            key = { data.hashCode().toString() }
                        ) { pair ->
                            ItemCart(
                                product = pair.second,
                                cartItem = pair.first,
                                onMinusClick = { quantity ->
                                    viewModel.updateCartItemQuantity(
                                        id = pair.first.id,
                                        quantity = quantity,
                                        onSuccess = {  },
                                        onError = { message -> messageBarState.addError(message)},
                                    )
                                },
                                onPlusClick = { quantity ->
                                    viewModel.updateCartItemQuantity(
                                        id = pair.first.id,
                                        quantity = quantity,
                                        onSuccess = {  },
                                        onError = { message -> messageBarState.addError(message)},
                                    )
                                },
                                onDeleteClick = {},
                            )
                        }
                    }
                } else {
                    InfoCard(
                        image = Resources.Image.Cart,
                        title = "Empty cart",
                        subtitle = "Check some of our products",
                    )
                }
            },
        )
    }
}