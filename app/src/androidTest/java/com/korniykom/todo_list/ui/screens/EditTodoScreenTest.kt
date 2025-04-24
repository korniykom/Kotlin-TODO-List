package com.korniykom.todo_list.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.korniykom.todo_list.ui.viewmodels.mvi.EditTodoIntent
import com.korniykom.todo_list.ui.viewmodels.mvi.EditTodoState
import com.korniykom.todo_list.ui.viewmodels.mvi.EditTodoViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditTodoScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel : EditTodoViewModel
    private lateinit var stateFlow : MutableStateFlow<EditTodoState>
    private lateinit var onSaveCallback : () -> Unit

    @Before
    fun setup() {
        val initialState = EditTodoState(
            id = 1L ,
            title = "Test todo" ,
            description = "Description" ,
            isChecked = true ,
            isInitialized = true
        )
        stateFlow = MutableStateFlow(initialState)
        viewModel = mockk(relaxed = true)
        every { viewModel.state } returns stateFlow
        onSaveCallback = mockk(relaxed = true)

        composeTestRule.setContent {
            EditTodoScreen(
                viewModel = viewModel ,
                onSave = onSaveCallback
            )
        }
    }

    @Test
    fun editTodoScreen_initialized_correct() {
        verify { viewModel.processIntent(EditTodoIntent.Initialize) }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Test todo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    @Test
    fun editTodoScreen_save_todo_button_is_pressed() {
        composeTestRule.onNodeWithText("Save todo").performClick()

        verify { viewModel.processIntent(EditTodoIntent.SaveTodo) }
        verify { onSaveCallback() }
    }
}