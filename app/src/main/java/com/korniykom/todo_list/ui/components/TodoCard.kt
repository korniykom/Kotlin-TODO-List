package com.korniykom.todo_list.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun SwipeableTodoCard(
    modifier : Modifier = Modifier ,
    checked : Boolean = false ,
    onCheckChange : (Boolean) -> Unit = {} ,
    title : String,
    onDelete : () -> Unit ,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        })

    SwipeToDismissBox(
        state = dismissState ,
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                else -> Color.Transparent
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = color
                )
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .padding(start = 4.dp , end = 16.dp) ,

                    horizontalArrangement = Arrangement.SpaceBetween ,
                    verticalAlignment = Alignment.CenterVertically ,
                ) {}
            }
        } ,
        enableDismissFromEndToStart = true ,
    ) {
        TodoCard(
            checked = checked ,
            onCheckChange = onCheckChange ,
            title = title
        )
    }
}

@Composable
fun TodoCard(
    modifier : Modifier = Modifier ,
    checked : Boolean ,
    onCheckChange : (Boolean) -> Unit ,
    title : String
) {
    Card {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(start = 4.dp , end = 16.dp) ,

            horizontalArrangement = Arrangement.SpaceBetween ,
            verticalAlignment = Alignment.CenterVertically ,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically ,
            ) {
                Checkbox(
                    checked = checked , onCheckedChange = onCheckChange
                )
                Text(
                    text = title ,
                    style = MaterialTheme.typography.titleLarge ,
                    textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            Icon(imageVector = Icons.Default.Edit , contentDescription = "Edit Todo")
        }
    }
}