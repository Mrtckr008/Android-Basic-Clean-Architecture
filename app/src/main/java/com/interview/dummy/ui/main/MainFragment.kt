package com.interview.dummy.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interview.dummy.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personAdapter = PersonAdapter()
        var isLoading = false

        binding.personListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.personListRecyclerView.adapter = personAdapter

        viewModel.noteListFromDatabase.observe(viewLifecycleOwner) {
            it.fetchResponse?.people?.let { newPersonList ->
                if (isLoading) {
                    val currentList = personAdapter.currentList.toMutableList()
                    currentList.addAll(newPersonList)
                    val filteredList = currentList.distinctBy { person ->
                        person.id
                    }
                    personAdapter.submitList(filteredList)
                    isLoading = false
                } else {
                    personAdapter.submitList(newPersonList)
                }
            }
        }

        viewModel.getAllPersonData()

        binding.personListRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val totalItemCount = personAdapter.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                    isLoading = true
                    viewModel.getAllPersonData()
                }
            }
        })
    }
}
