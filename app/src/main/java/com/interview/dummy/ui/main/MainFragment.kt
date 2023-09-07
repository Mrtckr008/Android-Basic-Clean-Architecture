package com.interview.dummy.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interview.dummy.databinding.FragmentMainBinding
import com.interview.dummy.domain.entity.Person
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val personAdapter = PersonAdapter()
    private var isLoading = false

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
        setupUI()
        setupObservers()
        viewModel.getAllPersonData()
    }

    private fun setupObservers() {
        viewModel.noteListFromDatabase.observe(viewLifecycleOwner) {
            it.fetchResponse?.people?.let { newPersonList ->
                if (newPersonList.isEmpty()) {
                    binding.informationText.apply {
                        text = "Empty list"
                        isVisible = true
                    }
                } else {
                    setPersonList(newPersonList)
                    binding.informationText.isVisible = false
                }
                binding.apply {
                    swipeRefreshLayout.isRefreshing = false
                    progressBar.isVisible = false
                }
            }

            it.fetchError?.let { fetchError ->
                binding.apply {
                    informationText.apply {
                        text = fetchError.errorDescription
                        isVisible = true
                    }
                    swipeRefreshLayout.isRefreshing = false
                    progressBar.isVisible = false
                    personAdapter.submitList(emptyList())
                }
            }
        }
    }

    private fun setupUI() {
        binding.personListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.personListRecyclerView.adapter = personAdapter

        binding.personListRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val totalItemCount = personAdapter.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                    binding.progressBar.isVisible = true
                    isLoading = true
                    viewModel.getAllPersonData()
                }
            }
        })

        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                viewModel.getAllPersonData()
            }
        }
    }

    private fun setPersonList(newPersonList: List<Person>) {
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
