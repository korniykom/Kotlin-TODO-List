package com.korniykom.todo_list.ui.viewmodels.mvi

data class EditTodoState(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val isChecked: Boolean = false,
    val isInitialized: Boolean = false
)