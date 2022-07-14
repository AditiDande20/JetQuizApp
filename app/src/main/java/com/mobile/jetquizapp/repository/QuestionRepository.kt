package com.mobile.jetquizapp.repository

import com.mobile.jetquizapp.data.DataOrException
import com.mobile.jetquizapp.model.QuestionItem
import com.mobile.jetquizapp.network.QuestionsAPI
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val questionsAPI: QuestionsAPI) {
    private val dataOrException =
        DataOrException<ArrayList<QuestionItem>,
                Boolean,
                Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = questionsAPI.getAllQuestions()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (exception: Exception) {
            dataOrException.e = exception
        }
        return dataOrException
    }
}