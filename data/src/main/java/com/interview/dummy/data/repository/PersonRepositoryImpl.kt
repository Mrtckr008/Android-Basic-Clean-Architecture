package com.interview.dummy.data.repository

import com.interview.dummy.data.datasource.PersonDataSource
import com.interview.dummy.data.datasource.PersonDataSourceImpl
import com.interview.dummy.domain.entity.ProcessResult
import com.interview.dummy.domain.repository.PersonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import javax.inject.Inject

private const val MAX_RETRIES = 3

class PersonRepositoryImpl @Inject constructor(private val dataSource: PersonDataSource) :
    PersonRepository {

    private var retryCounter = 0

    override suspend fun getAllPersonData(next: String?): Flow<ProcessResult> = flow {
        val personData = dataSource.getAllPersonData(next).single()
        emit(personData)
        while (personData.fetchError != null && retryCounter < MAX_RETRIES) {
            val retryRequestData = dataSource.getAllPersonData(next).single()
            if (retryRequestData.fetchError == null) {
                emit(retryRequestData)
            }
            retryCounter++
            delay(PersonDataSourceImpl.INITIAL_DELAY_MILLIS)
        }
    }
}
