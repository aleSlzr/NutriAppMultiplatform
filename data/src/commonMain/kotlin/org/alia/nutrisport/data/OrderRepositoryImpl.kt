package org.alia.nutrisport.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.alia.nutrisport.data.domain.CustomerRepository
import org.alia.nutrisport.data.domain.OrderRepository
import org.alia.nutrisport.shared.domain.Order

class OrderRepositoryImpl(
    private val customerRepository: CustomerRepository,
): OrderRepository {
    override fun getCurrentUserId(): String? = Firebase.auth.currentUser?.uid

    override suspend fun createOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val database = Firebase.firestore
                val orderCollection = database.collection(collectionPath = "order")
                orderCollection
                    .document(order.orderId)
                    .set(order)
                customerRepository.deleteAllCartItems(
                    onSuccess = {},
                    onError = {},
                )
                onSuccess()
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while adding a product to cart: ${e.message}")
        }
    }
}