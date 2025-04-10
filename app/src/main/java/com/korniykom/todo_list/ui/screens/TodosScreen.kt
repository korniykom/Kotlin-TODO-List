package com.korniykom.todo_list.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.korniykom.todo_list.ui.components.SwipeableTodoCard
import com.korniykom.todo_list.ui.viewmodels.mvvm.TodosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodosScreen(
    viewModel : TodosViewModel = hiltViewModel(),
    onEdit: (Long) -> Unit,
    onSave: () -> Unit
) {
    val publicIp by viewModel.publicIp.collectAsState()
    val todos by viewModel.todos.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "IP: $publicIp" , fontWeight = FontWeight.Bold
                )
            })
    } , floatingActionButton = {
        FloatingActionButton(onClick = onSave) {
            Icon(imageVector = Icons.Default.Add , contentDescription = "Add Todo Icon")
        }
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn {
                items(
                    items = todos , key = { it.id }) { item ->
                    Box(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        SwipeableTodoCard(
                            checked = item.isCompleted ,
                            title = item.title ,
                            onDelete = { viewModel.onTodoDelete(item.id) } ,
                            onEdit = { onEdit(item.id) } ,
                            onCheckChange = { viewModel.onToggleChecked(item)}
                        )
                    }
                }
            }
        }
    }
}