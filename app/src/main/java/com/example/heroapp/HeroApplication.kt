package com.example.heroapp
import android.app.Application
import androidx.room.Room
import com.example.heroapp.data.room.FavoriteHeroDatabase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class HeroApplication : Application() {

    private lateinit var database: FavoriteHeroDatabase

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        database = Room.databaseBuilder(
            applicationContext,
            FavoriteHeroDatabase::class.java, "hero_database"
        ).build()
    }
}
