package org.alia.nutrisport.data.domain

import org.alia.nutrisport.shared.domain.Product

interface AdminRepository {
    fun getCurrentUserId(): String?
    suspend fun createNewProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )
}