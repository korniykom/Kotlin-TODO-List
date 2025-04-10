package com.korniykom.todo_list.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.korniykom.todo_list.ui.components.EditTodoContent
import com.korniykom.todo_list.ui.viewmodels.mvi.EditTodoIntent
import com.korniykom.todo_list.ui.viewmodels.mvi.EditTodoViewModel

@Composable
fun EditTodoScreen(
    viewModel : EditTodoViewModel = hiltViewModel() , onSave : () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.processIntent(EditTodoIntent.Initialize)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp) , verticalArrangement = Arrangement.Center
    ) {
        EditTodoContent(
            state = state ,
            onTitleChange = { title -> viewModel.processIntent(EditTodoIntent.SetTitle(title)) } ,
            onDescriptionChange = { description ->
                viewModel.processIntent(
                    EditTodoIntent.SetDescription(
                        description
                    )
                )
            } ,
            onCheckChange = { checked -> viewModel.processIntent(EditTodoIntent.SetChecked(checked)) } ,
            onSaveClicked = {
                viewModel.processIntent(EditTodoIntent.SaveTodo)
                onSave()
            })
    }
}