package com.example.cleanarchitechture.data.db.dao

import androidx.room.*
import com.example.cleanarchitechture.domain.entity.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM Person")
    fun getAll(): Flow<List<Person>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(person: Person)

    @Delete
    fun delete(person: Person)
}