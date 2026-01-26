package org.alia.nutrisport.di

import org.alia.nutrisport.admin_panel.AdminPanelViewModel
import org.alia.nutrisport.auth.AuthViewModel
import org.alia.nutrisport.cart.CartViewModel
import org.alia.nutrisport.category_search.CategorySearchViewModel
import org.alia.nutrisport.checkout.CheckoutViewModel
import org.alia.nutrisport.data.AdminRepositoryImpl
import org.alia.nutrisport.data.CustomerRepositoryImpl
import org.alia.nutrisport.data.OrderRepositoryImpl
import org.alia.nutrisport.data.ProductRepositoryImpl
import org.alia.nutrisport.data.domain.AdminRepository
import org.alia.nutrisport.data.domain.CustomerRepository
import org.alia.nutrisport.data.domain.OrderRepository
import org.alia.nutrisport.data.domain.ProductRepository
import org.alia.nutrisport.details.DetailsViewModel
import org.alia.nutrisport.manage_product.ManageProductViewModel
import org.alia.nutrisport.products_overview.ProductsOverviewViewModel
import org.alia.nutrisport.profile.ProfileViewModel
import org.aliaslzr.nutrisport.home.HomeGraphViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<CustomerRepository> { CustomerRepositoryImpl() }
    single<AdminRepository> { AdminRepositoryImpl() }
    single<ProductRepository> { ProductRepositoryImpl() }
    single<OrderRepository> { OrderRepositoryImpl(get()) }
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeGraphViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ManageProductViewModel)
    viewModelOf(::AdminPanelViewModel)
    viewModelOf(::ProductsOverviewViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::CategorySearchViewModel)
    viewModelOf(::CheckoutViewModel)
}

expect val targetModule: Module

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
)  {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, targetModule)
    }
}