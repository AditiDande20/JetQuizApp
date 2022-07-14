package com.mobile.jetquizapp.network

import com.mobile.jetquizapp.model.Question
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionsAPI {

    @GET("world.json")
    suspend fun getAllQuestions() : Question
}