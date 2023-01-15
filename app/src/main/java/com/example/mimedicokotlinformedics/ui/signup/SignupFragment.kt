package com.example.mimedicokotlinformedics.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mimedicokotlinformedics.R

import com.example.mimedicokotlinformedics.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private val viewModel: SignupViewModel by viewModels()
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        viewModel.signupForm.observe(viewLifecycleOwner){
            if(it.firstnameError != null){
                binding.signupFirstname.error = getString(R.string.signup_firstname_err)
            }
            if(it.lastnameError != null){
                binding.signupLastname.error = getString(R.string.signup_lastname_err)
            }
            if(it.emailError != null){
                binding.signupEmail.error = getString(R.string.signup_email_err)
            }
            if(it.curpError != null){
                binding.signupCurp.error = getString(R.string.signup_curp_err)
            }
            if(it.passwordError != null){
                binding.signupPassword.error = getString(R.string.signup_password_err)
            }
            binding.signupContinue.isEnabled = it.isDataValid
        }

        binding.signupFirstname.addTextChangedListener {
            checkData()
        }
        binding.signupLastname.addTextChangedListener{
            checkData()
        }
        binding.signupEmail.addTextChangedListener {
            checkData()
        }
        binding.signupCurp.addTextChangedListener {
            checkData()
        }
        binding.signupPassword.addTextChangedListener {
            checkData()
        }
        binding.signupContinue.setOnClickListener {
            val bundle = bundleOf(
                "firstname" to binding.signupFirstname.text.toString(),
                "lastname" to binding.signupLastname.text.toString(),
                "email" to binding.signupEmail.text.toString(),
                "curp" to binding.signupCurp.text.toString(),
                "password" to binding.signupPassword.text.toString())
            findNavController().navigate(R.id.action_SignupFragment_to_SignupTwoFragment, bundle)
        }

        binding.signupContinue.isEnabled = false

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun checkData(){
        viewModel.checkData(
            binding.signupFirstname.text.toString(),
            binding.signupLastname.text.toString(),
            binding.signupEmail.text.toString(),
            binding.signupCurp.text.toString(),
            binding.signupPassword.text.toString())
    }
}