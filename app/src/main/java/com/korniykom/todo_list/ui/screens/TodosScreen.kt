package com.korniykom.todo_list.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.korniykom.todo_list.ui.viewmodels.MVVM.TodosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodosScreen(
    viewModel : TodosViewModel = hiltViewModel()
) {
    val publicIp by viewModel.publicIp.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "IP: $publicIp" , fontWeight = FontWeight.Bold
                )
            })
    } , floatingActionButton = {
        FloatingActionButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Add , contentDescription = "Add Todo Icon")
        }
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text("test")
        }
    }
}