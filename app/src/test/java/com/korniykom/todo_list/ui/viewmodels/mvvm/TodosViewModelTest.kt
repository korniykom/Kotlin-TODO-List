package com.korniykom.todo_list.ui.viewmodels.mvvm

import app.cash.turbine.test
import com.korniykom.data.remote.NetworkApi
import com.korniykom.domain.model.Todo
import com.korniykom.domain.repository.TodoRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


class TodosViewModelTest {
    @MockK
    private lateinit var repository : TodoRepository

    @MockK
    private lateinit var networkApi : NetworkApi

    private lateinit var viewModel : TodosViewModel
    private var testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        val todos = MutableStateFlow(emptyList<Todo>())
        every { repository.getTodos() } returns todos
        coEvery { networkApi.getPublicIp() } returns "1.1.1.1"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun during_init_viewModel_loads_todos_from_repository() : Unit = runTest {
        val mockTodos = listOf(
            Todo(
                id = 1 , title = "Todo1" , description = "Todo Description" , isCompleted = false
            ) , Todo(
                id = 2 , title = "Todo2" , description = "Todo Description" , isCompleted = true
            )
        )
        val todoFlow = MutableStateFlow(mockTodos)
        every { repository.getTodos() } returns todoFlow

        viewModel = TodosViewModel(repository , networkApi)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.todos.test {
            assertEquals(mockTodos , awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun during_init_viewModel_fetch_IP_address() : Unit = runTest {
        val expectedIp = "1.1.1.1"
        coEvery { networkApi.getPublicIp() } returns expectedIp

        viewModel = TodosViewModel(repository , networkApi)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.publicIp.test {
            assertEquals(expectedIp , awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun during_init_trows_error_if_fetching_IP_fails() : Unit = runTest {
        coEvery { networkApi.getPublicIp() } throws RuntimeException("Network error")

        viewModel = TodosViewModel(repository , networkApi)
        testDispatcher.scheduler.runCurrent()

        viewModel.publicIp.test {
            assertEquals("Unable to fetch IP address" , awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onTodoDelete_deletes_todo_from_repository() : Unit = runTest {
        viewModel = TodosViewModel(repository, networkApi)
        val id = 1L
        coEvery { repository.deleteTodo(any()) } returns Unit

        viewModel.onTodoDelete(id)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.deleteTodo(id) }
    }

    @Test
    fun onToggleChecked_toggles_completition_of_the_task() : Unit = runTest {
        viewModel = TodosViewModel(repository, networkApi)
        val todo = Todo(1, "Todo", "Descriptioon", false )
        val expectedUpdatedTodo = todo.copy(isCompleted = true)
        coEvery { repository.updateTodo(any()) } returns Unit

        viewModel.onToggleChecked(todo)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.updateTodo(expectedUpdatedTodo) }
    }
}