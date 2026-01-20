package org.alia.nutrisport.details

import ContentWithMessageBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.alia.nutrisport.shared.BebasNeueFont
import org.alia.nutrisport.shared.BorderIdle
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.IconPrimary
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.RobotoCondensedFont
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.SurfaceLighter
import org.alia.nutrisport.shared.TextPrimary
import org.alia.nutrisport.shared.TextSecondary
import org.alia.nutrisport.shared.component.FlavorChip
import org.alia.nutrisport.shared.component.InfoCard
import org.alia.nutrisport.shared.component.LoadingCard
import org.alia.nutrisport.shared.component.PrimaryButton
import org.alia.nutrisport.shared.component.QuantityCounter
import org.alia.nutrisport.shared.domain.ProductCategory
import org.alia.nutrisport.shared.domain.QuantityCounterSize
import org.alia.nutrisport.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
) {
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<DetailsViewModel>()
    val product by viewModel.product.collectAsState()
    val quantity = viewModel.quantity
    val selectedFlavor = viewModel.selectedFlavor

    Scaffold(
        contentColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Details",
                        fontFamily = BebasNeueFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary,
                ),
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Resources.Icon.BackArrow,
                            contentDescription = "Back arrow icon",
                            tint = IconPrimary,
                        )
                    }
                },
                actions = {
                    QuantityCounter(
                        size = QuantityCounterSize.Large,
                        value = quantity,
                        onMinusClick = viewModel::updateQuantity,
                        onPlusClick = viewModel::updateQuantity,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            )
        }
    ) { padding ->
        product.DisplayResult(
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            },
            onError = { message ->
                InfoCard(
                    image = Resources.Image.Cat,
                    title = "Oops!",
                    subtitle = message,
                )
            },
            onSuccess = { selectedProduct ->
                ContentWithMessageBar(
                    modifier = Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding(),
                        ),
                    contentBackgroundColor = Surface,
                    messageBarState = messageBarState,
                    errorMaxLines = 2,
                ) {
                    Column {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp)
                                .padding(top = 12.dp),
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = 1.dp,
                                        color = BorderIdle,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                model = ImageRequest
                                    .Builder(LocalPlatformContext.current)
                                    .data(selectedProduct.thumbnail)
                                    .crossfade(enable = true)
                                    .build(),
                                contentDescription = "Product thumbnail image",
                                contentScale = ContentScale.Fit,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                AnimatedContent(
                                    targetState = selectedProduct.category,

                                    ) { category ->
                                    if (ProductCategory.valueOf(category) == ProductCategory.Accessories) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    } else {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(14.dp),
                                                imageVector = Resources.Icon.Weight,
                                                contentDescription = "Weight icon",
                                                tint = IconPrimary,
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${selectedProduct.weight}g",
                                                fontSize = FontSize.REGULAR,
                                                color = TextPrimary,
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "$${selectedProduct.price}",
                                    fontSize = FontSize.MEDIUM,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = selectedProduct.title,
                                fontSize = FontSize.EXTRA_MEDIUM,
                                fontWeight = FontWeight.Medium,
                                fontFamily = RobotoCondensedFont(),
                                color = TextPrimary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = selectedProduct.description,
                                fontSize = FontSize.REGULAR,
                                lineHeight = FontSize.REGULAR * 1.3,
                                color = TextPrimary,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .background(
                                    if (selectedProduct.flavors?.isNotEmpty() == true) SurfaceLighter
                                    else Surface
                                )
                                .padding(all = 24.dp)
                        ) {
                            if (selectedProduct.flavors?.isNotEmpty() == true) {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    selectedProduct.flavors?.forEach { flavor ->
                                        FlavorChip(
                                            flavor = flavor,
                                            isSelected = selectedFlavor == flavor,
                                            onClick = {
                                                viewModel.updateFlavor(flavor)
                                            }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            PrimaryButton(
                                icon = Resources.Icon.ShoppingCart,
                                text = "Add to cart",
                                onClick = {
                                    viewModel.addItemToCart(
                                        onSuccess = { messageBarState.addSuccess("Product added to cart.") },
                                        onError = { message -> messageBarState.addError(message) }
                                    )
                                },
                                enabled = if (selectedProduct.flavors?.isNotEmpty() == true) selectedFlavor != null
                                else true,
                            )
                        }
                    }
                }
            },
        )
    }
}