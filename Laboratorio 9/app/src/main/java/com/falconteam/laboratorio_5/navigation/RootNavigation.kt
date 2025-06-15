package com.falconteam.laboratorio_5.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.falconteam.laboratorio_5.screens.auth.AuthViewModel
import com.falconteam.laboratorio_5.screens.auth.LoginScreen
import com.falconteam.laboratorio_5.screens.MainScreen
import com.falconteam.laboratorio_5.ui.theme.Laboratorio5Theme
import com.falconteam.laboratorio_5.utils.BlogsitoViewModel

sealed class GRAPH(val graph: String) {
    data object MAIN: GRAPH(graph = "MAIN")
    data object AUTH: GRAPH(graph = "AUTH")
}

sealed class MainScreens(val route: String) {
    data object MainScreen: MainScreens(route = "main_screen")
}

sealed class AuthScreens(val route: String) {
    data object LoginScreen: MainScreens(route = "main_screen")
}

@Composable
fun RootNavigation(
    blogViewModel: BlogsitoViewModel,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    Laboratorio5Theme {
        NavHost(
            navController = navController,
            route = GRAPH.AUTH.graph,
            startDestination = AuthScreens.LoginScreen.route
        ) {
            composable(AuthScreens.LoginScreen.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginClick = {
                        blogViewModel.getPosts()
                        navController.navigate(GRAPH.MAIN.graph) {
                            popUpTo(GRAPH.AUTH.graph) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            navigation(
                route = GRAPH.MAIN.graph,
                startDestination = MainScreens.MainScreen.route
            ) {
                composable(MainScreens.MainScreen.route) {
                    MainScreen(
                        viewModel = blogViewModel,
                        onCloseSessionClick = {
                            navController.navigate(GRAPH.AUTH.graph) {
                                popUpTo(GRAPH.MAIN.graph) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}