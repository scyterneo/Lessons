package com.example.cleanarchitechture.domain.usecase.person

import com.example.cleanarchitechture.data.cloud.NetworkResult
import com.example.cleanarchitechture.domain.entity.Person
import io.reactivex.Observable

interface PersonsCloudRepository {
    suspend fun getPersons(): NetworkResult<List<Person>>

    suspend fun addPerson(person: Person): NetworkResult<Person>
}