package com.korniykom.todo_list.ui.navigation

sealed class Screen(val route : String) {
    object Loading : Screen("Loading")
    object Todos : Screen("Todos")
    object Edit : Screen("Edit/{todoId}") {
        fun createRoute(todoId : Long) : String = "Edit/$todoId"
    }
}