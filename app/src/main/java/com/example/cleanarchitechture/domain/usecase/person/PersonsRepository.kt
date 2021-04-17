package com.example.cleanarchitechture.domain.usecase.person

import com.example.cleanarchitechture.domain.entity.Person
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

interface PersonsRepository {

    suspend fun addPerson(person: Person)
    suspend fun deletePerson(person: Person)
    fun getPersons(): Flow<List<Person>>
    fun getPersonsRX(): Flowable<List<Person>>

}