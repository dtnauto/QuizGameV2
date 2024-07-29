package com.example.quizgamev2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgamev2.R
import com.example.quizgamev2.databinding.FragmentResultBinding
import com.example.quizgamev2.viewmodel.QuizViewModel
import com.example.quizgamev2.viewmodel.ResultViewModel

class ResultFragment : Fragment() {

    private lateinit var resultViewModel: ResultViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultViewModel = ViewModelProvider(requireActivity())[ResultViewModel::class.java]
    }

    lateinit var fragmentResultBinding: FragmentResultBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentResultBinding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_result, container, false)
        return fragmentResultBinding.root
    }


    lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        addEvent()
    }

    private fun addEvent() {
        loadScore()
        btnExit()
        btnPlayAgain()
    }

    private fun btnPlayAgain() {
        fragmentResultBinding.btnPlayAgain.setOnClickListener{
            resultViewModel.resetAnswer()
            navController.navigate(R.id.action_resultFragment_to_quizFragment)
        }
    }

    private fun btnExit() {
        fragmentResultBinding.btnExit.setOnClickListener{
            navController.navigate(R.id.action_resultFragment_to_loginFragment)
        }
    }

    private fun loadScore() {
        resultViewModel.currentScoreLiveData.observe(viewLifecycleOwner){
            fragmentResultBinding.txtCorrect.text = it.correct.toString()
            fragmentResultBinding.txtWrong.text = it.wrong.toString()
        }
    }
}