package org.alia.nutrisport.data.domain

import org.alia.nutrisport.shared.domain.Order

interface OrderRepository {
    fun getCurrentUserId(): String?
    suspend fun createOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )
}