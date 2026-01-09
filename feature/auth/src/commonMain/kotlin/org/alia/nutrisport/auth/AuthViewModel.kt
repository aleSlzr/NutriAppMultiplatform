package org.alia.nutrisport.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import org.alia.nutrisport.data.domain.CustomerRepository

class AuthViewModel(
    private val customerRepository: CustomerRepository
): ViewModel() {
    fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch { 
            customerRepository
                .createCustomer(
                    user = user,
                    onSuccess = onSuccess,
                    onError = onError,
                )
        }
    }
}