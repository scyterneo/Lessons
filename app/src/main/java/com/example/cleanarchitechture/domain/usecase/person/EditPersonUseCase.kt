package com.example.cleanarchitechture.domain.usecase.person

import com.example.cleanarchitechture.domain.entity.Person

interface EditPersonUseCase {
    suspend fun addPerson(person: Person)
    suspend fun deletePerson(person: Person)
}