package com.example.mimedicokotlinformedics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.mimedicokotlinformedics.databinding.FragmentMainBinding
import com.example.mimedicokotlinformedics.databinding.FragmentSignupBinding


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.mainSignupBtn.setOnClickListener() {
            findNavController().navigate(R.id.action_MainFragment_to_SignupFragment)
        }
        binding.mainLoginBtn.setOnClickListener() {
            findNavController().navigate(R.id.action_MainFragment_to_LoginFragment)
        }

        return binding.root
    }
}