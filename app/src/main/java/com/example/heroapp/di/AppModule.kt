package com.example.heroapp.di

import com.example.heroapp.data.RetrofitHeroService
import com.example.heroapp.data.remote.HeroApi
import com.example.heroapp.data.room.FavoriteHeroDatabase
import com.example.heroapp.domain.HeroServices
import com.example.heroapp.repository.HeroRepository
import com.example.heroapp.repository.VSRepository
import com.example.heroapp.util.Constants.BASE_URL
import com.example.heroapp.util.StateMachine
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Método para proporcionar una instancia de [HeroRepository].
     * @param api Instancia de [HeroApi] necesaria para inicializar [HeroRepository].
     * @return Instancia de [HeroRepository].
     */

    @Singleton
    @Provides
    fun provideHeroRepository(heroService: HeroServices, db: FavoriteHeroDatabase): HeroRepository {
        return HeroRepository(heroService, db)
    }

    @Singleton
    @Provides
    fun provideVSRepository(heroService: HeroServices, db: FavoriteHeroDatabase): VSRepository {
        return VSRepository(heroService,db)
    }

    @Provides
    fun provideStateMachine(): StateMachine{
        return StateMachine()
    }

    /**
     * Método para proporcionar una instancia de [HeroApi].
     * @return Instancia de [HeroApi] configurada con Retrofit.
     */
    @Singleton
    @Provides
    fun provideHeroApi(): HeroApi {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(HeroApi::class.java)
    }

    @Singleton
    @Provides
    fun provideHeroServices(api: HeroApi): HeroServices{
        return RetrofitHeroService(api)
    }

}