package com.korniykom.todo_list.ui.viewmodels.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korniykom.domain.model.Todo
import com.korniykom.domain.usecase.DeleteTodoUseCase
import com.korniykom.domain.usecase.GetPublicIpUseCase
import com.korniykom.domain.usecase.GetTodosUseCase
import com.korniykom.domain.usecase.UpdateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val getTodosUseCase : GetTodosUseCase ,
    private val deleteTodoUseCase : DeleteTodoUseCase ,
    private val getPublicIpUseCase : GetPublicIpUseCase ,
    private val updateTodoUseCase : UpdateTodoUseCase
) : ViewModel() {

    private var _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos : StateFlow<List<Todo>> = _todos

    private val _publicIp = MutableStateFlow("No__ IP address")
    val publicIp : StateFlow<String> = _publicIp

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getTodosUseCase().collect { todosList ->
                _todos.value = todosList
            }
        }

        viewModelScope.launch(Dispatchers.IO) {

            try {
                _publicIp.value = getPublicIpUseCase()
            } catch (e : Exception) {
                Log.e("IP" , e.message.toString())
                _publicIp.value = "Unable to fetch IP address"
            }
        }
    }

    fun onTodoDelete(id : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTodoUseCase(id)
        }
    }

    fun onToggleChecked(todo : Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentCheckedState = todo.isCompleted
            val newTodo = todo.copy(isCompleted = ! currentCheckedState)
            updateTodoUseCase(newTodo)
        }
    }
}