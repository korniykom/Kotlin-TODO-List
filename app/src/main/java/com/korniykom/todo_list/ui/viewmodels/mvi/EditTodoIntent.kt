package com.korniykom.todo_list.ui.viewmodels.mvi

sealed class EditTodoIntent {
    data class SetTitle(val title : String) : EditTodoIntent()
    data class SetDescription(val description : String) : EditTodoIntent()
    data class SetChecked (val checked: Boolean) : EditTodoIntent()
    object SaveTodo : EditTodoIntent()
    object Initialize : EditTodoIntent()
}
