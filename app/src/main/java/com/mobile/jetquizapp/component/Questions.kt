package com.mobile.jetquizapp.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.jetquizapp.model.QuestionItem
import com.mobile.jetquizapp.screen.QuestionViewModel
import com.mobile.jetquizapp.util.AppColors

@Composable
fun Questions(questionViewModel: QuestionViewModel) {
    val questions = questionViewModel.data.value.data?.toMutableList()
    val questionIndex = remember {
        mutableStateOf(0)
    }
    if (questionViewModel.data.value.loading == true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = AppColors.mDarkPurple),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.height(200.dp))
        }
    } else {
        val question = try {
            questions?.get(questionIndex.value)
        } catch (e: Exception) {
            null
        }

        if (questions != null) {
            if (question != null) {
                QuestionsDisplay(questionItem = question, questionIndex, questionViewModel) {
                    questionIndex.value = questionIndex.value + 1
                }
            }
        }
    }
}

@Composable
fun QuestionsDisplay(
    questionItem: QuestionItem,
    questionIndex: MutableState<Int>,
    questionViewModel: QuestionViewModel,
    onNextClicked: (Int) -> Unit = {}
) {

    val choicesState = remember(questionItem) {
        questionItem.choices.toMutableList()
    }
    val answerState = remember(questionItem) {
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember(questionItem) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswerState: (Int) -> Unit = remember(questionItem) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == questionItem.answer
        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            if (questionIndex.value > 3) ShowProgress(questionIndex.value + 1)
            QuestionTracker(
                counter = questionIndex.value + 1,
                outOf = questionViewModel.data.value.data!!.size
            )
            ShowDottedLine(pathEffect)
            Text(
                text = questionItem.question,
                color = AppColors.mOffWhite,
                lineHeight = 22.sp,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.Start)
                    .fillMaxHeight(0.3f)
            )

            // Choices
            choicesState.forEachIndexed { index, answerText ->
                Row(
                    modifier = Modifier
                        .padding(3.dp)
                        .height(45.dp)
                        .fillMaxWidth()
                        .border(
                            width = 2.dp, brush = Brush.linearGradient(
                                colors = listOf(AppColors.mOffDarkPurple, AppColors.mOffDarkPurple)
                            ), shape = RoundedCornerShape(corner = CornerSize(15.dp))
                        )
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.Transparent),
                    verticalAlignment = CenterVertically
                ) {

                    RadioButton(
                        selected = (answerState.value == index), onClick = {
                            updateAnswerState(index)
                        },
                        modifier = Modifier.padding(start = 16.dp),
                        colors = RadioButtonDefaults.colors(
                            selectedColor =
                            if (correctAnswerState.value == true && index == answerState.value) {
                                Color.Green.copy(0.2f)
                            } else {
                                Color.Red.copy(0.2f)
                            }
                        )
                    )

                    val annotatedString = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Light,
                                fontSize = 17.sp,
                                color = if (correctAnswerState.value == true && index == answerState.value) {
                                    Color.Green
                                } else if (correctAnswerState.value == false && index == answerState.value) {
                                    Color.Red
                                } else {
                                    AppColors.mOffWhite
                                }
                            )
                        ) {
                            append(answerText)
                        }
                    }
                    Text(text = annotatedString, modifier = Modifier.padding(6.dp))
                }
            }
            Button(
                onClick = { onNextClicked(questionIndex.value) },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(CenterHorizontally),
                shape = RoundedCornerShape(34.dp),
                colors = buttonColors(backgroundColor = AppColors.mLightBlue)
            ) {
                Text(
                    text = "Next",
                    modifier = Modifier.padding(4.dp),
                    color = AppColors.mOffWhite,
                    fontSize = 17.sp
                )
            }
        }

    }
}


@Composable
@Preview
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
    Text(
        buildAnnotatedString {
            withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
                withStyle(
                    style = SpanStyle(
                        color = AppColors.mLightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp
                    )
                ) {
                    append("Question $counter/")
                    withStyle(
                        style = SpanStyle(
                            color = AppColors.mLightGray,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    ) {
                        append("$outOf")
                    }
                }
            }
        },
        modifier = Modifier.padding(12.dp)
    )
}

@Composable
fun ShowDottedLine(pathEffect: PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = 0f),
            pathEffect = pathEffect
        )
    }
}

@Preview
@Composable
fun ShowProgress(score: Int = 10) {
    val gradient = Brush.linearGradient(colors = listOf(Color(0XFFF95075), Color(0XFFBE6BE5)))
    val progressScore by remember(score) {
        mutableStateOf(score * 0.005f)
    }

    Row(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.mLightPurple,
                        AppColors.mLightPurple
                    )
                ),
                shape = RoundedCornerShape(corner = CornerSize(34.dp))
            )
            .clip(RoundedCornerShape(50.dp))
            .background(Color.Transparent),
        verticalAlignment = CenterVertically
    ) {
        Button(contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progressScore)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            ),
            onClick = { }) {
            Text(
                text = (score * 10).toString(),
                textAlign = TextAlign.Center,
                color = AppColors.mOffWhite,
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.87f)
            )

        }

    }
}