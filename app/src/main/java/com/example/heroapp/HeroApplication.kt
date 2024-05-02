package com.example.heroapp

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.heroapp.data.room.FavoriteHeroDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton


@HiltAndroidApp
class HeroApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavoriteHeroDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteHeroDatabase::class.java, "hero_database"
        ).build()
    }
}