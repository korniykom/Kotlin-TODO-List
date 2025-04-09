package com.korniykom.todo_list.ui.viewmodels.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korniykom.domain.model.Todo
import com.korniykom.domain.usecase.AddTodoUseCase
import com.korniykom.domain.usecase.DeleteTodoUseCase
import com.korniykom.domain.usecase.GetPublicIpUseCase
import com.korniykom.domain.usecase.GetTodosUseCase
import com.korniykom.domain.usecase.UpdateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val getTodosUseCase : GetTodosUseCase ,
    private val deleteTodoUseCase : DeleteTodoUseCase ,
    private val getPublicIpUseCase : GetPublicIpUseCase,
) : ViewModel() {

    private var _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos : StateFlow<List<Todo>> = _todos

    private val _publicIp = MutableStateFlow("No IP address")
    val publicIp : StateFlow<String> = _publicIp

    init {
        viewModelScope.launch {
            getTodosUseCase().collect {todosList ->
                _todos.value = todosList
            }
            try {
                _publicIp.value = getPublicIpUseCase()
            } catch (e : Exception) {
                _publicIp.value = "Unable to fetch IP address"
            }
        }
    }
    fun onTodoDelete(id : Long) {
        viewModelScope.launch {
            deleteTodoUseCase(id)
        }
    }
}