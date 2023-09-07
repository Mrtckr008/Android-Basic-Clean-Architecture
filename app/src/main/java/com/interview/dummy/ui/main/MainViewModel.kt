package com.interview.dummy.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.interview.dummy.domain.entity.ProcessResult
import com.interview.dummy.domain.usecase.GetAllPersonDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllPersonDataUseCase: GetAllPersonDataUseCase,
    application: Application
) : AndroidViewModel(application) {

    private var nextValue:String? = null
    private val _noteListFromDatabase = MutableLiveData<ProcessResult>()
    val noteListFromDatabase: MutableLiveData<ProcessResult>
        get() = _noteListFromDatabase

    fun getAllPersonData() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAllPersonDataUseCase.invoke(nextValue)
            result.collect {
                nextValue = it.fetchResponse?.next
                _noteListFromDatabase.postValue(it)
            }
        }
    }
}
