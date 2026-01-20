package org.alia.nutrisport.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.alia.nutrisport.data.domain.CustomerRepository
import org.alia.nutrisport.data.domain.ProductRepository
import org.alia.nutrisport.shared.domain.CartItem
import org.alia.nutrisport.shared.util.RequestState

class DetailsViewModel(
    private val productRepository: ProductRepository,
    private val customerRepository: CustomerRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val product = productRepository.readProductById(
        savedStateHandle.get<String>("id") ?: ""
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading,
    )

    var quantity by mutableStateOf(1)
        private set

    var selectedFlavor: String? by mutableStateOf(null)
        private set

    fun updateFlavor(value: String) {
        selectedFlavor = value
    }

    fun updateQuantity(value: Int) {
        quantity = value
    }

    fun addItemToCart(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val productId = savedStateHandle.get<String>("id")
            if (productId != null) {
                customerRepository.addItemToCart(
                    cartItem = CartItem(
                        productId = productId,
                        flavor = selectedFlavor,
                        quantity = quantity
                    ),
                    onSuccess = onSuccess,
                    onError = onError,
                )
            }
        }
    }
}