package com.example.cleanarchitechture.domain.usecase.person

import com.example.cleanarchitechture.domain.entity.Person

interface EditPersonUseCase {
    fun addPerson(person: Person)
}