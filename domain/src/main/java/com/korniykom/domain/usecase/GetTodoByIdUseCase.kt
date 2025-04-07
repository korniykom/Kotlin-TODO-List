package com.korniykom.domain.usecase

import com.korniykom.domain.model.Todo
import com.korniykom.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoByIdUseCase @Inject constructor(
    private val todoRepository : TodoRepository
) {
    suspend operator fun invoke(id : Long) : Todo? {
        return todoRepository.getTodoById(id)
    }
}