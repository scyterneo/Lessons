package com.example.cleanarchitechture.data.db

import android.content.Context
import androidx.room.Room
import com.example.cleanarchitechture.domain.entity.Person
import com.example.cleanarchitechture.domain.usecase.person.PersonsRepository
import com.example.cleanarchitechture.extensions.background
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    override suspend fun addPersons(persons: List<Person>) {
        background {
            personDao.insertAll(persons)
        }
    }

    override fun getPersons(): Flow<List<Person>> {
        return personDao.getAll()
    }

    override fun getPersonsRX(): Flowable<List<Person>> {
        return personDao.getAllRX().share()
    }

    override suspend fun deletePerson(person: Person) {
        background {
            personDao.delete(person)
        }
    }

    override suspend fun deleteAll() {
        background {
            personDao.deleteAll()
        }
    }

    override suspend fun updatePersons(persons: List<Person>) {
        background {
            personDao.updatePersons(persons)
        }
    }
}