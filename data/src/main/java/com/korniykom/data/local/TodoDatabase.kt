package com.korniykom.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.korniykom.data.local.entity.TodoEntity

@Database(entities = [TodoEntity::class] , version = 1 , exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao() : TodoDao
}