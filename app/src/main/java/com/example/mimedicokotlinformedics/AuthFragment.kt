package com.example.mimedicokotlinformedics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mimedicokotlinformedics.databinding.FragmentAuthBinding


class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)

        binding.authSignupBtn.setOnClickListener() {
            findNavController().navigate(R.id.action_MainFragment_to_SignupFragment)
        }
        binding.authLoginBtn.setOnClickListener() {
            findNavController().navigate(R.id.action_MainFragment_to_LoginFragment)
        }

        return binding.root
    }
}