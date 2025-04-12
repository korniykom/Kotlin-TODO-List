package com.korniykom.todo_list.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun LoadingScreen(
    modifier : Modifier = Modifier ,
    onLoadingFinished : () -> Unit ,
) {
    val animationCycles = 6
    val animationDuration : Long = 1000
    val boxColors =
        remember { mutableStateListOf(Color.Green , Color.Green , Color.Green , Color.Green) }
    val randomStartBox = remember { Random.nextInt(0 , 4) }

    LaunchedEffect(key1 = true) {
        for (cycle in 0 until animationCycles) {
            val boxIndex = (randomStartBox + cycle) % 4
            boxColors[boxIndex] = Color.Red
            delay(animationDuration)
            boxColors[boxIndex] = Color.Green
        }
        onLoadingFinished()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Column {
                Box(
                    modifier = modifier
                        .size(70.dp)
                        .background(boxColors[1] , RoundedCornerShape(8.dp))
                ) {}
                Box(
                    modifier = modifier
                        .size(70.dp)
                        .background(boxColors[0] , RoundedCornerShape(8.dp))
                ) {}
            }
            Column {
                Box(
                    modifier = modifier
                        .size(70.dp)
                        .background(boxColors[2] , RoundedCornerShape(8.dp))
                ) {}
                Box(
                    modifier = modifier
                        .size(70.dp)
                        .background(boxColors[3] , RoundedCornerShape(8.dp))
                ) {}
            }
        }
    }
}