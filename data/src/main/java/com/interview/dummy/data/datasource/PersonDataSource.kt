package com.interview.dummy.data.datasource

import com.interview.dummy.domain.entity.ProcessResult
import kotlinx.coroutines.flow.Flow

interface PersonDataSource {
    suspend fun getAllPersonData(): Flow<ProcessResult>
}
