package com.example.heroapp.repository

import com.example.heroapp.data.remote.responses.Hero
import com.example.heroapp.data.remote.responses.HeroApi
import com.example.heroapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class HeroRepository @Inject constructor(
    private val api: HeroApi
){
   /* suspend fun getHero(heroName: String): Resource<Hero> {
        val response = try {
            api.getHeroInfo(heroName)
        }catch (e: Exception) {
            return Resource.Error("An unkown error occured")
        }
        return  Resource.Success(response)
    }*/
   suspend fun getHeroes(heroName: String): Result<List<Hero>> {
       return try {
           val response = api.getHeroInfo(heroName)
           Result.success(listOf(response))
       } catch (e: Exception) {
           Result.failure(e)
       }
   }
}