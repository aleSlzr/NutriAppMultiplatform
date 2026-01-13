package org.alia.nutrisport.di

import org.alia.nutrisport.auth.AuthViewModel
import org.alia.nutrisport.data.AdminRepositoryImpl
import org.alia.nutrisport.data.CustomerRepositoryImpl
import org.alia.nutrisport.data.domain.AdminRepository
import org.alia.nutrisport.data.domain.CustomerRepository
import org.alia.nutrisport.manage_product.ManageProductViewModel
import org.alia.nutrisport.profile.ProfileViewModel
import org.aliaslzr.nutrisport.home.HomeGraphViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<CustomerRepository> { CustomerRepositoryImpl() }
    single<AdminRepository> { AdminRepositoryImpl() }
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeGraphViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ManageProductViewModel)
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
)  {
    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}