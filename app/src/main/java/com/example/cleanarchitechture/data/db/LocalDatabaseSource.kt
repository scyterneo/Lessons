package com.example.cleanarchitechture.data.db

import android.content.Context
import androidx.room.Room
import com.example.cleanarchitechture.domain.entity.Person
import com.example.cleanarchitechture.domain.usecase.person.PersonsRepository
import com.example.cleanarchitechture.extensions.background
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalDatabaseSource(context: Context): PersonsRepository {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name"
    )
        .build()

    private val personDao = db.personDao()

    override suspend fun addPerson(person: Person) {
        background {
            personDao.insert(person)
        }
    }

    override fun getPersonsRX(): Flowable<List<Person>> = personDao.getAllRX()

    override fun getPersons(): Flow<List<Person>> {
        return flow {
            this.emit(emptyList<Person>())
        }
       // return personDao.getAll()
    }

    override suspend fun deletePerson(person: Person) {
        background {
            personDao.delete(person)
        }
    }
}