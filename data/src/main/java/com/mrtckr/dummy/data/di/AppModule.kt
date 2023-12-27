package com.mrtckr.dummy.data.di

import com.mrtckr.dummy.data.datasource.DataSource
import com.mrtckr.dummy.data.datasource.PersonDataSource
import com.mrtckr.dummy.data.datasource.PersonDataSourceImpl
import com.mrtckr.dummy.data.repository.PersonRepositoryImpl
import com.mrtckr.dummy.domain.repository.PersonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDataSource(): DataSource = DataSource()

    @Provides
    fun providePersonDataSource(dataSource: DataSource): PersonDataSource {
        return PersonDataSourceImpl(dataSource)
    }

    @Provides
    fun providePersonRepository(dataSource: PersonDataSource): PersonRepository {
        return PersonRepositoryImpl(dataSource)
    }
}
