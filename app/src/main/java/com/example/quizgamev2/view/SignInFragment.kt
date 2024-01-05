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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgamev2.R
import com.example.quizgamev2.databinding.FragmentSignInBinding
import com.example.quizgamev2.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText

class SignInFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    lateinit var fragmentSignInBinding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSignInBinding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_sign_in,container,false)
//        return inflater.inflate(R.layout.fragment_sign_in, container, false)
        return fragmentSignInBinding.root
    }

    lateinit var navController: NavController
//    lateinit var email: TextInputEditText
//    lateinit var password: TextInputEditText
//    lateinit var btn_sign_in: Button
//    lateinit var txt_sign_up: TextView
//    lateinit var txt_forgot_password: TextView

//    lateinit var btn_continue_with_google: SignInButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
//        email=view.findViewById(R.id.txt_email)
//        password=view.findViewById(R.id.txt_password)
//        btn_sign_in=view.findViewById(R.id.btn_sign_in)
//        txt_forgot_password=view.findViewById(R.id.txt_forgot_password)
//        txt_sign_up=view.findViewById(R.id.txt_sign_up)

//        btn_continue_with_google=view.findViewById(R.id.btn_continue_with_google)

        authViewModel.currentUserLiveData.observe(viewLifecycleOwner) {
            navController.navigate(R.id.action_signInFragment_to_loginFragment)
        }

        authViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            // Display error message if login fails
            Toast.makeText(requireContext() , errorMessage , Toast.LENGTH_SHORT).show()
        }

        addEventOnViewCreated()
    }

    private fun addEventOnViewCreated() {
        btnSignIn()
        btnSignUp()
        btnForgotPassword()
        btnSignInWithGoogle()
    }

    private fun btnForgotPassword() {
        fragmentSignInBinding.txtForgotPassword.setOnClickListener(){
            navController.navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }
    }

    private fun btnSignInWithGoogle() {
        fragmentSignInBinding.btnContinueWithGoogle.setOnClickListener{
            startSignInWithGoogle()
        }
    }

    private fun btnSignUp() {
        fragmentSignInBinding.txtSignUp.setOnClickListener(){
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun btnSignIn() {
        fragmentSignInBinding.btnSignIn.setOnClickListener{
            val email = fragmentSignInBinding.txtEmail.text.toString().trim()
            val password = fragmentSignInBinding.txtPassword.text.toString().trim()
            if(email.isNotEmpty() && password.isNotEmpty()){
                authViewModel.signIn(email, password)
            }
        }
    }

    val RC_SIGN_IN = 9001
    private fun startSignInWithGoogle() {

        //GoogleSignInOptions.Builder xây dựng tùy chọn đăng nhập (cái activity của google hiện lên khi bấm button)
        //requestIdToken: yêu cầu token ID từ máy chủ của bạn thông qua web client ID
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) // tạo khung cửa sổ để đăng nhập vào account
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //Tạo intent để thực hiện đăng nhập Google qua GoogleSignIn.getClient()
        //Tạo và trả về một Intent để đăng nhập google
        val googleSignInClient = GoogleSignIn.getClient( // nếu có acc thì sẽ hiện lên khung của sổ
            requireActivity(), // trả về context activity chưa fragment hiện tại // là ngữ cảnh chứa tất cả các thông tin của activity hiện tại
            gso
        )

        // thực hiện yêu của signInintent là dựng khung 
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)  //request code đại diện cho intent
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
                //Xử lý kết quả trả về từ hành động đăng nhập ggl
                //Gọi GoogleSignIn.getSignedInAccountFromIntent(data) để lấy thông tin tài khoản sau khi đăng nhập thành công
                //Trả về idToken
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account = task.getResult(ApiException::class.java)
                    account.idToken?.let { authViewModel.signInWithGoogle(it) }
                } catch (e: ApiException) {
                    Log.e("mycodeisblocking", "ApiException: ${e.message}")
                }
            } else {
                Log.d("mycodeisblocking", "Data is null")
            }
        }
    }
}