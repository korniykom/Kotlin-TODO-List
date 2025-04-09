package com.korniykom.todo_list.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.korniykom.todo_list.ui.screens.EditTodoScreen
import com.korniykom.todo_list.ui.screens.LoadingScreen
import com.korniykom.todo_list.ui.screens.TodosScreen

enum class TodoScreen {
    Loading , Todos , Edit
}

@Composable
fun TodoNavHost(
    navController : NavHostController = rememberNavController() ,
    startDestination : String = TodoScreen.Todos.name
) {
    NavHost(
        navController = navController , startDestination = startDestination
    ) {
        composable(route = TodoScreen.Loading.name) {
            LoadingScreen(onLoadingFinished = { navController.navigate(TodoScreen.Todos.name) })
        }
        composable(route = TodoScreen.Todos.name) {
            TodosScreen()
        }
        composable(route = TodoScreen.Edit.name) {
            EditTodoScreen()
        }

    }
}