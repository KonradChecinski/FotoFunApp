package com.example.fotofun.di

import android.app.Application
import androidx.room.Room
import com.example.fotofun.data.*
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
    fun provideFotoFunDatabase(app: Application): FotoFunDatabase {
        return Room.databaseBuilder(
            app,
            FotoFunDatabase::class.java,
            "todo_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFotoFunRepository(db: FotoFunDatabase): FotoFunRepository {
        return FotoFunRepositoryImpl(db.dao)
    }
}