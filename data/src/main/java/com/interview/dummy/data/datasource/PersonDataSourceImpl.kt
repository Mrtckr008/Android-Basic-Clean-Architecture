package com.interview.dummy.data.datasource

import com.interview.dummy.domain.entity.ProcessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PersonDataSourceImpl @Inject constructor() : PersonDataSource {
    override suspend fun getAllPersonData(): Flow<ProcessResult> = flow {
        val dataSource = DataSource()
        val nextPageIdentifier = "1"
        val response = suspendCoroutine<ProcessResult?> { continuation ->
            dataSource.fetch(nextPageIdentifier) { response, error ->
                continuation.resume(
                    ProcessResult(
                        fetchResponse = response,
                        fetchError = null,
                        waitTime = 1.0
                    )
                )
            }
        }

        if (response != null) {
            emit(ProcessResult(fetchResponse = null, fetchError = null, 2.0))
        }
    }.flowOn(Dispatchers.IO)
}
