package com.example.cleanarchitechture.data

import com.example.cleanarchitechture.domain.Operation
import com.example.cleanarchitechture.domain.OperationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class OperationsLocalSource : OperationsRepository {

    private var operations = mutableListOf(Operation(1, 2, 3), Operation(3, 6, 9))

    override suspend fun getOperations(): List<Operation> {
        withContext(Dispatchers.IO) {
            delay(3000)
        }
        return operations
    }

    override suspend fun addOperation(operation: Operation) {
        operations.add(operation)
    }

    override suspend fun removeOperation(operation: Operation) {
        operations.remove(operation)
    }
}
