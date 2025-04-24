package com.korniykom.data.repository

import com.korniykom.data.local.TodoDao
import com.korniykom.data.local.entity.TodoEntity
import com.korniykom.domain.model.Todo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TodoRepositoryImplTest {
    @MockK
    private lateinit var todoDao : TodoDao

    private lateinit var todoRepositoryImpl : TodoRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        todoRepositoryImpl = TodoRepositoryImpl(todoDao)
    }

    @Test
    fun getTodos_maps_entities_to_domain_model_correct() : Unit = runBlocking {
        val todoEntities = listOf(
            TodoEntity(1 , "Blink 1 times" , "Do it regularly" , false) ,
            TodoEntity(2 , "Buy milk" , "The last milk didn't age well..." , true)
        )
        every { todoDao.getTodos() } returns flowOf(todoEntities)

        val todos = todoRepositoryImpl.getTodos().first()

        assertEquals(todos.size , 2)
        assertEquals(Todo(1 , "Blink 1 times" , "Do it regularly" , false) , todos[0])
        assertEquals(
            Todo(2 , "Buy milk" , "The last milk didn't age well..." , true) ,
            todos[1]
        )
        verify { todoDao.getTodos() }
    }

    @Test
    fun getTodoById_returns_domain_model_when_exist() : Unit = runBlocking {
        val todoId = 1L
        val todoEntity = TodoEntity(
            id = todoId ,
            title = "Brush teeth" ,
            description = "Do it properly" ,
            isCompleted = true
        )
        val expectedTodo = Todo(
            id = todoId ,
            title = "Brush teeth" ,
            description = "Do it properly" ,
            isCompleted = true
        )
        coEvery { todoDao.getTodoById((todoId)) } returns todoEntity

        val result = todoRepositoryImpl.getTodoById(todoId)

        assertEquals(expectedTodo , result)
        coVerify { todoDao.getTodoById(todoId) }
    }

    @Test
    fun getTodoBYId_returns_null_when_does_not_exist() : Unit = runBlocking {
        val todoId = 1L
        coEvery { todoDao.getTodoById((todoId)) } returns null

        val result = todoRepositoryImpl.getTodoById(todoId)

        assertNull(result)
        coVerify { todoDao.getTodoById(todoId) }
    }

    @Test
    fun insertTodo_convert_model_to_entity_and_return_id() : Unit = runBlocking {
        val todo = Todo(
            id = 1 ,
            title = "Go jogging" ,
            description = "Go jogging from 7:30am to 8:30am" ,
            isCompleted = true
        )
        val todoEntity = TodoEntity.Companion.fromDomainModel(todo)
        val expectedId = 1L
        coEvery { todoDao.insertTodo(any()) } returns expectedId

        val result = todoRepositoryImpl.insertTodo(todo)

        assertEquals(expectedId , result)
        coVerify {
            todoDao.insertTodo(match {
                it.id == todoEntity.id && it.title == todoEntity.title
                it.description == todoEntity.description && it.isCompleted == todoEntity.isCompleted
            })
        }
    }

    @Test
    fun updateTodo_convert_domain_model_and_call_dao() : Unit = runBlocking {
        val todo = Todo(
            id = 1 , title = "Cook dinner" , description = "Cook dinner today" , isCompleted = true
        )
        coEvery { todoDao.updateTodo(any()) } just runs

        todoRepositoryImpl.updateTodo(todo)

        coVerify {
            todoDao.updateTodo(match {
                it.id == todo.id && it.title == todo.title
                it.description == todo.description && it.isCompleted == todo.isCompleted
            })
        }
    }

    @Test
    fun deleteTodo_calls_dao_with_correct_id() : Unit = runBlocking {
        val todoId = 1L
        coEvery { todoDao.deleteTodo(any()) } just runs

        todoRepositoryImpl.deleteTodo(todoId)

        coVerify { todoDao.deleteTodo(todoId) }
    }

}