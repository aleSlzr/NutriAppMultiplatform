package org.alia.nutrisport.admin_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.alia.nutrisport.data.domain.AdminRepository
import org.alia.nutrisport.shared.util.RequestState

class AdminPanelViewModel(
    private val adminRepository: AdminRepository,
) : ViewModel() {
    val products = adminRepository.readLastTenProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading,
        )
}