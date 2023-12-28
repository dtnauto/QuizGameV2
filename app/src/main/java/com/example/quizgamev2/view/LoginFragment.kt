package com.example.quizgamev2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgamev2.R
import com.example.quizgamev2.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginFragment : Fragment() {

    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }


    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    lateinit var navController: NavController

    lateinit var btn_sign_out: Button
    lateinit var btn_start: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        btn_sign_out=view.findViewById(R.id.btn_sign_out)
        btn_start=view.findViewById(R.id.btn_start)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        addEventOnViewCreated()
    }

    private fun addEventOnViewCreated() {
        obseverCurrentUser(viewLifecycleOwner)
        btnSignOut()
        btnStart()
    }

    private fun btnStart() {
        btn_start.setOnClickListener{
            navController.navigate(R.id.action_loginFragment_to_quizFragment)
        }
    }

    private fun obseverCurrentUser(owner: LifecycleOwner) {
        viewModel.obseverCurrentUser().observe(owner) {
            navController.navigate(R.id.action_loginFragment_to_signInFragment)
        }
    }

    private fun btnSignOut() {
        btn_sign_out.setOnClickListener{
            viewModel.signOut()
            signOutGoogle()
        }
    }

    private fun signOutGoogle() {
        googleSignInClient.signOut()
    }
}