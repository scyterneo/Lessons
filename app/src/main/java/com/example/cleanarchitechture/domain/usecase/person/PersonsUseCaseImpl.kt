package com.example.cleanarchitechture.domain.usecase.person

import com.example.cleanarchitechture.data.cloud.NetworkResult
import com.example.cleanarchitechture.domain.entity.Person
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

class PersonsUseCaseImpl(
    private val personsRepository: PersonsRepository,
    private val personsCloudRepository: PersonsCloudRepository
) : PersonsUseCase, EditPersonUseCase {
    override fun observePersons(): Flow<List<Person>> =
        personsRepository.getPersons()

    override fun getPersonsRX(): Flowable<List<Person>> =
        personsRepository.getPersonsRX()

    override suspend fun addPerson(person: Person): NetworkResult<Person> {
        return personsCloudRepository.addPerson(person)
    }

    override suspend fun deletePerson(person: Person) {
        personsRepository.deletePerson(person)
    }

    override suspend fun getPersons(): NetworkResult<List<Person>> {
        return personsCloudRepository.getPersons()
    }
}
