package com.mrtckr.dummy.domain.repository

import com.mrtckr.dummy.domain.entity.ProcessResult
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun getAllPersonData(next: String?): Flow<ProcessResult>
}
