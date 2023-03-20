package com.example.mimedicokotlinformedics.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mimedicokotlinformedics.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.homeCardLayout.visibility = View.INVISIBLE
        binding.homeLoading.visibility = View.VISIBLE
        viewModel.profileData.observe(viewLifecycleOwner) {
            binding.profileNameField.text = it.name
            binding.profileEmailField.text = it.email
            binding.profilePhoneField.text = it.curp
            binding.profileSchoolField.text = it.school
            Picasso.get().load(it.certUrl).into(binding.profileCert)
            Picasso.get().load(it.photoUrl).into(binding.profilePhoto)
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