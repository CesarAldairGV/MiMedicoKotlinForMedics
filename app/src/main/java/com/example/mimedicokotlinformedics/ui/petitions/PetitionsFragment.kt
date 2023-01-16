package com.example.mimedicokotlinformedics.ui.petitions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mimedicokotlinformedics.databinding.FragmentPetitionsBinding
import com.example.mimedicokotlinformedics.hilt.App
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PetitionsFragment : Fragment() {

    private var _binding: FragmentPetitionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FirestoreRecyclerAdapter<PetitionItem, PetitionItemViewHolder>
    private lateinit var app: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as App
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetitionsBinding.inflate(inflater, container, false)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        adapter = PetitionsAdapter.getAdapter()
        binding.petitionsList.layoutManager = linearLayoutManager
        binding.petitionsList.adapter = adapter

        return binding.root
    }

}