package band.effective.office.tv.di

import band.effective.office.tv.repository.leaderIdRepository.LeaderIdEventsInfoRepository
import band.effective.office.tv.repository.leaderIdRepository.impl.LeaderIdEventsInfoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {
    @Singleton
    @Binds
    abstract fun provideLeaderIdEventsInfoRepository(leaderIdEventsInfoRepositoryImpl: LeaderIdEventsInfoRepositoryImpl): LeaderIdEventsInfoRepository
}