package com.korniykom.todo_list.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.korniykom.todo_list.ui.viewmodels.mvi.EditTodoIntent
import com.korniykom.todo_list.ui.viewmodels.mvi.EditTodoState
import com.korniykom.todo_list.ui.viewmodels.mvi.EditTodoViewModel
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimplifiedEditTodoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: EditTodoViewModel
    private lateinit var stateFlow: MutableStateFlow<EditTodoState>
    private lateinit var onSaveCallback: () -> Unit

    @Before
    fun setup() {
        // Create initial state
        val initialState = EditTodoState(
            id = 1L,
            title = "Test Todo",
            description = "Test Description",

        )

        // Setup state flow
        stateFlow = MutableStateFlow(initialState)

        // Create mock ViewModel
        viewModel = mockk(relaxed = true)
        every { viewModel.state } returns stateFlow

        // Create a callback for onSave
        onSaveCallback = mockk(relaxed = true)

        // Set the content for testing
        composeTestRule.setContent {
            EditTodoScreen(
                viewModel = viewModel,
                onSave = onSaveCallback
            )
        }
    }

    @Test
    fun editTodoScreen_initializesCorrectly() {
        // Verify Initialize intent is processed when screen is launched
        verify { viewModel.processIntent(EditTodoIntent.Initialize) }

        // Verify the initial state is displayed
        composeTestRule.onNodeWithText("Test Todo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Description").assertIsDisplayed()
    }

}