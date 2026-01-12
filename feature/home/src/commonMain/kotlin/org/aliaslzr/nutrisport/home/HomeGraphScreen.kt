package org.aliaslzr.nutrisport.home

import ContentWithMessageBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.alia.nutrisport.shared.Alpha
import org.alia.nutrisport.shared.BebasNeueFont
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.IconPrimary
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.SurfaceLighter
import org.alia.nutrisport.shared.TextPrimary
import org.alia.nutrisport.shared.navigation.Screen
import org.alia.nutrisport.shared.util.getScreenWidth
import org.aliaslzr.nutrisport.home.component.BottomBar
import org.aliaslzr.nutrisport.home.component.CustomDrawer
import org.aliaslzr.nutrisport.home.domain.BottomBarDestination
import org.aliaslzr.nutrisport.home.domain.CustomDrawerState
import org.aliaslzr.nutrisport.home.domain.isOpened
import org.aliaslzr.nutrisport.home.domain.opposite
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen(
    navigateToAuth: () -> Unit,
    navigateToProfile: () -> Unit,
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()
    val selectedDestination by remember {
        derivedStateOf {
            val route = currentRoute.value?.destination?.route.toString()
            when {
                route.contains(BottomBarDestination.ProductsOverview.screen.toString()) -> BottomBarDestination.ProductsOverview
                route.contains(BottomBarDestination.Cart.screen.toString()) -> BottomBarDestination.Cart
                route.contains(BottomBarDestination.Categories.screen.toString()) -> BottomBarDestination.Categories
                else -> BottomBarDestination.ProductsOverview
            }
        }
    }
    // this stores and saves the value only once and it's not recalculated
    // on every recomposition
    val screenWidth = remember { getScreenWidth() }
    // holds the drawer state
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }

    // calculates the distance the main content view should slide to the right when the drawer is open
    // it's set to be about two-thirds of the screen's width
    // derivedStateOf is a performance optimization
    // ensures that the calculation inside it only runs if screenWidth changes
    val offsetValue by remember {
        derivedStateOf {
            (screenWidth / 1.5).dp
        }
    }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp
    )
    val animatedBackground by animateColorAsState(
        targetValue = if (drawerState.isOpened()) SurfaceLighter else Surface
    )
    // at first state that shows the home tab it will show the full home tab
    // because drawerState is closed, so the home tab will take the whole screen
    // when drawerState opened transforms and animate to decrease the home tab screen a 90%
    // and show the drawerItem that is behind the tab screen
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f
    )
    // at first state that shows the home tab it will show the full home tab
    // because drawerState is closed, so the radius is 0dp
    // when drawerState opened transforms and animate to create a radius of 20dp
    // of the home tab
    val animatedRadius by animateDpAsState(
        targetValue = if (drawerState.isOpened()) 20.dp else 0.dp
    )

    val viewModel = koinViewModel<HomeGraphViewModel>()
    val messageBarState = rememberMessageBarState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackground)
    ) {
        CustomDrawer(
            onProfileClick = navigateToProfile,
            onContactUsClick = {},
            onSignOutClick = {
                viewModel.signOut(
                    onSuccess = navigateToAuth,
                    onError = { message ->
                        messageBarState.addError(message)
                    }
                )
            },
            onAdminPanelClick = {},
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(size = animatedRadius))
                .offset(x = animatedOffset)
                .scale(scale = animatedScale)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(size = animatedRadius),
                    ambientColor = Color.Black.copy(alpha = Alpha.DISABLED),
                    spotColor = Color.Black.copy(alpha = Alpha.DISABLED),
                ),
        ) {
            Scaffold(
                containerColor = Surface,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            AnimatedContent(
                                targetState = selectedDestination,
                            ) { destination ->
                                Text(
                                    text = destination.title,
                                    fontFamily = BebasNeueFont(),
                                    fontSize = FontSize.LARGE,
                                    color = TextPrimary,
                                )
                            }
                        },
                        navigationIcon = {
                            AnimatedContent(
                                targetState = drawerState,
                            ) { drawer ->
                                if (drawer.isOpened()) {
                                    IconButton(onClick = { drawerState = drawerState.opposite() }) {
                                        Icon(
                                            imageVector = Resources.Icon.Close,
                                            contentDescription = "Close icon",
                                            tint = IconPrimary,
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { drawerState = drawerState.opposite() }) {
                                        Icon(
                                            imageVector = Resources.Icon.Menu,
                                            contentDescription = "Menu icon",
                                            tint = IconPrimary,
                                        )
                                    }
                                }
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Surface,
                            scrolledContainerColor = Surface,
                            navigationIconContentColor = IconPrimary,
                            titleContentColor = TextPrimary,
                            actionIconContentColor = IconPrimary,
                        )
                    )
                }
            ) { padding ->
                ContentWithMessageBar(
                    messageBarState = messageBarState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding(),
                        ),
                    errorMaxLines = 2,
                    contentBackgroundColor = Surface,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        NavHost(
                            modifier = Modifier.weight(1f),
                            navController = navController,
                            startDestination = Screen.ProductsOverview
                        ) {
                            composable<Screen.ProductsOverview> {  }
                            composable<Screen.Cart> {  }
                            composable<Screen.Categories> {  }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier
                            .padding(all = 12.dp)
                        ) {
                            BottomBar(
                                selected = selectedDestination,
                                onSelect = { destination ->
                                    navController.navigate(destination.screen) {
                                        launchSingleTop = true
                                        popUpTo<Screen.ProductsOverview> {
                                            saveState = true
                                            inclusive = false
                                        }
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}