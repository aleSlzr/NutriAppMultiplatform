package org.alia.nutrisport.category_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.alia.nutrisport.data.domain.ProductRepository
import org.alia.nutrisport.shared.domain.ProductCategory
import org.alia.nutrisport.shared.util.RequestState

class CategorySearchViewModel(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    val products = productRepository.readProductByCategoryList(
        category = ProductCategory.valueOf(savedStateHandle.get<String>("category") ?: ProductCategory.Protein.name)
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading,
    )
}