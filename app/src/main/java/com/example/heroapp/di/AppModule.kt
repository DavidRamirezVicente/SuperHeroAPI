package com.example.heroapp.di

import com.example.heroapp.data.RetrofitHeroService
import com.example.heroapp.data.remote.HeroApi
import com.example.heroapp.domain.HeroServices
import com.example.heroapp.repository.HeroRepository
import com.example.heroapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideHeroRepository(
        api: HeroApi
    ) = HeroRepository(api)

    /**
     * Método para proporcionar una instancia de [HeroApi].
     * @return Instancia de [HeroApi] configurada con Retrofit.
     */
    @Singleton
    @Provides
    fun provideHeroApi(): HeroApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(HeroApi::class.java)
    }

    @Singleton
    @Provides
    fun provideHeroServices(api: HeroApi): HeroServices{
        return RetrofitHeroService(api)
    }
}