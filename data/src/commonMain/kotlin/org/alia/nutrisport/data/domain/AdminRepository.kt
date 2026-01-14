package org.alia.nutrisport.data.domain

import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.flow.Flow
import org.alia.nutrisport.shared.domain.Product
import org.alia.nutrisport.shared.util.RequestState

interface AdminRepository {
    fun getCurrentUserId(): String?
    suspend fun createNewProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )
    suspend fun uploadImageToStorage(file: File): String?
    suspend fun deleteImageFromStorage(
        downloadUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )
    fun readLastTenProducts(): Flow<RequestState<List<Product>>>
    suspend fun readProductById(productId: String): RequestState<Product>
}