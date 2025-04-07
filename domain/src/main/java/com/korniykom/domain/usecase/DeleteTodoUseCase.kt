package com.korniykom.domain.usecase

import com.korniykom.domain.repository.TodoRepository
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val todoRepository : TodoRepository
) {
    suspend operator fun invoke(id : Long) {
        todoRepository.deleteTodo(id)
    }
}