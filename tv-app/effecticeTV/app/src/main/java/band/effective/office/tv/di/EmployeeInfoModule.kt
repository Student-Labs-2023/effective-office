package band.effective.office.tv.di

import band.effective.office.tv.repository.EmployeeInfoRepository
import band.effective.office.tv.repository.EmployeeInfoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface EmployeeInfoModule {
    @Singleton
    @Binds
    fun bindEmployeeInfoRepository(impl: EmployeeInfoRepositoryImpl): EmployeeInfoRepository
}