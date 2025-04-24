package com.korniykom.todo_list.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.korniykom.domain.model.Todo
import com.korniykom.todo_list.ui.viewmodels.mvvm.TodosViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodosScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: NavController
    private lateinit var viewModel: TodosViewModel
    private val testTodos = listOf(
        Todo(id = 1L, title = "Todo1", description = "Description1", isCompleted = false),
        Todo(id = 2L, title = "Todo2", description = "Description2", isCompleted = true)
    )

    @Before
    fun setup() {
        navController = mockk(relaxed = true)
        viewModel = mockk(relaxed = true)

        val todosFlow = MutableStateFlow(testTodos)
        val publicIpFlow = MutableStateFlow("1.1.1.1")

        every { viewModel.todos } returns todosFlow
        every { viewModel.publicIp } returns publicIpFlow

        composeTestRule.setContent {
            TodosScreen(
                viewModel = viewModel,
                onEdit = { todoId -> navController.navigate("edit/$todoId") },
                onSave = { navController.navigate("edit/0") }
            )
        }
    }
    @Test
    fun todosScreen_displaysCorrectItems() {
        composeTestRule.onNodeWithText("Todo1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Todo2").assertIsDisplayed()
        composeTestRule.onNodeWithText("IP: 1.1.1.1").assertIsDisplayed()
    }
}