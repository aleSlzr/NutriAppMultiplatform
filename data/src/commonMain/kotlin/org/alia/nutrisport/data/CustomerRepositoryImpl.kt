package org.alia.nutrisport.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.alia.nutrisport.data.domain.CustomerRepository
import org.alia.nutrisport.shared.domain.Customer
import org.alia.nutrisport.shared.util.RequestState

class CustomerRepositoryImpl: CustomerRepository {
    override fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            if (user != null) {
                val customerCollection = Firebase.firestore.collection(collectionPath = "customer")
                val customer = Customer(
                    id = user.uid,
                    firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                    lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                    email = user.email ?: "Unknown",
                )
                val customerExists = customerCollection.document(user.uid).get().exists
                if (customerExists) {
                    onSuccess()
                } else {
                    customerCollection.document(user.uid).set(customer)
                    onSuccess()
                }
            } else {
                onError("User is not available")
            }
        } catch (e: Exception) {
            onError("Error creating a customer: ${e.message}")
        }
    }

    override suspend fun signOut(): RequestState<Unit> {
        return try {
            Firebase.auth.signOut()
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error("Error while signin out: ${e.message}")
        }
    }
}