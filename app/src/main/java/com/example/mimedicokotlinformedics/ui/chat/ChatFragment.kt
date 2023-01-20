package com.example.mimedicokotlinformedics.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mimedicokotlinformedics.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ChatViewModel by viewModels()

    private lateinit var adapter: ChatAdapter

    private lateinit var consultId: String

    private lateinit var photoUrl: String

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

        viewModel.messageState.observe(viewLifecycleOwner){
            binding.chatMsgSend.isEnabled = it
        }

        viewModel.photoState.observe(viewLifecycleOwner){
            if(it != null){
                photoUrl = it
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

        viewModel.getMedicPhoto()

        binding.chatMsgSend.isEnabled = false
        return binding.root
    }
}