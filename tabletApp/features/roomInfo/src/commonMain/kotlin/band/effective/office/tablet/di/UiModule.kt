package band.effective.office.tablet.di

import band.effective.office.tablet.domain.useCase.CheckBookingUseCase
import band.effective.office.tablet.domain.useCase.UpdateUseCase
import org.koin.dsl.module

val uiModule = module {
    single<UpdateUseCase> { UpdateUseCase(get(), get(), get()) }
    single<CheckBookingUseCase> { CheckBookingUseCase(get()) }
}