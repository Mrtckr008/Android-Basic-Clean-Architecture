package com.interview.dummy.domain.usecase

import com.interview.dummy.domain.entity.ProcessResult
import com.interview.dummy.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPersonDataUseCase @Inject constructor(private val repository: PersonRepository) {
    suspend operator fun invoke(next: String?): Flow<ProcessResult> {
        return repository.getAllPersonData(next)
    }
}
