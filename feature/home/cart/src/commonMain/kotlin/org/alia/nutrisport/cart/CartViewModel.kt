package org.alia.nutrisport.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.alia.nutrisport.data.domain.CustomerRepository
import org.alia.nutrisport.data.domain.ProductRepository
import org.alia.nutrisport.shared.util.RequestState

class CartViewModel(
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository,
): ViewModel() {
    private val customer = customerRepository.readCustomerFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val products = customer
        .flatMapLatest { customerState ->
            if (customerState.isSuccess()) {
                val productIdList = customerState.getSuccessData().cart.map { it.productId }.toSet()
                if (productIdList.isNotEmpty()) {
                    productRepository.readProductByIdList(productIdList.toList())
                } else {
                    flowOf(RequestState.Success(emptyList()))
                }
            } else if (customerState.isError()) {
                flowOf(RequestState.Error(customerState.getErrorMessage()))
            } else {
                flowOf(RequestState.Loading)
            }
        }

    val cartItemsWithProducts = combine(customer, products) { customerState, productListState ->
        when {
            customerState.isSuccess() && productListState.isSuccess() -> {
                val cart = customerState.getSuccessData().cart
                val productList = productListState.getSuccessData()

                val result = cart.mapNotNull { cartItem ->
                    val product = productList.find { it.id == cartItem.productId }
                    product?.let { cartItem to it }
                }

                RequestState.Success(result)
            }
            customerState.isError() -> RequestState.Error(customerState.getErrorMessage())
            productListState.isError() -> RequestState.Error(productListState.getErrorMessage())
            else -> RequestState.Loading
        }
    }

    fun updateCartItemQuantity(
        id: String,
        quantity: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            customerRepository.updateCartItemQuantity(
                id = id,
                quantity = quantity,
                onSuccess = onSuccess,
                onError = onError,
            )
        }
    }

    fun deleteCartItem(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            customerRepository.deleteCartItem(
                id = id,
                onSuccess = onSuccess,
                onError = onError,
            )
        }
    }
}