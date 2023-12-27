package com.mrtckr.dummy.data.datasource

import com.mrtckr.dummy.domain.entity.ProcessResult
import kotlinx.coroutines.flow.Flow

interface PersonDataSource {
    suspend fun getAllPersonData(next: String?): Flow<ProcessResult>
}
