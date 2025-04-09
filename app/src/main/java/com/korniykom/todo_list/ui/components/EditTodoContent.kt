package com.korniykom.todo_list.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.korniykom.todo_list.ui.viewmodels.mvi.EditTodoState

@Composable
fun EditTodoContent(
    modifier: Modifier = Modifier,
    state: EditTodoState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCheckChange: (Boolean) -> Unit,
    onSaveClicked: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = state.title,
            onValueChange = onTitleChange,
            label = { Text("Title")},
            modifier = modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = state.description,
            onValueChange = onDescriptionChange,
            label = { Text("Description")},
            modifier = modifier.fillMaxWidth(),
            singleLine = true
        )
        Row(
            modifier = modifier.height(48.dp). padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.isChecked,
                onCheckedChange = onCheckChange
            )
            Text(text = " Mark as completed")
            Spacer(modifier = modifier.weight(1f))
            Button(
                onClick = onSaveClicked ,
            ) {
                Text(text = "Save todo")
            }
        }
    }
}