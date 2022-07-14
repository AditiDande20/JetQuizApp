package com.mobile.jetquizapp.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobile.jetquizapp.component.Questions

@Composable
fun QuizHome(questionViewModel: QuestionViewModel = hiltViewModel()) {
    Questions(questionViewModel)
}