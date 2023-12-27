package com.example.quizgamev2.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgamev2.R
import com.example.quizgamev2.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText

class SignInFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    lateinit var navController: NavController
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var btn_sign_in: Button
    lateinit var txt_sign_up: TextView

    lateinit var btn_continue_with_google: SignInButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        email=view.findViewById(R.id.txt_email)
        password=view.findViewById(R.id.txt_password)
        btn_sign_in=view.findViewById(R.id.btn_sign_in)
        txt_sign_up=view.findViewById(R.id.txt_sign_up)

        btn_continue_with_google=view.findViewById(R.id.btn_continue_with_google)

        addEventOnViewCreated()
    }

    private fun addEventOnViewCreated() {
        obseverCurrentUser(viewLifecycleOwner)
        btnSignIn()
        btnSignUp()
        btnSignInWithGoogle()
    }

    private fun btnSignInWithGoogle() {
        btn_continue_with_google.setOnClickListener{
            startSignInWithGoogle()
        }
    }

    private fun btnSignUp() {
        txt_sign_up.setOnClickListener(){
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun obseverCurrentUser(owner: LifecycleOwner) {
        viewModel.obseverCurrentUser().observe(owner) {
            navController.navigate(R.id.action_signInFragment_to_loginFragment)
        }
    }

    private fun btnSignIn() {
        btn_sign_in.setOnClickListener{
            val email = email.text.toString().trim()
            val password = password.text.toString().trim()
            if(email.isNotEmpty() && password.isNotEmpty()){
                viewModel.signIn(email, password)
            }
        }
    }

    val RC_SIGN_IN = 9001
    private fun startSignInWithGoogle() {
        val signInIntent = getGoogleSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun getGoogleSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(
            requireActivity(),
            gso
        )
        return googleSignInClient.signInIntent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
                val idToken = handleGoogleSignInResult(data)
                idToken?.let {
                    viewModel.signInWithGoogle(it)
                }
            } else {
                Log.d("GoogleTest", "Data is null")
            }
        }
    }
    private fun handleGoogleSignInResult(data: Intent?): String? {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            return account?.idToken
        } catch (e: ApiException) {
            // Xử lý lỗi nếu cần thiết
            Log.e("GoogleTest", "ApiException: ${e.message}")
        }
        return null
    }
}