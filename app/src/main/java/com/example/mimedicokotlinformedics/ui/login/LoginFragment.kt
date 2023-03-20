package com.example.mimedicokotlinformedics.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mimedicokotlinformedics.R
import com.example.mimedicokotlinformedics.databinding.FragmentLoginBinding
import com.example.mimedicokotlinformedics.ui.profile.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val loginViewModel : LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        loginViewModel.loginForm.observe(viewLifecycleOwner){
            if(it.emailError != null){
                binding.loginEmail.error = getString(R.string.login_email_err)
            }
            if(it.passwordError != null){
                binding.loginPassword.error = getString(R.string.login_password_err)
            }
            binding.loginButton.isEnabled = it.isDataValid
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner){
            if(it.loginError == 1){
                Toast.makeText(activity, getString(R.string.login_err2), Toast.LENGTH_LONG).show()
                binding.loginProgress.visibility = View.GONE
            }else if(it.loginError == 2){
                Toast.makeText(activity, getString(R.string.login_err1), Toast.LENGTH_LONG).show()
                binding.loginProgress.visibility = View.GONE
            }
            if(it.loginSuccess){
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        binding.loginEmail.addTextChangedListener {
            checkData()
        }
        binding.loginPassword.addTextChangedListener {
            checkData()
        }

        binding.loginButton.setOnClickListener {
            login()
            binding.loginProgress.visibility = View.VISIBLE
        }

        binding.loginButton.isEnabled = false
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun checkData(){
        loginViewModel.checkData(
            binding.loginEmail.text.toString(),
            binding.loginPassword.text.toString()
        )
    }

    fun login(){
        loginViewModel.login(
            binding.loginEmail.text.toString(),
            binding.loginPassword.text.toString()
        )
    }
}