package org.alia.nutrisport.category_search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.alia.nutrisport.shared.BebasNeueFont
import org.alia.nutrisport.shared.BorderIdle
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.IconPrimary
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.TextPrimary
import org.alia.nutrisport.shared.component.InfoCard
import org.alia.nutrisport.shared.component.LoadingCard
import org.alia.nutrisport.shared.component.ProductCard
import org.alia.nutrisport.shared.domain.ProductCategory
import org.alia.nutrisport.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySearchScreen(
    category: ProductCategory,
    navigateBack: () -> Unit,
    navigateToDetails: (String) -> Unit,
) {
    val viewModel = koinViewModel<CategorySearchViewModel>()
    val productList by viewModel.products.collectAsState()

    Scaffold(
        containerColor = Surface,
        topBar = {
            AnimatedContent(
                targetState = false
            ) { visible ->
                if (visible) {
                    /*SearchBar(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        inputField = {
                            SearchBarDefaults.InputField(
                                modifier = Modifier.fillMaxWidth(),
                                query = searchQuery,
                                onQueryChange = viewModel::updateSearchQuery,
                                expanded = false,
                                onExpandedChange = {},
                                onSearch = {},
                                placeholder = {
                                    Text(
                                        text = "Search here",
                                        fontSize = FontSize.REGULAR,
                                        color = TextPrimary,
                                    )
                                },
                                trailingIcon = {
                                    IconButton(
                                        modifier = Modifier.size(14.dp),
                                        onClick = {
                                            if (searchQuery.isNotEmpty()) {
                                                viewModel.updateSearchQuery("")
                                            } else {
                                                searchBarVisible = false
                                            }
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Resources.Icon.Close,
                                            contentDescription = "Close icon",
                                            tint = IconPrimary,
                                        )
                                    }
                                },
                            )
                        },
                        colors = SearchBarColors(
                            containerColor = Surface,
                            dividerColor = BorderIdle,
                        ),
                        expanded = false,
                        onExpandedChange = {},
                        content = {}
                    )*/
                } else {
                    TopAppBar(
                        title = {
                            Text(
                                text = category.title,
                                fontFamily = BebasNeueFont(),
                                fontSize = FontSize.LARGE,
                                color = TextPrimary,
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Surface,
                            scrolledContainerColor = Surface,
                            navigationIconContentColor = IconPrimary,
                            titleContentColor = TextPrimary,
                            actionIconContentColor = IconPrimary,
                        ),
                        actions = {
                            IconButton(
                                onClick = {
                                    // searchBarVisible = true
                                }
                            ) {
                                Icon(
                                    imageVector = Resources.Icon.Search,
                                    contentDescription = "Search icon",
                                    tint = IconPrimary,
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = navigateBack) {
                                Icon(
                                    imageVector = Resources.Icon.BackArrow,
                                    contentDescription = "Back arrow icon",
                                    tint = IconPrimary,
                                )
                            }
                        },
                    )
                }
            }
        },
    ) { paddingValues ->
        productList.DisplayResult(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
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
            onSuccess = { categoryProducts ->
                AnimatedContent(
                    targetState = categoryProducts,
                ) { products ->
                    if (products.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(all = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = products,
                                key = { it.id }
                            ) { productItem ->
                                ProductCard(
                                    product = productItem,
                                    onClick = {
                                        navigateToDetails(productItem.id)
                                    }
                                )
                            }
                        }
                    } else {
                        InfoCard(
                            image = Resources.Image.Cat,
                            title = "Nothing here",
                            subtitle = "We couldn't find any product."
                        )
                    }
                }
            },
        )
    }
}