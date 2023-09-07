package com.interview.dummy.data.repository

import com.interview.dummy.data.datasource.PersonDataSource
import com.interview.dummy.domain.entity.ProcessResult
import com.interview.dummy.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(private val dataSource: PersonDataSource) :
    PersonRepository {

    override suspend fun getAllPersonData(): Flow<ProcessResult> = flow {
        emit(dataSource.getAllPersonData().single())
    }
}
