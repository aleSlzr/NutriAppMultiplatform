package org.alia.nutrisport.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.alia.nutrisport.admin_panel.AdminPanelScreen
import org.alia.nutrisport.auth.AuthScreen
import org.alia.nutrisport.category_search.CategorySearchScreen
import org.alia.nutrisport.checkout.CheckoutScreen
import org.alia.nutrisport.details.DetailScreen
import org.alia.nutrisport.manage_product.ManageProductScreen
import org.alia.nutrisport.payment_completed.PaymentCompletedScreen
import org.alia.nutrisport.profile.ProfileScreen
import org.alia.nutrisport.shared.domain.ProductCategory
import org.alia.nutrisport.shared.navigation.Screen
import org.alia.nutrisport.shared.util.IntentHandler
import org.aliaslzr.nutrisport.home.HomeGraphScreen
import org.koin.compose.koinInject

@Composable
fun NavGraph(startDestination: Screen = Screen.Auth) {
    val navController = rememberNavController()
    val intentHandler = koinInject<IntentHandler>()
    val navigateTo by intentHandler.navigateTo.collectAsState()

    LaunchedEffect(navigateTo) {
        navigateTo?.let { paymentCompleted ->
            println("Navigating to PaymentCompleted")
            navController.navigate(paymentCompleted)
            intentHandler.resetNavigation()
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Screen.Auth> {
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo<Screen.Auth> { inclusive = true }
                    }
                },
            )
        }
        composable<Screen.HomeGraph> {
            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth) {
                        popUpTo<Screen.HomeGraph> { inclusive = true }
                    }
                },
                navigateToProfile = {
                    navController.navigate(Screen.Profile)
                },
                navigateToAdminPanel = {
                    navController.navigate(Screen.AdminPanel)
                },
                navigateToDetails = { productId ->
                    navController.navigate(Screen.DetailScreen(id = productId))
                },
                navigateToCategorySearch = { categoryName ->
                    navController.navigate(Screen.CategorySearch(category = categoryName))
                },
                navigateToCheckout = { totalAmount ->
                    navController.navigate(Screen.Checkout(totalAmount = totalAmount))
                }
            )
        }
        composable<Screen.Profile> {
            ProfileScreen(
                navigateBack = {
                    navController.navigateUp()
                },
            )
        }
        composable<Screen.AdminPanel> {
            AdminPanelScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToManageProduct = { id ->
                    navController.navigate(Screen.ManageProduct(id = id))
                },
            )
        }
        composable<Screen.ManageProduct> {
            val id = it.toRoute<Screen.ManageProduct>().id
            ManageProductScreen(
                id = id,
                navigateBack = {
                    navController.navigateUp()
                },
            )
        }
        composable<Screen.DetailScreen> {
            DetailScreen(
                navigateBack = {
                    navController.navigateUp()
                },
            )
        }
        composable<Screen.CategorySearch> {
            val category = ProductCategory.valueOf(it.toRoute<Screen.CategorySearch>().category)
            CategorySearchScreen(
                category = category,
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToDetails = { productId ->
                    navController.navigate(Screen.DetailScreen(id = productId))
                }
            )
        }
        composable<Screen.Checkout> {
            val totalAmount = it.toRoute<Screen.Checkout>().totalAmount
            CheckoutScreen(
                totalAmount = totalAmount.toDoubleOrNull() ?: 0.0,
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToPaymentCompleted = { isSuccess, error ->
                    navController.navigate(Screen.PaymentCompleted(isSuccess, error))
                }
            )
        }
        composable<Screen.PaymentCompleted> {
            val isSuccess = it.toRoute<Screen.PaymentCompleted>().isSuccess
            val error = it.toRoute<Screen.PaymentCompleted>().error
            PaymentCompletedScreen(
                navigateBack = {
                    navController.navigate(Screen.HomeGraph) {
                        launchSingleTop = true
                        // Clear backstack completely
                        popUpTo(0) { inclusive = true }
                    }
                },
                isSuccess = isSuccess,
                error = error,
            )
        }
    }
}