package com.example.quizgamev2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgamev2.R
import com.example.quizgamev2.databinding.FragmentSignUpBinding
import com.example.quizgamev2.viewmodel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText

class SignUpFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    lateinit var fragmentSignUpBinding : FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSignUpBinding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_sign_up,container,false)
//        return inflater.inflate(R.layout.fragment_sign_up, container, false)
        return fragmentSignUpBinding.root

    }

    lateinit var navController: NavController

//    lateinit var email: TextInputEditText
//    lateinit var password: TextInputEditText
//    lateinit var btn_sign_up: Button
//    lateinit var btn_back: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
//        email=view.findViewById(R.id.txt_email)
//        password=view.findViewById(R.id.txt_password)
//        btn_sign_up=view.findViewById(R.id.btn_sign_up)
//        btn_back=view.findViewById(R.id.btn_back)

        authViewModel.currentUserLiveData.observe(viewLifecycleOwner) {
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        authViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            // Display error message if login fails
            Toast.makeText(requireContext() , errorMessage , Toast.LENGTH_SHORT).show()
        }

        addEventOnViewCreated()
    }

    private fun addEventOnViewCreated() {
        btnSignUp()
        btnBack()
    }

    private fun btnBack() {
        fragmentSignUpBinding.btnBack.setOnClickListener(){
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    private fun btnSignUp() {
        fragmentSignUpBinding.btnSignUp.setOnClickListener{
            val email = fragmentSignUpBinding.txtEmail.text.toString().trim()
            val password = fragmentSignUpBinding.txtPassword.text.toString().trim()
            if(email.isNotEmpty() && password.isNotEmpty()){
                authViewModel.signUp(email, password)
            }
        }
    }

}