package org.alia.nutrisport.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.alia.nutrisport.data.domain.AdminRepository
import org.alia.nutrisport.shared.domain.Product

class AdminRepositoryImpl : AdminRepository {
    override fun getCurrentUserId() = Firebase.auth.currentUser?.uid

    override suspend fun createNewProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUser = getCurrentUserId()
            if (currentUser != null) {
                val firestore = Firebase.firestore
                val productCollection = firestore.collection(collectionPath = "product")
                productCollection.document(product.id).set(product)
                onSuccess()
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while creating a new product: ${e.message}")
        }
    }
}