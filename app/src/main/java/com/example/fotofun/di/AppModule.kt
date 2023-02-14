package com.example.fotofun.di

import android.app.Application
import androidx.room.Room
import com.example.fotofun.data.AssistantDatabase
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.data.AssistantRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFotoFunDatabase(app: Application): AssistantDatabase {
        return Room.databaseBuilder(
            app,
            AssistantDatabase::class.java,
            "todo_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFotoFunRepository(db: AssistantDatabase): AssistantRepository {
        return AssistantRepositoryImpl(db.dao)
    }
}