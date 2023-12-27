package com.example.quizgamev2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgamev2.R
import com.example.quizgamev2.viewmodel.QuizViewModel
import com.example.quizgamev2.viewmodel.ResultViewModel

class ResultFragment : Fragment() {

    lateinit var resultViewModel: ResultViewModel
    lateinit var navController: NavController

    lateinit var txt_correct: TextView
    lateinit var txt_wrong: TextView

    lateinit var btn_exit: Button
    lateinit var btn_play_again: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultViewModel = ViewModelProvider(requireActivity())[ResultViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        txt_correct=view.findViewById(R.id.txt_correct)
        txt_wrong=view.findViewById(R.id.txt_wrong)

        btn_exit=view.findViewById(R.id.btn_exit)
        btn_play_again=view.findViewById(R.id.btn_play_again)

        addEvent()
    }

    private fun addEvent() {
        loadScore()
        btnExit()
        btnPlayAgain()
    }

    private fun btnPlayAgain() {
        btn_play_again.setOnClickListener{
            resultViewModel.resetAnswer()
            navController.navigate(R.id.action_resultFragment_to_quizFragment)
        }
    }

    private fun btnExit() {
        btn_exit.setOnClickListener{
            navController.navigate(R.id.action_resultFragment_to_loginFragment)
        }
    }

    private fun loadScore() {
        resultViewModel.observerCurrentScores().observe(viewLifecycleOwner){
            txt_correct.text = it.correct.toString()
            txt_wrong.text = it.wrong.toString()
        }
    }
}