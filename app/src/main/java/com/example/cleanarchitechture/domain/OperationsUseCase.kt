package com.example.cleanarchitechture.domain

interface OperationsUseCase {

    fun getOperations() : List<Operation>

    fun deleteOperation(operation: Operation)
}