package com.interview.dummy.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interview.dummy.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
        viewModel.getAllPersonData()
    }

    private fun setupObservers() {
        viewModel.noteListFromDatabase.observe(viewLifecycleOwner) { result ->
            binding.apply {
                swipeRefreshLayout.isRefreshing = false
                progressBar.isVisible = false
            }
            isLoading = false
            when (val response = result.fetchResponse) {
                null -> {
                    if (personAdapter.currentList.isEmpty()) {
                        binding.informationText.apply {
                            text = result.fetchError?.errorDescription
                                ?: getString(R.string.unknown_error_message)
                            isVisible = true
                        }
                    } else {
                        val toast = Toast.makeText(
                            this.requireContext(),
                            result.fetchError?.errorDescription
                                ?: getString(R.string.unknown_error_message),
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }

                else -> {
                    val newPersonList = response.people
                    if (newPersonList.isEmpty()) {
                        binding.informationText.apply {
                            text = getString(R.string.empty_list_message)
                            isVisible = true
                        }
                    } else {
                        setPersonList(newPersonList)
                        binding.informationText.isVisible = false
                    }
                }
            }
        }
    }

    private fun setupUI() {
        with(binding.personListRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = personAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = personAdapter.itemCount

                    if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                        binding.progressBar.isVisible = true
                        isLoading = true
                        viewModel.getAllPersonData()
                    }
                }
            })
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            personAdapter.submitList(emptyList())
            viewModel.getAllPersonData()
        }
    }

    private fun setPersonList(newPersonList: List<Person>) {
        val currentList = personAdapter.currentList.toMutableList()
        currentList.addAll(newPersonList)
        personAdapter.submitList(currentList.distinctBy {
            it.id
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
