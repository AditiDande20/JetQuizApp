package com.mobile.jetquizapp.di

import com.mobile.jetquizapp.network.QuestionsAPI
import com.mobile.jetquizapp.repository.QuestionRepository
import com.mobile.jetquizapp.util.Constants
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

    @Provides
    @Singleton
    fun provideQuestionRepository(questionsAPI: QuestionsAPI) = QuestionRepository(questionsAPI)

    @Provides
    @Singleton
    fun provideQuestions() : QuestionsAPI{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionsAPI::class.java)
    }
}