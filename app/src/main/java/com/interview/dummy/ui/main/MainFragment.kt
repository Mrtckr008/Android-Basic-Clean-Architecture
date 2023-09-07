package com.interview.dummy.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.interview.dummy.databinding.FragmentMainBinding
import com.interview.dummy.domain.entity.Person
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

        viewModel.getAllPersonData().observe(viewLifecycleOwner) { processResult ->
            if (processResult.fetchError != null) {
                //TODO: Error message
                println("mcmc error -> " + processResult.fetchError?.errorDescription)
            } else if (processResult.fetchResponse != null) {
                processResult.fetchResponse?.people?.let { personList ->
                    setupUI(personList)
                }
            }
        }
    }

    private fun setupUI(personList: List<Person>) {
        val personAdapter = PersonAdapter()
        binding.personListRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = personAdapter
        }
        personAdapter.submitList(personList)
    }
}
