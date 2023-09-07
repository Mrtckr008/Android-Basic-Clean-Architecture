package com.interview.dummy.data.datasource

import com.interview.dummy.domain.entity.ProcessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PersonDataSourceImpl @Inject constructor(private val dataSource: DataSource) :
    PersonDataSource {
    override suspend fun getAllPersonData(next: String?): Flow<ProcessResult> = flow {
        val response = suspendCoroutine<ProcessResult?> { continuation ->
            dataSource.fetch(next) { response, error ->
                continuation.resume(
                    ProcessResult(
                        fetchResponse = response,
                        fetchError = error,
                        waitTime = INITIAL_DELAY_MILLIS.toDouble()
                    )
                )
            }
        }

        emit(
            ProcessResult(
                fetchResponse = response?.fetchResponse,
                fetchError = response?.fetchError,
                waitTime = INITIAL_DELAY_MILLIS.toDouble()
            )
        )
    }.flowOn(Dispatchers.IO)

    companion object {
        const val INITIAL_DELAY_MILLIS = 5000L
    }
}
