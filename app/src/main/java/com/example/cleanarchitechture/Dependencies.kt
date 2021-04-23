package com.example.cleanarchitechture

import com.example.cleanarchitechture.data.cloud.CloudSource
import com.example.cleanarchitechture.data.db.LocalDatabaseSource
import com.example.cleanarchitechture.domain.usecase.person.EditPersonUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCaseImpl

object Dependencies {

    private val localDatabaseSource: LocalDatabaseSource by lazy { LocalDatabaseSource(App.instance) }
    private val CLOUD_SOURCE: CloudSource by lazy { CloudSource()}

    fun getPersonsUseCase(): PersonsUseCase =
        PersonsUseCaseImpl(localDatabaseSource, CLOUD_SOURCE)

    fun getEditPersonUseCase(): EditPersonUseCase =
        PersonsUseCaseImpl(localDatabaseSource, CLOUD_SOURCE)
}
