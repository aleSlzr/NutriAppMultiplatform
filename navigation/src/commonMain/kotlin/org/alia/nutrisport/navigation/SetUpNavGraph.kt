package org.alia.nutrisport.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.alia.nutrisport.auth.AuthScreen

@Composable
fun SetUpNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Auth,
    ) {
        composable<Screen.Auth> {
            AuthScreen()
        }
    }
}