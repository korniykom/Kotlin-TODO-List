package com.korniykom.todo_list.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.korniykom.domain.model.Todo
import com.korniykom.todo_list.ui.viewmodels.mvvm.TodosViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodosScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController : NavController
    private lateinit var viewModel : TodosViewModel
    private val testTodos = listOf(
        Todo(id = 1L , title = "Todo1" , description = "Description1" , isCompleted = false) ,
        Todo(id = 2L , title = "Todo2" , description = "Description2" , isCompleted = true)
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
                viewModel = viewModel ,
                onEdit = { todoId -> navController.navigate("edit/$todoId") } ,
                onSave = { navController.navigate("edit/0") })
        }
    }

    @Test
    fun todosScreen_displays_items() {
        composeTestRule.onNodeWithText("Todo1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Todo2").assertIsDisplayed()
        composeTestRule.onNodeWithText("IP: 1.1.1.1").assertIsDisplayed()
    }

    @Test
    fun todosScreen_navigate_to_EditScreen_when_clicked_FAB() {
        composeTestRule.onNodeWithContentDescription("Add Todo Icon").performClick()

        verify { navController.navigate("edit/0") }
    }

    @Test
    fun todosScreen_deletes_item_when_swiped_correct() {
        composeTestRule.onNodeWithText("Todo1").performTouchInput { swipeLeft() }

        verify { viewModel.onTodoDelete(1L) }
    }

    @Test
    fun todosScreen_edit_item_navigates_to_EditTodoScreen() {
        composeTestRule.onNodeWithContentDescription("Edit Todo Todo1").performClick()

        verify { navController.navigate("edit/1") }
    }
}