package com.korniykom.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.korniykom.data.local.entity.TodoEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoDatabaseTest {
    private lateinit var database : TodoDatabase
    private lateinit var dao : TodoDao


    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext() ,
            TodoDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.todoDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insert_in_database_correct() : Unit = runTest {
        val id = 1L;
        val todoItem = TodoEntity(
            id = id ,
            title = "Brand New Task" ,
            description = "Renamed old task" ,
            isCompleted = false
        )
        dao.insertTodo(todoItem)

        val insertedItem = dao.getTodoById(id)

        assertEquals(todoItem , insertedItem)

    }

    @Test
    fun delete_from_database_correct() : Unit = runTest {
        val id = 1L;
        val todoItem = TodoEntity(
            id = id ,
            title = "Brand New Task" ,
            description = "Renamed old task" ,
            isCompleted = false
        )
        dao.insertTodo(todoItem)

        dao.deleteTodo(id)
        val deletedItem = dao.getTodoById(id)

        assertNull(deletedItem)
    }

    @Test
    fun getAllTodos_is_correct() : Unit = runTest {
        val todoItem1 = TodoEntity(
            id = 1 ,
            title = "Brand New Task" ,
            description = "Renamed old task" ,
            isCompleted = false
        )
        val todoItem2 = TodoEntity(
            id = 2 ,
            title = "Brand New Task2" ,
            description = "Renamed old task2" ,
            isCompleted = true
        )
        val expectedTodos = listOf(todoItem1 , todoItem2)

        dao.insertTodo(todoItem1)
        dao.insertTodo(todoItem2)

        val allTodos = dao.getTodos().first()

        assertTrue(allTodos.containsAll(expectedTodos))
    }

    @Test
    fun updateTodo_is_correct() : Unit = runTest {
        val id = 1L
        val todoItem1 = TodoEntity(
            id = id ,
            title = "Brand New Task" ,
            description = "Renamed old task" ,
            isCompleted = false
        )
        val updatedTodoItem = TodoEntity(
            id = id ,
            title = "Old task" ,
            description = "I forgot to do it" ,
            isCompleted = false
        )
        dao.insertTodo(todoItem1)


        dao.updateTodo(updatedTodoItem)

        val todoInDatabase = dao.getTodoById(id)
        assertEquals(todoInDatabase , updatedTodoItem)

    }
}


