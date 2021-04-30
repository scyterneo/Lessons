package com.example.cleanarchitechture.domain.usecase.person

import com.example.cleanarchitechture.data.cloud.NetworkResult
import com.example.cleanarchitechture.domain.entity.Person
import io.reactivex.Flowable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class PersonsUseCaseImpl(
    private val personsRepository: PersonsRepository,
    private val personsCloudRepository: PersonsCloudRepository
) : PersonsUseCase, EditPersonUseCase {
    override fun observePersons(): Flow<List<Person>> =
        personsRepository.observePersons()

    override suspend fun getLocalPersons(): List<Person> =
        personsRepository.getPersons()

    override fun getPersonsRX(): Flowable<List<Person>> =
        personsRepository.getPersonsRX()

    override suspend fun addPerson(person: Person): NetworkResult<Person> {
        delay(3000)
        return personsCloudRepository.addPerson(person)
    }

    override suspend fun deletePerson(person: Person) {
        personsRepository.deletePerson(person)
    }

    override suspend fun getPersons(): NetworkResult.Error<List<Person>>? {
        when(val getPersonsResult = personsCloudRepository.getPersons()) {
            is NetworkResult.Error -> return getPersonsResult
            is NetworkResult.Success -> {
                delay(3000)
                personsRepository.updatePersons(getPersonsResult.data)
            }
        }
        return null
    }
}
