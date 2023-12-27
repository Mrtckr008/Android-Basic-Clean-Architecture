package com.mrtckr.dummy.data.repository

import com.mrtckr.dummy.data.datasource.PersonDataSource
import com.mrtckr.dummy.data.datasource.PersonDataSourceImpl
import com.mrtckr.dummy.domain.entity.ProcessResult
import com.mrtckr.dummy.domain.repository.PersonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import javax.inject.Inject

private const val MAX_RETRIES = 3

class PersonRepositoryImpl @Inject constructor(private val dataSource: PersonDataSource) :
    PersonRepository {

    override suspend fun getAllPersonData(next: String?): Flow<ProcessResult> = flow {
        var retryCounter = 0
        val personData = dataSource.getAllPersonData(next).single()
        emit(personData)
        while (personData.fetchError != null && retryCounter < MAX_RETRIES) {
            val retryRequestData = dataSource.getAllPersonData(next).single()
            if (retryRequestData.fetchError == null) {
                emit(retryRequestData)
                break
            }
            retryCounter++
            delay(PersonDataSourceImpl.INITIAL_DELAY_MILLIS)
        }
    }
}
