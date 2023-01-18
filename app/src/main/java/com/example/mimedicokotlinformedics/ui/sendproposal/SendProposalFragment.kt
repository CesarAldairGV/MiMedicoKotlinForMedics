package com.example.mimedicokotlinformedics.ui.sendproposal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.mimedicokotlinformedics.databinding.FragmentSendProposalBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SendProposalFragment : Fragment() {

    private val viewModel: SendProposalViewModel by viewModels()

    private var _binding: FragmentSendProposalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendProposalBinding.inflate(inflater, container, false)

        viewModel.formState.observe(viewLifecycleOwner){
            if(it.messageErr != null){
                binding.sendpMessage.error = "El mensaje debe ser mayor a 20 caracteres"

            }
            binding.sendpButton.isEnabled = it.isDataValid
        }

        viewModel.petitionInfo.observe(viewLifecycleOwner){
            binding.sendpSubject.text = it.subject
            binding.sendpUsername.text = it.username
            binding.sendpDate.text = it.date
            binding.sendpBody.text = it.body

            if(it.imgUrl != null){
                binding.sendpImg.visibility = View.VISIBLE
                Picasso.get()
                    .load(it.imgUrl)
                    .into(binding.sendpImg)
            }

            binding.sendpProgress.visibility = View.GONE
            binding.sendpCard1Layout.visibility = View.VISIBLE
        }

        viewModel.result.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(context, "Propuesta enviada correctamente",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "Ocurrio un error",Toast.LENGTH_LONG).show()
            }
            binding.sendpProgress2.visibility = View.GONE
        }

        binding.sendpMessage.addTextChangedListener {
            viewModel.checkData(it.toString())
        }

        binding.sendpButton.setOnClickListener{
            viewModel.sendProposal(arguments?.getString("petitionId")!!,
                binding.sendpMessage.text.toString())
            binding.sendpProgress2.visibility = View.VISIBLE
        }

        viewModel.loadInfo(arguments?.getString("petitionId")!!)
        binding.sendpButton.isEnabled = false
        return binding.root
    }

}