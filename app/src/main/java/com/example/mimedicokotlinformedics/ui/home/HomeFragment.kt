package com.example.mimedicokotlinformedics.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mimedicokotlinformedics.databinding.FragmentHomeBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeCardLayout.visibility = View.INVISIBLE
        binding.homeLoading.visibility = View.VISIBLE
        viewModel.profileData.observe(viewLifecycleOwner) {
            binding.homeNameField.text = it.name
            binding.homeEmailField.text = it.email
            binding.homeCurpField.text = it.curp
            binding.homeSchoolField.text = it.school
            Picasso.get().load(it.certUrl).into(binding.homeCert)
            Picasso.get().load(it.photoUrl).into(binding.homePhoto)
            binding.homeCardLayout.visibility = View.VISIBLE
            binding.homeLoading.visibility = View.INVISIBLE
        }

        viewModel.loadProfileData()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}