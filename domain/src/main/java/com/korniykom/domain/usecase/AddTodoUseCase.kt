package com.korniykom.domain.usecase

import com.korniykom.domain.model.Todo
import com.korniykom.domain.repository.TodoRepository
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(
    private val todoRepository : TodoRepository
) {
    suspend operator fun invoke(todo : Todo) : Long {
        return todoRepository.insertTodo(todo)
    }
}