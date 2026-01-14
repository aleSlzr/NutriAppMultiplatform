package org.alia.nutrisport.admin_panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.alia.nutrisport.shared.BebasNeueFont
import org.alia.nutrisport.shared.ButtonPrimary
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.IconPrimary
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.TextPrimary
import org.alia.nutrisport.shared.component.InfoCard
import org.alia.nutrisport.shared.component.LoadingCard
import org.alia.nutrisport.shared.component.ProductCard
import org.alia.nutrisport.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    navigateBack: () -> Unit,
    navigateToManageProduct: (String?) -> Unit,
) {
    val viewModel = koinViewModel<AdminPanelViewModel>()
    val products = viewModel.products.collectAsStateWithLifecycle()

    Scaffold(
        contentColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Admin Panel",
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
                    IconButton(onClick = {}) {
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToManageProduct(null) },
                containerColor = ButtonPrimary,
                contentColor = IconPrimary,
                content = {
                    Icon(
                        imageVector = Resources.Icon.Plus,
                        contentDescription = "Add product icon",
                        tint = IconPrimary,
                    )
                }
            )
        }
    ) { innerPadding ->
        products.value.DisplayResult(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                ),
            onLoading = {
                LoadingCard(
                    modifier = Modifier.fillMaxSize()
                )
            },
            onError = { message ->
                InfoCard(
                    image = Resources.Image.Cat,
                    title = "Oops!",
                    subtitle = message,
                )
            },
            onSuccess = { productList ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = productList,
                        key = { it.id }
                    ) { productItem ->
                        ProductCard(
                            product = productItem,
                            onClick = { navigateToManageProduct(productItem.id) }
                        )
                    }
                }
            },
        )
    }
}