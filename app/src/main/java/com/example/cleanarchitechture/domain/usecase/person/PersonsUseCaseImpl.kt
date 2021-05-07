package com.example.cleanarchitechture.domain.usecase.person

import com.example.cleanarchitechture.data.cloud.NetworkResult
import com.example.cleanarchitechture.domain.entity.Person
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PersonsUseCaseImpl @Inject constructor(
    //private val personsRepository: PersonsRepository,
    private val personsCloudRepository: PersonsCloudRepository,
   // private val workController: WorkController
) : PersonsUseCase, EditPersonUseCase {
    override fun observePersons(): Flow<List<Person>> = flowOf()
       // personsRepository.observePersons()

    override suspend fun getLocalPersons(): List<Person> = emptyList()
       // personsRepository.getPersons()

    override fun getPersonsRX(): Flowable<List<Person>> = Flowable.just(emptyList())
       // personsRepository.getPersonsRX()

    override suspend fun addPerson(person: Person): NetworkResult<Person> {
        return personsCloudRepository.addPerson(person)
    }

    override suspend fun deletePerson(person: Person) {
      //  personsRepository.deletePerson(person)
    }

    override fun addPerson(name: String, rating: Float) {
       // return workController.addPerson(name, rating)
    }

    override suspend fun getPersons(): Throwable? {
        when(val getPersonsResult = personsCloudRepository.getPersons()) {
            is NetworkResult.Error -> return getPersonsResult.exception
            is NetworkResult.Success -> {
               // personsRepository.updatePersons(getPersonsResult.data)
            }
        }
        return null
    }
}
