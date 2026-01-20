package org.alia.nutrisport.data.domain

import kotlinx.coroutines.flow.Flow
import org.alia.nutrisport.shared.domain.Product
import org.alia.nutrisport.shared.util.RequestState

interface ProductRepository {
    fun getCurrentUserId(): String?
    fun readDiscountedProducts(): Flow<RequestState<List<Product>>>
    fun readNewProducts(): Flow<RequestState<List<Product>>>
    fun readProductById(id: String): Flow<RequestState<Product>>
    fun readProductByIdList(id: List<String>): Flow<RequestState<List<Product>>>
}