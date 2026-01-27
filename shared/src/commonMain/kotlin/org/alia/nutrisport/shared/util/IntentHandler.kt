package org.alia.nutrisport.shared.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.alia.nutrisport.shared.navigation.Screen
import org.alia.nutrisport.shared.navigation.Screen.PaymentCompleted

class IntentHandler {
    private val _navigateTo = MutableStateFlow<Screen?>(null)
    val navigateTo: StateFlow<Screen?> = _navigateTo.asStateFlow()

    fun navigateToPaymentCompleted(
        isSuccess: Boolean?,
        error: String?,
        token: String?
    ) {
        _navigateTo.value = PaymentCompleted(
            isSuccess = isSuccess,
            error = error,
            token = token,
        )
    }

    fun resetNavigation() {
        _navigateTo.value = null
    }
}