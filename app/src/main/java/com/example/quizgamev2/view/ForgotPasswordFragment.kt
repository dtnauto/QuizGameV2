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
import com.example.quizgamev2.viewmodel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText

class ForgotPasswordFragment : Fragment() {

    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }


    lateinit var navController: NavController
    lateinit var txt_email: TextInputEditText
    lateinit var btn_reset: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        btn_reset = view.findViewById(R.id.btn_reset)
        txt_email = view.findViewById(R.id.txt_email)

        btn_reset.setOnClickListener {
            val email = txt_email.text.toString()
            viewModel.sendEmailToResetPassword(email)
            navController.navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
        }
    }

}