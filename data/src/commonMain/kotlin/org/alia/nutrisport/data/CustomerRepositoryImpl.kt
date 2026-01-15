package org.alia.nutrisport.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
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
                    customerCollection.document(user.uid)
                        .collection("privateData")
                        .document("role")
                        .set(mapOf("isAdmin" to false))
                    onSuccess()
                }
            } else {
                onError("User is not available")
            }
        } catch (e: Exception) {
            onError("Error creating a customer: ${e.message}")
        }
    }

    override fun readCustomerFlow(): Flow<RequestState<Customer>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database
                    .collection(collectionPath = "customer")
                    .document(userId)
                    .snapshots
                    .collectLatest { document ->
                        if (document.exists) {
                            val privateDataDocument = database.collection(collectionPath = "customer")
                                .document(userId)
                                .collection(collectionPath = "privateData")
                                .document("role")
                                .get()
                            val customer = Customer(
                                id = document.id,
                                firstName = document.get(field = "firstName"),
                                lastName = document.get(field = "lastName"),
                                email = document.get(field = "email"),
                                city = document.get(field = "city"),
                                postalCode = document.get(field = "postalCode"),
                                address = document.get(field = "address"),
                                phoneNumber = document.get(field = "phoneNumber"),
                                cart = document.get(field = "cart"),
                                isAdmin = privateDataDocument.get("isAdmin")
                            )
                            send(RequestState.Success(data = customer))
                        } else {
                            send(RequestState.Error(message = "Queried customer document does not exists."))
                        }
                    }
            } else {
                send(RequestState.Error(message = "User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error(message = "Error while reading a Customer information: ${e.message}"))
        }
    }

    override suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val firestore = Firebase.firestore
                val customerCollection = firestore.collection(collectionPath = "customer")
                val existingCustomer = customerCollection
                    .document(customer.id)
                    .get()

                if (existingCustomer.exists) {
                    customerCollection
                        .document(customer.id)
                        .update(customer)
                    onSuccess()
                } else {
                    onError("Customer not found.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while updating a Customer information: ${e.message}")
        }
    }

    override suspend fun signOut(): RequestState<Unit> {
        return try {
            Firebase.auth.signOut()
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error(message = "Error while signin out: ${e.message}")
        }
    }
}