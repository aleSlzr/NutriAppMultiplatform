package org.alia.nutrisport.profile

import ContentWithMessageBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.alia.nutrisport.shared.BebasNeueFont
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.IconPrimary
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.TextPrimary
import org.alia.nutrisport.shared.component.InfoCard
import org.alia.nutrisport.shared.component.LoadingCard
import org.alia.nutrisport.shared.component.PrimaryButton
import org.alia.nutrisport.shared.component.ProfileForm
import org.alia.nutrisport.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val screenState = viewModel.screenState
    val screenReady = viewModel.screenReady
    val isFormValid = viewModel.isFormValid
    val messageBarState = rememberMessageBarState()

    Scaffold(
        contentColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Profile",
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
                }
            )
        }
    ) { paddingValues ->
        ContentWithMessageBar(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
            messageBarState = messageBarState,
            errorMaxLines = 2,
            contentBackgroundColor = Surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(
                        top = 12.dp,
                        bottom = 24.dp,
                    )
                    .imePadding(),
            ) {
                screenReady.DisplayResult(
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
                    onSuccess = {
                        Column(
                            modifier = Modifier.fillMaxSize()
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
                            Spacer(modifier = Modifier.height(12.dp))
                            PrimaryButton(
                                text = "Update",
                                icon = Resources.Icon.CheckMark,
                                enabled = isFormValid,
                                onClick = {
                                    viewModel.updateCustomer(
                                        onSuccess = {
                                            messageBarState.addSuccess("Successfully updated!")
                                        },
                                        onError = { message ->
                                            messageBarState.addError(message = message)
                                        }
                                    )
                                },
                            )
                        }
                    },
                )
            }
        }
    }
}