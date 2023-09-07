package com.interview.dummy.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.interview.dummy.domain.entity.ProcessResult
import com.interview.dummy.domain.usecase.GetAllPersonDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllPersonDataUseCase: GetAllPersonDataUseCase,
    application: Application
) : AndroidViewModel(application) {

    fun getAllPersonData() = liveData<ProcessResult> {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAllPersonDataUseCase.invoke()
            result.collectLatest {
                emit(it)
            }
        }
    }
}
