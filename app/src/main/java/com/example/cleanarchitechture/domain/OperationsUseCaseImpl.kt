package com.example.cleanarchitechture.domain

class OperationsUseCaseImpl(
    private val operationsRepository: OperationsRepository
) : OperationsUseCase {
    override fun getOperations(): List<Operation> {
        return operationsRepository.getOperations()
    }

    override fun deleteOperation(operation: Operation) {
        operationsRepository.removeOperation(operation)
    }
}
