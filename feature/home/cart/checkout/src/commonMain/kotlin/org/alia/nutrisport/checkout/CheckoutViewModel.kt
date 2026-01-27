package org.alia.nutrisport.checkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.alia.nutrisport.checkout.domain.Amount
import org.alia.nutrisport.checkout.domain.PayPalAPI
import org.alia.nutrisport.checkout.domain.ShippingAddress
import org.alia.nutrisport.data.domain.CustomerRepository
import org.alia.nutrisport.data.domain.OrderRepository
import org.alia.nutrisport.shared.domain.CartItem
import org.alia.nutrisport.shared.domain.Country
import org.alia.nutrisport.shared.domain.Customer
import org.alia.nutrisport.shared.domain.Order
import org.alia.nutrisport.shared.domain.PhoneNumber
import org.alia.nutrisport.shared.util.RequestState

data class CheckoutScreenState(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val city: String? = null,
    val postalCode: Int? = null,
    val address: String? = null,
    val country: Country = Country.Mexico,
    val phoneNumber: PhoneNumber? = null,
    val cart: List<CartItem> = emptyList(),
)

class CheckoutViewModel(
    private val customerRepository: CustomerRepository,
    private val orderRepository: OrderRepository,
    private val savedStateHandle: SavedStateHandle,
    private val payPalAPI: PayPalAPI,
) : ViewModel() {
    var screenReady: RequestState<Unit> by mutableStateOf(RequestState.Loading)
    var screenState: CheckoutScreenState by mutableStateOf(CheckoutScreenState())
        private set

    val isFormValid: Boolean
        get() = with(screenState) {
            firstName.length in 3..50 &&
                lastName.length in 3..50 &&
                city?.length in 3..50 &&
                postalCode != null || postalCode?.toString()?.length in 3..8 &&
                address?.length in 3..50 &&
                phoneNumber != null || phoneNumber?.number?.length in 5..30
        }

    init {
        viewModelScope.launch {
            payPalAPI.fetchAccessToken(
                onSuccess = { token ->

                },
                onError = { message ->

                }
            )
        }
        viewModelScope.launch {
            customerRepository.readCustomerFlow().collectLatest { data ->
                if (data.isSuccess()) {
                    val fetchedCustomer = data.getSuccessData()
                    screenState = CheckoutScreenState(
                        id = fetchedCustomer.id,
                        firstName = fetchedCustomer.firstName,
                        lastName = fetchedCustomer.lastName,
                        email = fetchedCustomer.email,
                        city = fetchedCustomer.city,
                        postalCode = fetchedCustomer.postalCode,
                        address = fetchedCustomer.address,
                        country = Country.entries.firstOrNull { it.dialCode == fetchedCustomer.phoneNumber?.dialCode }
                            ?: Country.Mexico,
                        phoneNumber = fetchedCustomer.phoneNumber,
                        cart = fetchedCustomer.cart,
                    )
                    screenReady = RequestState.Success(Unit)
                } else if (data.isError()) {
                    screenReady = RequestState.Error(data.getErrorMessage())
                }
            }
        }
    }

    fun updateFirstName(value: String) {
        screenState = screenState.copy(firstName = value)
    }

    fun updateLasName(value: String) {
        screenState = screenState.copy(lastName = value)
    }

    fun updateCity(value: String) {
        screenState = screenState.copy(city = value)
    }

    fun updatePostalCode(value: Int?) {
        screenState = screenState.copy(postalCode = value)
    }

    fun updateAddress(value: String) {
        screenState = screenState.copy(address = value)
    }

    fun updateCountry(value: Country) {
        screenState = screenState.copy(
            country = value,
            phoneNumber = screenState.phoneNumber?.copy(
                dialCode = value.dialCode,
            )
        )
    }

    fun updatePhoneNumber(value: String?) {
        screenState = screenState
            .copy(
                phoneNumber =
                    PhoneNumber(
                        dialCode = screenState.country.dialCode,
                        number = value.orEmpty()
                    )
            )
    }

    private fun updateCustomer(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            customerRepository.updateCustomer(
                customer = Customer(
                    id = screenState.id,
                    firstName = screenState.firstName,
                    lastName = screenState.lastName,
                    email = screenState.email,
                    city = screenState.city,
                    postalCode = screenState.postalCode,
                    address = screenState.address,
                    phoneNumber = screenState.phoneNumber,
                ),
                onSuccess = onSuccess,
                onError = onError,
            )
        }
    }

    fun payOnDelivery(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        updateCustomer(
            onSuccess = {
                createOrder(
                    onSuccess = onSuccess,
                    onError = onError,
                )
            },
            onError = onError
        )
    }

    private fun createOrder(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            orderRepository.createOrder(
                order = Order(
                    customerId = screenState.id,
                    items = screenState.cart,
                    totalAmount = savedStateHandle.get<String>("totalAmount")?.toDoubleOrNull() ?: 0.0,
                ),
                onSuccess = onSuccess,
                onError = onError,
            )
        }
    }
    
    fun payWithPaypal(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val totalAmount = savedStateHandle.get<String>("totalAmount")
        if (totalAmount != null) {
            viewModelScope.launch {
                payPalAPI.beginCheckout(
                    amount = Amount(
                        currencyCode = "USD",
                        value = totalAmount
                    ),
                    fullName = "${screenState.firstName} ${screenState.lastName}",
                    shippingAddress = ShippingAddress(
                        addressLine1 = screenState.address ?: "Unknown address",
                        state = screenState.country.name,
                        city = screenState.city ?: "Unknown city ",
                        postalCode = screenState.postalCode.toString(),
                        countryCode = screenState.country.code,
                    ),
                    onSuccess = onSuccess,
                    onError = onError,
                )
            }
        }
    }
}