package com.example.mimedicokotlinformedics.ui.signuptwo

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mimedicokotlinformedics.R
import com.example.mimedicokotlinformedics.databinding.FragmentSignupTwoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class SignupTwoFragment : Fragment() {

    private val TAG = "SignupFragment"

    private val viewModel: SignupTwoViewModel by viewModels()
    private var _binding: FragmentSignupTwoBinding? = null
    private val binding get() = _binding!!

    private var getCertificateImg = registerForActivityResult(ActivityResultContracts.GetContent()){
        binding.signupCertificateImg.setImageURI(it)
        checkData()
    }

    private var getPhotoImg = registerForActivityResult(ActivityResultContracts.GetContent()){
        binding.signupPhotoImg.setImageURI(it)
        checkData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupTwoBinding.inflate(inflater,container,false)

        viewModel.signupFormState.observe(viewLifecycleOwner){
            if(it.schoolError != null){
                binding.signupSchool.error = getString(R.string.signup_school_err)
            }
            if(it.yearsError != null){
                binding.signupYears.error = "Debe tener mas de un año de experiencia"
            }
            if(it.businessError != null){
                binding.signupBusiness.error = "Debe ser una empresa valida"
            }

            binding.signupButton.isEnabled = it.isDataValid
        }

        viewModel.signupResult.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(R.id.action_SignupTwoFragment_to_SignupSuccessFragment)
            }else{
                Toast.makeText(context, getString(R.string.signup_err1), Toast.LENGTH_LONG).show()
            }
        }

        binding.signupSchool.addTextChangedListener {
            checkData()
        }

        binding.signupBusiness.addTextChangedListener {
            checkData()
        }

        binding.signupYears.addTextChangedListener {
            checkData()
        }

        binding.signupCertificateButton.setOnClickListener{
            getCertificateImg.launch("image/*")
        }

        binding.signupPhotoButton.setOnClickListener{
            getPhotoImg.launch("image/*")
        }

        binding.signupButton.setOnClickListener{
            signup()
            binding.signupProgress.visibility = View.VISIBLE
        }

        binding.signupButton.isEnabled = false
        return binding.root
    }

    private fun signup(){
        viewModel.signup(
            arguments?.getString("firstname")!!,
            arguments?.getString("lastname")!!,
            arguments?.getString("email")!!,
            arguments?.getString("curp")!!,
            arguments?.getString("password")!!,
            binding.signupSchool.text.toString(),
            (binding.signupCertificateImg.drawable as BitmapDrawable).bitmap,
            (binding.signupPhotoImg.drawable as BitmapDrawable).bitmap,
            binding.signupBusiness.text.toString(),
            Integer.parseInt(binding.signupYears.text.toString())
        )
    }

    private fun checkData(){
        val numString = binding.signupYears.text.toString()
        var num: Int? = if(numString.isEmpty()){
            null
        }else{
            Integer.parseInt(numString)
        }
        viewModel.checkData(
            binding.signupSchool.text.toString(),
            (binding.signupCertificateImg.drawable as? BitmapDrawable)?.bitmap,
            (binding.signupPhotoImg.drawable as? BitmapDrawable)?.bitmap,
            binding.signupBusiness.text.toString(),
            num
        )
    }

}