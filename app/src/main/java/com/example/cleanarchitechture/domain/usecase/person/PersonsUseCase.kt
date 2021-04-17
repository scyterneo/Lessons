package com.example.cleanarchitechture.domain.usecase.person

import com.example.cleanarchitechture.domain.entity.Person
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

interface PersonsUseCase {
    fun getPersons(): Flow<List<Person>>
    fun getPersonsRX(): Flowable<List<Person>>
}