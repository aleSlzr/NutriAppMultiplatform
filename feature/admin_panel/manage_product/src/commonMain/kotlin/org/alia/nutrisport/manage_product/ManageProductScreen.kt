package org.alia.nutrisport.manage_product

import ContentWithMessageBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.alia.nutrisport.shared.BebasNeueFont
import org.alia.nutrisport.shared.BorderIdle
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.IconPrimary
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.SurfaceLighter
import org.alia.nutrisport.shared.TextPrimary
import org.alia.nutrisport.shared.component.AlertTextField
import org.alia.nutrisport.shared.component.CustomTextField
import org.alia.nutrisport.shared.component.PrimaryButton
import org.alia.nutrisport.shared.component.dialog.CategoriesDialog
import org.alia.nutrisport.shared.domain.ProductCategory
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreen(
    id: String?,
    navigateBack: () -> Unit,
) {
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<ManageProductViewModel>()
    val screenState = viewModel.screenState
    val isFormValid = viewModel.isFormValid
    var showCategoriesDialog by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = showCategoriesDialog,
    ) {
        CategoriesDialog(
            category = screenState.category,
            onDismiss = { showCategoriesDialog = false },
            onConfirmClick = { selectedCategory ->
                viewModel.updateCategory(selectedCategory)
                showCategoriesDialog = false
            }
        )
    }

    Scaffold(
        contentColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (id == null) "New Product" else "Edit Product",
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
    ) { padding ->
        ContentWithMessageBar(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                ),
            messageBarState = messageBarState,
            errorMaxLines = 2,
            contentBackgroundColor = Surface,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(
                        bottom = 24.dp,
                        top = 12.dp
                    )
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(size = 12.dp))
                            .border(
                                width = 1.dp,
                                color = BorderIdle,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(SurfaceLighter),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Resources.Icon.Plus,
                            contentDescription = "Plus icon",
                            tint = IconPrimary,
                        )
                    }
                    CustomTextField(
                        value = screenState.title,
                        onValueChange = viewModel::updateTitle,
                        placeholder = "Title",
                    )
                    CustomTextField(
                        modifier = Modifier.height(168.dp),
                        value = screenState.description,
                        onValueChange = viewModel::updateDescription,
                        placeholder = "Description",
                        expanded = true,
                    )
                    AlertTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = screenState.category.title,
                        onClick = { showCategoriesDialog = true },
                    )
                    CustomTextField(
                        value = "${screenState.weight ?: ""}",
                        onValueChange = {
                            viewModel.updateWeight(it.toIntOrNull() ?: 0)
                        },
                        placeholder = "Weight (Optional)",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        )
                    )
                    CustomTextField(
                        value = screenState.flavors ?: "",
                        onValueChange = viewModel::updateFlavors,
                        placeholder = "Flavors (Optional)",
                    )
                    CustomTextField(
                        value = "${screenState.price}",
                        onValueChange = { value ->
                            if (value.isEmpty() || value.toDoubleOrNull() != null) {
                                viewModel.updatePrice(value.toDoubleOrNull() ?: 0.0)
                            }
                        },
                        placeholder = "Price",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                PrimaryButton(
                    text = if (id == null) "Add new product" else "Update",
                    icon = if (id == null) Resources.Icon.Plus else Resources.Icon.CheckMark,
                    enabled = isFormValid,
                    onClick = {
                        viewModel.createNewProduct(
                            onSuccess = { messageBarState.addSuccess("Product successfully added!") },
                            onError = { message -> messageBarState.addError(message) }
                        )
                    },
                )
            }
        }
    }
}