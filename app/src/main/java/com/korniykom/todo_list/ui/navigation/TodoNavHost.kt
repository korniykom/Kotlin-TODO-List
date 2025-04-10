package com.korniykom.todo_list.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.korniykom.todo_list.ui.screens.EditTodoScreen
import com.korniykom.todo_list.ui.screens.LoadingScreen
import com.korniykom.todo_list.ui.screens.TodosScreen


@Composable
fun TodoNavHost(
    navController : NavHostController = rememberNavController() ,
    startDestination : String = Screen.Todos.route
) {
    NavHost(
        navController = navController , startDestination = startDestination
    ) {
        composable(route = Screen.Loading.route) {
            LoadingScreen(onLoadingFinished = { navController.navigate(Screen.Todos.route) })
        }
        composable(route = Screen.Todos.route) {
            TodosScreen(onEdit = { todoId ->
                navController.navigate(Screen.Edit.createRoute(todoId))
            } , onSave = { navController.navigate(Screen.Edit.createRoute(0)) })
        }
        composable(
            route = Screen.Edit.route , arguments = listOf(
                navArgument("todoId") {
                    type = NavType.LongType
                    defaultValue = 0L
                })
        ) {
            EditTodoScreen(
                onSave = { navController.popBackStack() })
        }

    }
}