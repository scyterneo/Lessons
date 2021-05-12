package com.example.cleanarchitechture.di

import androidx.work.WorkManager
import com.example.cleanarchitechture.data.cloud.CloudSource
import com.example.cleanarchitechture.data.db.LocalDatabaseSource
import com.example.cleanarchitechture.data.work.WorkControllerImpl
import com.example.cleanarchitechture.domain.usecase.person.*
import com.example.cleanarchitechture.presentation.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy

val networkModule = module {
    single { CloudSource() }
    single { LocalDatabaseSource(androidContext()) }
    single { WorkControllerImpl(get()) }
    factory {
        WorkManager.getInstance(androidContext())
    }
//
//    factory <WorkController> {
//        return@factory WorkControllerImpl((WorkManager.getInstance(androidContext())))
//    }

    factoryBy<PersonsCloudRepository, CloudSource>()

    factoryBy<WorkController, WorkControllerImpl>()

    factoryBy<PersonsRepository, LocalDatabaseSource>()

    factoryBy<PersonsUseCase, PersonsUseCaseImpl>()
    factoryBy<EditPersonUseCase, PersonsUseCaseImpl>()

    factory {
        PersonsUseCaseImpl(get(), get(), get())
    }
}

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), get())
    }
}