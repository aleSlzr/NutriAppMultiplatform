package org.aliaslzr.nutrisport.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alia.nutrisport.data.domain.CustomerRepository
import org.alia.nutrisport.data.domain.ProductRepository
import org.alia.nutrisport.shared.util.RequestState

class HomeGraphViewModel(
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository,
): ViewModel() {

    val customer = customerRepository.readCustomerFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading,
        )

    fun signOut(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                customerRepository.signOut()
            }
            if (result.isSuccess()) {
                onSuccess()
            } else if (result.isError()) {
                onError(result.getErrorMessage())
            }
        }
    }

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

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalAmountFlow = cartItemsWithProducts
        .flatMapLatest { data ->
            if (data.isSuccess()) {
                val items = data.getSuccessData()
                val cartItems = items.map { it.first }
                val products = items.map { it.second }.associateBy { it.id }

                val totalPrice = cartItems.sumOf { cartItem ->
                    val productPrice = products[cartItem.productId]?.price ?: 0.0
                    productPrice * cartItem.quantity
                }
                flowOf(RequestState.Success(totalPrice))
            } else if (data.isError()) {
                flowOf(RequestState.Error(data.getErrorMessage()))
            } else {
                flowOf(RequestState.Loading)
            }
        }

}