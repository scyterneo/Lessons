package com.example.cleanarchitechture.domain

interface OperationsRepository {

    suspend fun getOperations(): List<Operation>
    suspend fun addOperation(operation: Operation)
    suspend fun removeOperation(operation: Operation)

}
