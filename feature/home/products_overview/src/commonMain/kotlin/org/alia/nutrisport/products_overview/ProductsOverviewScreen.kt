package org.alia.nutrisport.products_overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.alia.nutrisport.shared.Alpha
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.TextPrimary
import org.alia.nutrisport.shared.component.InfoCard
import org.alia.nutrisport.shared.component.LoadingCard
import org.alia.nutrisport.shared.component.ProductCard
import org.alia.nutrisport.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductsOverviewScreen() {
    val viewModel = koinViewModel<ProductsOverviewViewModel>()
    val products = viewModel.products.collectAsState()
    
    products.value.DisplayResult(
        onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
        onError = { message ->
            InfoCard(
                image = Resources.Image.Cat,
                title = "Oops!",
                subtitle = message,
            )
        },
        onSuccess = { productList ->
            AnimatedContent(
                targetState = productList,
            ) { products ->
                if (products.isNotEmpty()) {
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(Alpha.HALF),
                            text = "Discounted Products",
                            fontSize = FontSize.EXTRA_REGULAR,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(
                                items = products.sortedBy { it.createdAt }.take(3),
                                key = { it.id }
                            ) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = {  }
                                )
                            }
                        }
                    }
                } else {
                    InfoCard(
                        image = Resources.Image.Cat,
                        title = "Nothing here.",
                        subtitle = "Empty product list.",
                    )
                }
            }
        },
    )
}