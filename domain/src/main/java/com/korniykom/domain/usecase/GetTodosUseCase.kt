package com.korniykom.domain.usecase

import com.korniykom.domain.model.Todo
import com.korniykom.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(
    private val todoRepository : TodoRepository
) {
    operator fun invoke() : Flow<List<Todo>> {
        return todoRepository.getTodos()
    }
}