package com.example.mimedicokotlinformedics.ui.chat

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mimedicokotlinformedics.R
import com.example.mimedicokotlinformedics.databinding.FragmentChatBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ChatViewModel by viewModels()

    private lateinit var adapter: ChatAdapter

    private lateinit var consultId: String
    private lateinit var userId: String
    private lateinit var medicId: String

    private lateinit var photoUrl: String

    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            val count = it.values.stream().filter{ it }.count()
            if(count == 2L){
                val bundle = bundleOf("myUsername" to medicId, "peerUsername" to userId)
                findNavController().navigate(R.id.action_ChatFragment_to_VideochatFragment, bundle)
            }
        }


    private var getContent = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it == null) return@registerForActivityResult
        val fragmentManager = this@ChatFragment.parentFragmentManager
        val newFragment = SendImageDialogFragment(consultId,it!!,photoUrl)
        newFragment.show(fragmentManager, "dialog")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consultId = arguments?.getString("consultId")!!
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        adapter = ChatAdapter.getAdapter(consultId, binding.chatMsgList)
        binding.chatMsgList.layoutManager = linearLayoutManager
        binding.chatMsgList.adapter = adapter

        viewModel.consultData.observe(viewLifecycleOwner){
            userId = it.userId
            medicId = it.medicId
        }

        viewModel.messageState.observe(viewLifecycleOwner){
            binding.chatMsgSend.isEnabled = it
        }

        viewModel.photoState.observe(viewLifecycleOwner){
            if(it != null){
                photoUrl = it
            }
        }

        viewModel.consultData.observe(viewLifecycleOwner){
            if(it != null){
                binding.chatSubj.text = it.title
                binding.chatBody.text = it.body
                binding.chatUser.text = it.userName
                if(it.imgUrl != null){
                    binding.chatImg.visibility = View.VISIBLE
                    Picasso.get().load(it.imgUrl).into(binding.chatImg)
                    binding.chatImg.visibility = View.VISIBLE
                }
            }
        }

        binding.chatMsgSend.setOnClickListener {
            viewModel.sendMessage(consultId, binding.chatMsgField.text.toString(),photoUrl)
            binding.chatMsgField.text.clear()
        }

        binding.chatMsgImg.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.chatMsgField.addTextChangedListener {
            viewModel.checkMessage(it.toString())
        }

        binding.chatVideochat.setOnClickListener{
            if(!checkPermissions()){
                requestPermissionLauncher.launch(permissions)
            }else{
                val bundle = bundleOf("myUsername" to medicId, "peerUsername" to userId, "consultId" to consultId)
                findNavController().navigate(R.id.action_ChatFragment_to_VideochatFragment, bundle)
            }

        }

        viewModel.getMedicPhoto()
        viewModel.getConsultData(consultId)
        binding.chatMsgSend.isEnabled = false
        return binding.root
    }

    private fun checkPermissions(): Boolean {
        for (per in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), per)
                != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

}