package com.korniykom.domain.model

data class Todo(
    val id : Long = 0 ,
    val title : String ,
    val description : String ,
    val isComplete : Boolean = false
)