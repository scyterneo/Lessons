package com.example.cleanarchitechture.domain

interface OperationsUseCase {

    suspend fun getOperations() : List<Operation>

    suspend fun deleteOperation(operation: Operation)
}