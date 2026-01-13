package org.alia.nutrisport.data.domain

import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import org.alia.nutrisport.shared.domain.Customer
import org.alia.nutrisport.shared.util.RequestState

interface CustomerRepository {
    fun getCurrentUserId(): String?
    suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )
    fun readCustomerFlow(): Flow<RequestState<Customer>>
    suspend fun signOut(): RequestState<Unit>
}