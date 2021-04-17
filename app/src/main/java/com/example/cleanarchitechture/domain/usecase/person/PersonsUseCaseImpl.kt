package com.example.cleanarchitechture.domain.usecase.person

import com.example.cleanarchitechture.domain.entity.Person
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

class PersonsUseCaseImpl(private val personsRepository: PersonsRepository): PersonsUseCase, EditPersonUseCase {
    override fun getPersons(): Flow<List<Person>> =
        personsRepository.getPersons()

    override fun getPersonsRX(): Flowable<List<Person>> =
        personsRepository.getPersonsRX()

    override suspend fun addPerson(person: Person) {
        personsRepository.addPerson(person)
    }

    override suspend fun deletePerson(person: Person) {
        personsRepository.deletePerson(person)
    }
}