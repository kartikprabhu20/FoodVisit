package com.mintanable.foodvisit.ui.navigation

import android.net.Uri
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import androidx.compose.ui.tooling.preview.Preview
import com.mintanable.core.model.Restaurant
import com.mintanable.foodvisit.ui.screens.aboutus.AboutUsScreen
import com.mintanable.foodvisit.ui.screens.detail.DetailScreen
import com.mintanable.foodvisit.ui.screens.home.HomeScreen
import com.mintanable.foodvisit.ui.screens.maps.MapsScreen
import com.mintanable.foodvisit.ui.screens.settings.SettingsScreen
import com.mintanable.foodvisit.ui.screens.tovisit.ToVisitScreen
import com.mintanable.foodvisit.ui.theme.FoodVisitTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val gson = remember { Gson() }

    val openDrawer: () -> Unit = { scope.launch { drawerState.open() } }
    val navigateToDetail: (Restaurant) -> Unit = { restaurant ->
        val json = Uri.encode(gson.toJson(restaurant))
        navController.navigate(Screen.Detail.createRoute(json))
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute != Screen.Detail.route,
        drawerContent = {
            AppDrawerContent(
                currentRoute = currentRoute,
                onDestinationClick = { route ->
                    scope.launch { drawerState.close() }
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) {
        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onOpenDrawer = openDrawer,
                        onRestaurantClick = navigateToDetail,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }
                composable(Screen.ToVisit.route) {
                    ToVisitScreen(onOpenDrawer = openDrawer, onRestaurantClick = navigateToDetail)
                }
                composable(Screen.Maps.route) {
                    MapsScreen(onOpenDrawer = openDrawer)
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        onOpenDrawer = openDrawer,
                        onNavigateUp = { navController.navigateUp() }
                    )
                }
                composable(Screen.AboutUs.route) {
                    AboutUsScreen(onOpenDrawer = openDrawer)
                }
                composable(
                    route = Screen.Detail.route,
                    arguments = listOf(navArgument("restaurantJson") { type = NavType.StringType })
                ) { entry ->
                    val json = Uri.decode(entry.arguments?.getString("restaurantJson") ?: "")
                    val restaurant = remember(json) {
                        try { gson.fromJson(json, Restaurant::class.java) } catch (_: Exception) { null }
                    }
                    DetailScreen(
                        restaurant = restaurant,
                        onNavigateUp = { navController.navigateUp() },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }
            }
        }
    }
}

@Composable
private fun AppDrawerContent(
    currentRoute: String?,
    onDestinationClick: (String) -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "FoodVisit",
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))

        val items = listOf(
            Triple(Screen.Home.route,     "Home",     Icons.Default.Home),
            Triple(Screen.ToVisit.route,  "To Visit", Icons.AutoMirrored.Filled.List),
            Triple(Screen.Maps.route,     "Maps",     Icons.Default.Map),
            Triple(Screen.Settings.route, "Settings", Icons.Default.Settings)
        )
        items.forEach { (route, label, icon) ->
            NavigationDrawerItem(
                icon    = { Icon(icon, contentDescription = label) },
                label   = { Text(label) },
                selected = currentRoute == route,
                onClick  = { onDestinationClick(route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Other",
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            style = MaterialTheme.typography.labelSmall
        )
        NavigationDrawerItem(
            icon    = { Icon(Icons.Default.Info, contentDescription = "About Us") },
            label   = { Text("About Us") },
            selected = currentRoute == Screen.AboutUs.route,
            onClick  = { onDestinationClick(Screen.AboutUs.route) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun AppDrawerContentPreview() {
    FoodVisitTheme {
        AppDrawerContent(
            currentRoute = Screen.Home.route,
            onDestinationClick = {}
        )
    }
}
