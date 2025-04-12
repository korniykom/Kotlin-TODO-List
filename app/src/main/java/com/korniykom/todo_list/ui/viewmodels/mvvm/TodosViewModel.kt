package com.korniykom.todo_list.ui.viewmodels.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korniykom.data.remote.NetworkApi
import com.korniykom.domain.model.Todo
import com.korniykom.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val todoRepository : TodoRepository ,
    private val networkApi : NetworkApi
) : ViewModel() {

    private var _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos : StateFlow<List<Todo>> = _todos

    private val _publicIp = MutableStateFlow("No IP address")
    val publicIp : StateFlow<String> = _publicIp

    init {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.getTodos().collect { todosList ->
                _todos.value = todosList
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _publicIp.value = networkApi.getPublicIp()
            } catch (e : Exception) {
                _publicIp.value = "Unable to fetch IP address"
            }
        }
    }

    fun onTodoDelete(id : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.deleteTodo(id)
        }
    }

    fun onToggleChecked(todo : Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentCheckedState = todo.isCompleted
            val newTodo = todo.copy(isCompleted = ! currentCheckedState)
            todoRepository.updateTodo(newTodo)
        }
    }
}