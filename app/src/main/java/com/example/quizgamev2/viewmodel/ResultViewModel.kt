package com.example.quizgamev2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizgamev2.model.QuizModel
import com.example.quizgamev2.model.ResultModel
import com.example.quizgamev2.repository.ResultRepository

class ResultViewModel: ViewModel(), ResultRepository.OnResultLoad {
    var repository = ResultRepository()

    var currentScore = ResultModel(0,0)
    var observerCurrentScores = MutableLiveData<ResultModel>(currentScore)

    fun readScores() {
        repository.readScores()
    }

    fun resetAnswer(){
        currentScore.correct = 0
        currentScore.wrong = 0
        observerCurrentScores.postValue(currentScore)
    }

    fun checkAnswer(checked: Boolean,num_q: Int){
        if (checked){
            currentScore.correct = currentScore.correct?.plus(num_q)
            Log.e("mycodeisblocking",currentScore.correct.toString())
        }else{
            currentScore.wrong = currentScore.wrong?.plus(num_q)
            Log.e("mycodeisblocking",currentScore.wrong.toString())
        }
        observerCurrentScores.postValue(currentScore)
    }

    fun finish(num_q: Int){
        currentScore.wrong = num_q - currentScore.correct!!
        observerCurrentScores.postValue(currentScore)
    }

    fun observerCurrentScores(): LiveData<ResultModel> {
        return observerCurrentScores
    }

    override fun OnResultLoad(score: ResultModel) {
        observerCurrentScores.value = score
    }

    fun writeScores() {
        repository.writeScores(currentScore)
    }

    fun writeScores(score: ResultModel) {
        repository.writeScores(score)
    }
}