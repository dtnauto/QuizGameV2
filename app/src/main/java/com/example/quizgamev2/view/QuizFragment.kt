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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgamev2.R
import com.example.quizgamev2.databinding.FragmentQuizBinding
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

//    lateinit var txt_question: TextView
//    lateinit var txt_option1: TextView
//    lateinit var txt_option2: TextView
//    lateinit var txt_option3: TextView
//    lateinit var txt_option4: TextView
//    lateinit var txt_timer: TextView
//    lateinit var txt_correct: TextView
//    lateinit var txt_wrong: TextView
//
//    lateinit var progressBar: ProgressBar
//    lateinit var layout_quiz: RelativeLayout
//
//    lateinit var btn_next: Button
//    lateinit var btn_finish: Button

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

    lateinit var fragmentQuizBinding: FragmentQuizBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentQuizBinding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_quiz, container, false)
        return fragmentQuizBinding.root
//        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

//        txt_question=view.findViewById(R.id.txt_question)
//        txt_option1=view.findViewById(R.id.txt_option1)
//        txt_option2=view.findViewById(R.id.txt_option2)
//        txt_option3=view.findViewById(R.id.txt_option3)
//        txt_option4=view.findViewById(R.id.txt_option4)
//        txt_timer=view.findViewById(R.id.txt_timer)
//        txt_correct=view.findViewById(R.id.txt_correct)
//        txt_wrong=view.findViewById(R.id.txt_wrong)
//
//        progressBar=view.findViewById(R.id.progressBar)
//        layout_quiz=view.findViewById(R.id.layout_quiz)
//
//        btn_next=view.findViewById(R.id.btn_next)
//        btn_finish=view.findViewById(R.id.btn_finish)

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
        quizViewModel.currentListQuestions.observe(viewLifecycleOwner){
                quizModel ->

            selected="" // resetanswer

            timer?.cancel()
            timer = object : CountDownTimer(5L * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    fragmentQuizBinding.txtTimer.text = "Timer: " + (millisUntilFinished / 1000)
                }
                override fun onFinish() {
                    fragmentQuizBinding.txtQuestion.setText("Times Up !! No answer selected")
                    enableOptions(false)
                    checkAnswer()
                }
            }.start()

            totalQuestions = quizModel.size
            fragmentQuizBinding.txtQuestion.text = quizModel[currentQuestion].q
            fragmentQuizBinding.txtOption1.text = quizModel[currentQuestion].a
            fragmentQuizBinding.txtOption2.text = quizModel[currentQuestion].b
            fragmentQuizBinding.txtOption3.text = quizModel[currentQuestion].c
            fragmentQuizBinding.txtOption4.text = quizModel[currentQuestion].d
            answer = quizModel[currentQuestion].answer
            resetBackground()
            enableOptions(true)
            if (currentQuestion==0){
                fragmentQuizBinding.progressBar.visibility=View.GONE
                fragmentQuizBinding.layoutQuiz.visibility=View.VISIBLE
            }
        }
    }

    private fun loadScore() {
        resultViewModel.currentScoreLiveData.observe(viewLifecycleOwner) {
            fragmentQuizBinding.txtCorrect.text = "Correct Answer: " + it.correct
            fragmentQuizBinding.txtWrong.text = "Wrong Answer: " + it.wrong
        }
    }

    private fun enableOptions(state: Boolean) {
        fragmentQuizBinding.txtOption1.isEnabled = state
        fragmentQuizBinding.txtOption2.isEnabled = state
        fragmentQuizBinding.txtOption3.isEnabled = state
        fragmentQuizBinding.txtOption4.isEnabled = state
        txtOption()
    }

    private fun txtOption() {
        setOptionClickListener(fragmentQuizBinding.txtOption1, "a")
        setOptionClickListener(fragmentQuizBinding.txtOption2, "b")
        setOptionClickListener(fragmentQuizBinding.txtOption3, "c")
        setOptionClickListener(fragmentQuizBinding.txtOption4, "d")
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
                    "a" -> setBackground(fragmentQuizBinding.txtOption1, true)
                    "b" -> setBackground(fragmentQuizBinding.txtOption2, true)
                    "c" -> setBackground(fragmentQuizBinding.txtOption3, true)
                    "d" -> setBackground(fragmentQuizBinding.txtOption4, true)
                }
            } else {
                resultViewModel.checkAnswer(false,1)
                when (selected) {
                    "a" -> setBackground(fragmentQuizBinding.txtOption1, false)
                    "b" -> setBackground(fragmentQuizBinding.txtOption2, false)
                    "c" -> setBackground(fragmentQuizBinding.txtOption3, false)
                    "d" -> setBackground(fragmentQuizBinding.txtOption4, false)
                }
            }
            when (answer) {
                "a" -> setBackground(fragmentQuizBinding.txtOption1, true)
                "b" -> setBackground(fragmentQuizBinding.txtOption2, true)
                "c" -> setBackground(fragmentQuizBinding.txtOption3, true)
                "d" -> setBackground(fragmentQuizBinding.txtOption4, true)
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
        fragmentQuizBinding.txtOption1.setBackgroundColor(Color.WHITE)
        fragmentQuizBinding.txtOption2.setBackgroundColor(Color.WHITE)
        fragmentQuizBinding.txtOption3.setBackgroundColor(Color.WHITE)
        fragmentQuizBinding.txtOption4.setBackgroundColor(Color.WHITE)
    }

    private fun btnNext() {
        fragmentQuizBinding.btnNext.setOnClickListener{
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
        fragmentQuizBinding.btnFinish.setOnClickListener{
            showResultDialog()
        }
    }

    private fun showResultDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        timer?.cancel()
        alertDialogBuilder.setTitle("Quiz Game")
        alertDialogBuilder.setMessage("Congratulations!!! \n You have answered all the questions. Do you want to see the result?")
        alertDialogBuilder.setNegativeButton("PLAY AGAIN") { _, _ ->
            navController.navigate(R.id.quizFragment)
            resultViewModel.resetAnswer()
        }.setPositiveButton("SEE RESULT") { _, _ ->
            resultViewModel.writeScores()
            resultViewModel.checkAnswer(totalQuestions)
            navController.navigate(R.id.action_quizFragment_to_resultFragment)
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()
    }
}