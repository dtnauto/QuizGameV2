package com.example.quizgamev2.view

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgamev2.R
import com.example.quizgamev2.model.ResultModel
import com.example.quizgamev2.viewmodel.QuizViewModel
import com.example.quizgamev2.viewmodel.ResultViewModel
import kotlin.String
import kotlin.Int
import kotlin.Long


class QuizFragment : Fragment() {

    lateinit var quizViewModel: QuizViewModel
    lateinit var resultViewModel: ResultViewModel
    lateinit var navController: NavController

    lateinit var txt_question: TextView
    lateinit var txt_option1: TextView
    lateinit var txt_option2: TextView
    lateinit var txt_option3: TextView
    lateinit var txt_option4: TextView
    lateinit var txt_timer: TextView
    lateinit var txt_correct: TextView
    lateinit var txt_wrong: TextView

    lateinit var progressBar: ProgressBar
    lateinit var layout_quiz: RelativeLayout

    lateinit var btn_next: Button
    lateinit var btn_finish: Button

    var timer: CountDownTimer? = null
    var totalQuestions = 0
    var currentQuestion = 0

    var answer: String? = ""
    var selected: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        resultViewModel = ViewModelProvider(requireActivity())[ResultViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        txt_question=view.findViewById(R.id.txt_question)
        txt_option1=view.findViewById(R.id.txt_option1)
        txt_option2=view.findViewById(R.id.txt_option2)
        txt_option3=view.findViewById(R.id.txt_option3)
        txt_option4=view.findViewById(R.id.txt_option4)
        txt_timer=view.findViewById(R.id.txt_timer)
        txt_correct=view.findViewById(R.id.txt_correct)
        txt_wrong=view.findViewById(R.id.txt_wrong)

        progressBar=view.findViewById(R.id.progressBar)
        layout_quiz=view.findViewById(R.id.layout_quiz)

        btn_next=view.findViewById(R.id.btn_next)
        btn_finish=view.findViewById(R.id.btn_finish)

        addEvent()
    }

    private fun addEvent() {
        readQuestions()
        loadQuestion()
        loadScore()
        btnNext()
        btnFinish()
    }

    private fun readQuestions() {
        quizViewModel.readQuestions()
    }
    private fun loadQuestion() {
        quizViewModel.observerCurrentListQuestions().observe(viewLifecycleOwner){
                quizModel ->

            selected="" // resetanswer

            timer?.cancel()
            timer = object : CountDownTimer(5L * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    txt_timer.text = "Timer: " + (millisUntilFinished / 1000)
                }
                override fun onFinish() {
                    txt_question.setText("Times Up !! No answer selected")
                    enableOptions(false)
                    checkAnswer()
                }
            }.start()

            totalQuestions = quizViewModel.repository.listQuestions.size
            txt_question.text = quizModel[currentQuestion].q
            txt_option1.text = quizModel[currentQuestion].a
            txt_option2.text = quizModel[currentQuestion].b
            txt_option3.text = quizModel[currentQuestion].c
            txt_option4.text = quizModel[currentQuestion].d
            answer = quizModel[currentQuestion].answer
            resetBackground()
            enableOptions(true)
            if (currentQuestion==0){
                progressBar.visibility=View.GONE
                layout_quiz.visibility=View.VISIBLE
            }
        }
    }

    private fun loadScore() {
        resultViewModel.observerCurrentScores().observe(viewLifecycleOwner) {
            txt_correct.text = "Correct Answer: " + it.correct
            txt_wrong.text = "Wrong Answer: " + it.wrong
        }
    }

    private fun enableOptions(state: Boolean) {
        txt_option1.isEnabled = state
        txt_option2.isEnabled = state
        txt_option3.isEnabled = state
        txt_option4.isEnabled = state
        txtOption()
    }

    private fun txtOption() {
        setOptionClickListener(txt_option1, "a")
        setOptionClickListener(txt_option2, "b")
        setOptionClickListener(txt_option3, "c")
        setOptionClickListener(txt_option4, "d")
    }
    private fun setOptionClickListener(option: TextView, selectedAnswer: String) {
        option.setOnClickListener {
            timer?.cancel()
            selected = selectedAnswer
            enableOptions(false)
            checkAnswer()
        }
    }
    private fun checkAnswer() {
        if (selected==""){
            resultViewModel.checkAnswer(false,1)
        }else{
            if (selected == answer) {
                resultViewModel.checkAnswer(true,1)
                // Handle correct answer UI
                when (selected) {
                    "a" -> setBackground(txt_option1, true)
                    "b" -> setBackground(txt_option2, true)
                    "c" -> setBackground(txt_option3, true)
                    "d" -> setBackground(txt_option4, true)
                }
            } else {
                resultViewModel.checkAnswer(false,1)
                when (selected) {
                    "a" -> setBackground(txt_option1, false)
                    "b" -> setBackground(txt_option2, false)
                    "c" -> setBackground(txt_option3, false)
                    "d" -> setBackground(txt_option4, false)
                }
            }
            when (answer) {
                "a" -> setBackground(txt_option1, true)
                "b" -> setBackground(txt_option2, true)
                "c" -> setBackground(txt_option3, true)
                "d" -> setBackground(txt_option4, true)
            }
        }
        selected="true"
    }

    private fun setBackground(txt_color : TextView , isCorrect : Boolean) {
        if (isCorrect) {
            txt_color.setBackgroundColor(Color.GREEN)
        }
        else {
            txt_color.setBackgroundColor(Color.RED)
        }
    }
    private fun resetBackground() {
        txt_option1.setBackgroundColor(Color.WHITE)
        txt_option2.setBackgroundColor(Color.WHITE)
        txt_option3.setBackgroundColor(Color.WHITE)
        txt_option4.setBackgroundColor(Color.WHITE)
    }

    private fun btnNext() {
        btn_next.setOnClickListener{
            currentQuestion++
            if (currentQuestion < totalQuestions){
                if (selected!="true") checkAnswer()
                loadQuestion()
            }else{
                showResultDialog()
            }
        }
    }

    private fun btnFinish() {
        btn_finish.setOnClickListener{
            showResultDialog()
        }
    }

    private fun showResultDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Quiz Game")
        alertDialogBuilder.setMessage("Congratulations!!! \n You have answered all the questions. Do you want to see the result?")

        alertDialogBuilder.setNegativeButton("PLAY AGAIN") { _, _ ->
            navController.navigate(R.id.quizFragment)
            resultViewModel.resetAnswer()
        }.setPositiveButton("SEE RESULT") { _, _ ->
            resultViewModel.writeScores()
            timer?.cancel()
            resultViewModel.finish(totalQuestions)
            navController.navigate(R.id.action_quizFragment_to_resultFragment)
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()
    }
}