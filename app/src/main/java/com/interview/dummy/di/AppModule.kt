package com.interview.dummy.di

import com.interview.dummy.data.datasource.PersonDataSource
import com.interview.dummy.data.datasource.PersonDataSourceImpl
import com.interview.dummy.data.repository.PersonRepositoryImpl
import com.interview.dummy.domain.repository.PersonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providePersonDataSource(): PersonDataSource {
        return PersonDataSourceImpl()
    }

    @Provides
    fun providePersonRepository(dataSource: PersonDataSource): PersonRepository {
        return PersonRepositoryImpl(dataSource)
    }
}