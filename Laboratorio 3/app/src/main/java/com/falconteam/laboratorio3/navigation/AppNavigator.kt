
package com.falconteam.laboratorio3.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.falconteam.laboratorio3.screens.HomeScreen
import com.falconteam.laboratorio3.screens.TodoListScreen
import com.falconteam.laboratorio3.screens.GiroscopioScreen
import com.tuapp.viewmodel.GeneralViewModel

@Composable
fun AppNavigator() {
    val navController: NavHostController = rememberNavController()
    val viewModel: GeneralViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("todo") { TodoListScreen(viewModel = viewModel, navController = navController) }
        composable("sensor") { GiroscopioScreen(navController) }
    }
}
