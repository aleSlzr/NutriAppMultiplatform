package org.alia.nutrisport.products_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.alia.nutrisport.data.domain.ProductRepository
import org.alia.nutrisport.shared.util.RequestState

class ProductsOverviewViewModel(
    private val productRepository: ProductRepository,
): ViewModel() {
    val products = productRepository.readDiscountedProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading,
        )
}