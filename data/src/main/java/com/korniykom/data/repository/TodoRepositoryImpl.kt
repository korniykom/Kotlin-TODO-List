package com.korniykom.data.repository

import com.korniykom.data.local.TodoDao
import com.korniykom.data.local.entity.TodoEntity
import com.korniykom.domain.model.Todo
import com.korniykom.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDao : TodoDao
) : TodoRepository {

    override fun getTodos() : Flow<List<Todo>> {
        return todoDao.getTodos().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getTodoById(id : Long) : Todo? {
        return todoDao.getTodoById(id)?.toDomainModel()
    }

    override suspend fun insertTodo(todo : Todo) : Long {
        return todoDao.insertTodo(TodoEntity.fromDomainModel(todo))
    }

    override suspend fun updateTodo(todo : Todo) {
        todoDao.updateTodo(TodoEntity.fromDomainModel(todo))
    }

    override suspend fun deleteTodo(id : Long) {
        todoDao.deleteTodo(id)
    }

}