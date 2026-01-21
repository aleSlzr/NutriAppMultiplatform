package org.alia.nutrisport.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.alia.nutrisport.admin_panel.AdminPanelScreen
import org.alia.nutrisport.auth.AuthScreen
import org.alia.nutrisport.category_search.CategorySearchScreen
import org.alia.nutrisport.details.DetailScreen
import org.alia.nutrisport.manage_product.ManageProductScreen
import org.alia.nutrisport.profile.ProfileScreen
import org.alia.nutrisport.shared.domain.ProductCategory
import org.alia.nutrisport.shared.navigation.Screen
import org.aliaslzr.nutrisport.home.HomeGraphScreen

@Composable
fun NavGraph(startDestination: Screen = Screen.Auth) {
    val navController = rememberNavController()
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
    }
}