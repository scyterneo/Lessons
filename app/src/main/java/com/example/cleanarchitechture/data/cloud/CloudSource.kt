package com.example.cleanarchitechture.data.cloud

import com.example.cleanarchitechture.domain.entity.Person
import com.example.cleanarchitechture.domain.usecase.person.PersonsCloudRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CloudSource @Inject constructor(private val apiService: APIService) : PersonsCloudRepository {

    override suspend fun getPersons(): NetworkResult<List<Person>> {
        var personsResponse: NetworkResult<List<Person>>
        withContext(Dispatchers.IO) {
            personsResponse = apiService.getPersons().toNetworkResult()
        }
        return personsResponse
    }

    override suspend fun addPerson(person: Person): NetworkResult<Person> {
        var personsResponse: NetworkResult<Person>
        withContext(Dispatchers.IO) {
            personsResponse = apiService.addPerson(person).toNetworkResult()
        }
        return personsResponse
    }
}
