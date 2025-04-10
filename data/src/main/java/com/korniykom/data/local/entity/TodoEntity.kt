package com.korniykom.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.korniykom.domain.model.Todo

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id : Long = 0 ,
    val title : String ,
    val description : String ,
    val isCompleted : Boolean
) {
    fun toDomainModel() : Todo {
        return Todo(
            id = id , title = title , description = description , isCompleted = isCompleted
        )
    }

    companion object {
        fun fromDomainModel(todo : Todo) : TodoEntity {
            return TodoEntity(
                id = todo.id ,
                title = todo.title ,
                description = todo.description ,
                isCompleted = todo.isCompleted
            )
        }
    }
}