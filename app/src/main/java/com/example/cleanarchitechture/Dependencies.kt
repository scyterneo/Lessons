package com.example.cleanarchitechture

import com.example.cleanarchitechture.data.db.LocalDatabaseSource
import com.example.cleanarchitechture.domain.usecase.person.EditPersonUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCaseImpl

object Dependencies {

    private val localDatabaseSource: LocalDatabaseSource by lazy { LocalDatabaseSource(App.instance) }

    fun getPersonsUseCase(): PersonsUseCase =
        PersonsUseCaseImpl(localDatabaseSource)

    fun getEditPersonUseCase(): EditPersonUseCase =
        PersonsUseCaseImpl(localDatabaseSource)
}