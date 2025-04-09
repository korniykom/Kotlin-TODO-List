package com.korniykom.todo_list.ui.viewmodels.mvi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korniykom.domain.model.Todo
import com.korniykom.domain.usecase.AddTodoUseCase
import com.korniykom.domain.usecase.GetTodoByIdUseCase
import com.korniykom.domain.usecase.UpdateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditTodoViewModel @Inject constructor(
    private val getTodoByIdUseCase : GetTodoByIdUseCase ,
    private val addTodoUseCase : AddTodoUseCase ,
    private val updateTodoUseCase : UpdateTodoUseCase ,
    savedStateHandle : SavedStateHandle
) : ViewModel() {

    private val todoId : Long = savedStateHandle.get<Long>("todoId") ?: 0L

    private val _state = MutableStateFlow(EditTodoState(id = todoId))
    val state : StateFlow<EditTodoState> = _state.asStateFlow()

    fun processIntent(intent : EditTodoIntent) {
        when (intent) {
            EditTodoIntent.Initialize -> initializeTodo()
            EditTodoIntent.SaveTodo -> saveTodo()
            is EditTodoIntent.SetChecked -> updateChecked(intent.checked)
            is EditTodoIntent.SetDescription -> updateDescription(intent.description)
            is EditTodoIntent.SetTitle -> updateTitle(intent.title)
        }
    }

    private fun initializeTodo() {
        if (_state.value.isInitialized) return

        viewModelScope.launch(Dispatchers.IO) {
            if (todoId > 0) {
                val todo = getTodoByIdUseCase(todoId)
                if (todo != null) {
                    _state.value = _state.value.copy(
                        id = todo.id ,
                        title = todo.title ,
                        description = todo.description ,
                        isChecked = todo.isCompleted ,
                        isInitialized = true
                    )
                }
            } else {
                _state.value = _state.value.copy(
                    isInitialized = true
                )
            }
        }
    }

    private fun saveTodo() {
        viewModelScope.launch {
            val todo = Todo(
                id = _state.value.id ,
                title = _state.value.title ,
                description = _state.value.description ,
                isCompleted = _state.value.isChecked
            )
            if (todoId > 0) {
                updateTodoUseCase(todo)
            } else {
                addTodoUseCase(todo)
            }
            _state.value.copy(
                isInitialized = true
            )
        }
    }

    private fun updateChecked( checked: Boolean) {
        _state.value = _state.value.copy(isChecked = checked)
    }

    private fun updateDescription(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    private fun updateTitle(title: String) {
        _state.value = _state.value.copy(title = title)
    }
}

