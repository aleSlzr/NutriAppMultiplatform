package org.alia.nutrisport.data.domain

import dev.gitlive.firebase.storage.File
import org.alia.nutrisport.shared.domain.Product

interface AdminRepository {
    fun getCurrentUserId(): String?
    suspend fun createNewProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )
    suspend fun uploadImageToStorage(file: File): String?
}