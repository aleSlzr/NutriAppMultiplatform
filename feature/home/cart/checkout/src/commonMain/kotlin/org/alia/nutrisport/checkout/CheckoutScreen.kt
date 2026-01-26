package org.alia.nutrisport.checkout

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.alia.nutrisport.shared.BebasNeueFont
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.IconPrimary
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.TextPrimary
import org.alia.nutrisport.shared.component.PrimaryButton
import org.alia.nutrisport.shared.component.ProfileForm
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    totalAmount: Double,
    navigateBack: () -> Unit,
    navigateToPaymentCompleted: (Boolean?, String?) -> Unit,
) {
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<CheckoutViewModel>()
    val screenState = viewModel.screenState
    val isFormValid = viewModel.isFormValid
    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout",
                        fontFamily = BebasNeueFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary,
                    )
                },
                actions = {
                    Text(
                        text = "$${totalAmount}",
                        fontSize = FontSize.EXTRA_MEDIUM,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Resources.Icon.BackArrow,
                            contentDescription = "Back arrow icon",
                            tint = IconPrimary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary,
                )
            )
        }
    ) { paddingValues ->
        ContentWithMessageBar(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
            contentBackgroundColor = Surface,
            messageBarState = messageBarState,
            errorMaxLines = 2,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 12.dp,
                        end = 24.dp,
                    )
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                ProfileForm(
                    modifier = Modifier.weight(1f),
                    firstName = screenState.firstName,
                    onFirstNameChange = viewModel::updateFirstName,
                    lastName = screenState.lastName,
                    onLastNameChange = viewModel::updateLasName,
                    email = screenState.email,
                    country = screenState.country,
                    onCountrySelect = viewModel::updateCountry,
                    city = screenState.city.orEmpty(),
                    onCityChange = viewModel::updateCity,
                    postalCode = screenState.postalCode,
                    onPostalCodeChange = viewModel::updatePostalCode,
                    address = screenState.address.orEmpty(),
                    onAddressChange = viewModel::updateAddress,
                    phoneNumber = screenState.phoneNumber?.number,
                    onPhoneNumberChange = viewModel::updatePhoneNumber,
                )
                PrimaryButton(
                    text = "Pay on Delivery",
                    icon = Resources.Icon.ShoppingCart,
                    enabled = isFormValid,
                    secondary = true,
                    onClick = {
                        viewModel.payOnDelivery(
                            onSuccess = {
                                navigateToPaymentCompleted(true, null)
                            },
                            onError = { message ->
                                navigateToPaymentCompleted(null, message)
                            }
                        )
                    },
                )
            }
        }
    }
}