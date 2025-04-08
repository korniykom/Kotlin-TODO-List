package com.korniykom.data.di

import android.content.Context
import androidx.room.Room
import com.korniykom.data.local.TodoDao
import com.korniykom.data.local.TodoDatabase
import com.korniykom.data.remote.NetworkApi
import com.korniykom.data.repository.TodoRepositoryImpl
import com.korniykom.domain.repository.NetworkRepository
import com.korniykom.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoDao(database : TodoDatabase): TodoDao {
        return database.todoDao()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Provides
    @Singleton
    fun provideNetworkApi(httpClient : HttpClient) : NetworkApi {
        return NetworkApi(httpClient)
    }

    @Provides
    @Singleton
    fun provideTodoRepository(todoDao : TodoDao): TodoRepository {
        return TodoRepositoryImpl(todoDao)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(networkApi: NetworkApi): NetworkRepository {
        return NetworkRepositoryImpl(networkApi)
    }
}