package com.jehad.foodics.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jehad.foodics.ui.components.BottomNavBar
import com.jehad.foodics.ui.components.BottomNavItem
import com.jehad.foodics.ui.screens.tables.TablesScreen
import com.jehad.foodics.ui.screens.orders.OrderScreen
import com.jehad.foodics.ui.screens.settings.SettingsScreen
import com.jehad.foodics.ui.screens.menu.MenuScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        Scaffold(
            bottomBar = {
                BottomNavBar(navController = navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Menu.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Menu.route) {
                    MenuScreen()
                }
                composable(BottomNavItem.Orders.route) {
                    OrderScreen()
                }
                composable(BottomNavItem.Tables.route) {
                    TablesScreen()
                }
                composable(BottomNavItem.Settings.route) {
                    SettingsScreen()
                }
            }
        }
    }
}