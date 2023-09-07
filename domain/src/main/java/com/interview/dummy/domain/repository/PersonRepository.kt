package com.interview.dummy.domain.repository

import com.interview.dummy.domain.entity.ProcessResult
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun getAllPersonData(next: String?): Flow<ProcessResult>
}
