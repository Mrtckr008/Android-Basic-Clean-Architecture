package com.interview.dummy.data.datasource

import com.interview.dummy.domain.entity.ProcessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val WAIT_TIME = 0.1

class PersonDataSourceImpl @Inject constructor() : PersonDataSource {
    override suspend fun getAllPersonData(next: String?): Flow<ProcessResult> = flow {
        val dataSource = DataSource()
        val response = suspendCoroutine<ProcessResult?> { continuation ->
            dataSource.fetch(next) { response, error ->
                continuation.resume(
                    ProcessResult(
                        fetchResponse = response, fetchError = null, waitTime = WAIT_TIME
                    )
                )
            }
        }

        emit(
            ProcessResult(
                fetchResponse = response?.fetchResponse,
                fetchError = response?.fetchError,
                waitTime = WAIT_TIME
            )
        )
    }.flowOn(Dispatchers.IO)
}

