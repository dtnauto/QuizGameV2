package com.example.quizgamev2.view

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgamev2.R
import com.example.quizgamev2.databinding.FragmentSplashBinding
import com.example.quizgamev2.viewmodel.AuthViewModel

class SplashFragment : Fragment() {

    val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            if (viewModel.repository.firebaseAuth.currentUser != null) {
                navController?.navigate(R.id.action_splashFragment_to_loginFragment)
            } else {
                navController?.navigate(R.id.action_splashFragment_to_signInFragment)
            }
        }, 1000)
    }

    lateinit var fragmentSplashBinding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       fragmentSplashBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_splash, container, false)
        return fragmentSplashBinding.root
//        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {  // dễ lỗi ở đoạn này
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}
