package org.aliaslzr.nutrisport.home.domain

import androidx.compose.ui.graphics.vector.ImageVector
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.navigation.Screen

enum class BottomBarDestination(
    val icon: ImageVector,
    val title: String,
    val screen: Screen,
) {
    ProductsOverview(
        icon = Resources.Icon.Home,
        title = "NutriSport",
        screen = Screen.ProductsOverview,
    ),
    Cart(
        icon = Resources.Icon.ShoppingCart,
        title = "Cart",
        screen = Screen.Cart,
    ),
    Categories(
        icon = Resources.Icon.Categories,
        title = "Categories",
        screen = Screen.Categories,
    ),
}