package com.example.cleanarchitechture.domain

class OperationsUseCaseImpl(
    private val operationsRepository: OperationsRepository
) : OperationsUseCase {
    override suspend fun getOperations(): List<Operation> {
        return operationsRepository.getOperations()
    }

    override suspend fun deleteOperation(operation: Operation) {
        operationsRepository.removeOperation(operation)
    }
}
