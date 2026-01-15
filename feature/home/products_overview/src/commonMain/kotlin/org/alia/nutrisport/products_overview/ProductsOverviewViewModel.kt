package org.alia.nutrisport.products_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.alia.nutrisport.data.domain.ProductRepository
import org.alia.nutrisport.shared.util.RequestState

class ProductsOverviewViewModel(
    private val productRepository: ProductRepository,
): ViewModel() {

    val products = combine(
        productRepository.readNewProducts(),
        productRepository.readDiscountedProducts(),
    ) { new, discounted ->
        when {
            new.isSuccess() && discounted.isSuccess() -> {
                RequestState.Success(new.getSuccessData() + discounted.getSuccessData())
            }
            new.isError() -> new
            discounted.isError() -> discounted
            else -> RequestState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading,
    )
}