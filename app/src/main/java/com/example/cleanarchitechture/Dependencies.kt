package com.example.cleanarchitechture

import androidx.work.WorkManager
import com.example.cleanarchitechture.data.cloud.CloudSource
import com.example.cleanarchitechture.data.db.LocalDatabaseSource
import com.example.cleanarchitechture.data.work.WorkControllerImpl
import com.example.cleanarchitechture.domain.usecase.person.EditPersonUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCaseImpl
import com.example.cleanarchitechture.domain.usecase.person.WorkController

object Dependencies {

    private val localDatabaseSource: LocalDatabaseSource by lazy { LocalDatabaseSource(App.instance) }
    private val cloudSource: CloudSource by lazy { CloudSource() }
    private val workManager: WorkManager by lazy { WorkManager.getInstance(App.instance) }



    fun getPersonsUseCase(): PersonsUseCase =
        PersonsUseCaseImpl(localDatabaseSource, cloudSource, getWorkController())

    fun getEditPersonUseCase(): EditPersonUseCase =
        PersonsUseCaseImpl(localDatabaseSource, cloudSource, getWorkController())

    private fun getWorkController(): WorkController =
        WorkControllerImpl(workManager)
}
