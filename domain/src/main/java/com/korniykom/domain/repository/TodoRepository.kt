package com.korniykom.domain.repository

import com.korniykom.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos() : Flow<List<Todo>>
    suspend fun getTodoById(id : Long) : Todo?
    suspend fun insertTodo(todo : Todo) : Long
    suspend fun updateTodo(todo : Todo)
    suspend fun deleteTodo(id : Long)
}