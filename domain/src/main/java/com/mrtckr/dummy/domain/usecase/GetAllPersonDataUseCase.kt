package com.mrtckr.dummy.domain.usecase

import com.mrtckr.dummy.domain.entity.ProcessResult
import com.mrtckr.dummy.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPersonDataUseCase @Inject constructor(private val repository: PersonRepository) {
    suspend operator fun invoke(next: String?): Flow<ProcessResult> {
        return repository.getAllPersonData(next)
    }
}
