package com.korniykom.todo_list.ui.viewmodels.mvi

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.korniykom.domain.model.Todo
import com.korniykom.domain.repository.TodoRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class EditTodoViewModelTest {
    @MockK
    private lateinit var repository : TodoRepository

    private lateinit var savedStateHandle : SavedStateHandle
    private lateinit var viewModel : EditTodoViewModel

    private var testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun if_todoId_is_0L_set_isInitialized_to_true() : Unit = runTest {
        savedStateHandle = SavedStateHandle(mapOf("todoId" to 0L))

        viewModel = EditTodoViewModel(repository , savedStateHandle)
        viewModel.processIntent(EditTodoIntent.Initialize)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(0L , state.id)
            assertEquals("" , state.title)
            assertEquals("" , state.description)
            assertEquals(false , state.isChecked)
            assertEquals(true , state.isInitialized)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun initialized_with_valid_id_loads_todo_from_repository() : Unit = runTest {
        val todoId = 1L
        savedStateHandle = SavedStateHandle(mapOf("todoId" to todoId))
        val todo = Todo(
            id = todoId , title = "Todo" , description = "Description" , isCompleted = false
        )
        coEvery { repository.getTodoById(todoId) } returns todo

        viewModel = EditTodoViewModel(repository , savedStateHandle)
        viewModel.processIntent(EditTodoIntent.Initialize)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(todoId , state.id)
            assertEquals("Todo" , state.title)
            assertEquals("Description" , state.description)
            assertEquals(false , state.isChecked)
            assertEquals(true , state.isInitialized)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun setTitle_updates_title_in_state() : Unit = runTest {
        savedStateHandle = SavedStateHandle(mapOf("todoId" to 0L))
        viewModel = EditTodoViewModel(repository , savedStateHandle)

        viewModel.processIntent(EditTodoIntent.SetTitle("New Title"))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals("New Title" , state.title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun setDescription_updates_description_in_state() : Unit = runTest {
        savedStateHandle = SavedStateHandle(mapOf("todoId" to 0L))
        viewModel = EditTodoViewModel(repository , savedStateHandle)

        viewModel.processIntent(EditTodoIntent.SetDescription("New Description"))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals("New Description" , state.description)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun setChecked_updates_isChecked_in_state() : Unit = runTest {
        savedStateHandle = SavedStateHandle(mapOf("todoId" to 0L))
        viewModel = EditTodoViewModel(repository , savedStateHandle)

        viewModel.processIntent(EditTodoIntent.SetChecked(true))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(true , state.isChecked)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveTodo_saves_new_todo_when_id_is_0L() : Unit = runTest {
        savedStateHandle = SavedStateHandle(mapOf("todoId" to 0L))
        viewModel = EditTodoViewModel(repository , savedStateHandle)
        coEvery { repository.insertTodo(any()) } returns 999L
        viewModel.processIntent(EditTodoIntent.SetTitle("Todo"))
        viewModel.processIntent(EditTodoIntent.SetDescription("Description"))
        viewModel.processIntent(EditTodoIntent.SetChecked(false))

        viewModel.processIntent(EditTodoIntent.SaveTodo)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify {
            repository.insertTodo(match { todo ->
                todo.id == 0L
                todo.title == "Todo"
                todo.description == "Description"
                todo.isCompleted == false
            })
        }
    }

    @Test
    fun saveTodo_update_existing_todo_when_id_not_0L() : Unit = runTest {
        val todoId = 1L
        savedStateHandle = SavedStateHandle(mapOf("todoId" to todoId))
        val mockTodo = Todo(
            id = todoId ,
            title = "Title" ,
            description = "Description" ,
            isCompleted = false
        )

        coEvery { repository.getTodoById(todoId) } returns mockTodo
        coEvery { repository.updateTodo(any()) } returns Unit

        viewModel = EditTodoViewModel(repository , savedStateHandle)
        viewModel.processIntent(EditTodoIntent.Initialize)
        viewModel.processIntent(EditTodoIntent.SetTitle("New Title"))
        viewModel.processIntent(EditTodoIntent.SetChecked(true))
        viewModel.processIntent(EditTodoIntent.SetDescription("New Description"))

        viewModel.processIntent(EditTodoIntent.SaveTodo)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify {
            repository.updateTodo(match { todo ->
                todo.id == todoId &&
                        todo.title == "New Title" &&
                        todo.isCompleted == true &&
                        todo.description == "New Description"
            })
        }
    }
}