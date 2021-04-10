package com.example.cleanarchitechture.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitechture.Dependencies
import com.example.cleanarchitechture.domain.CalculateUseCase
import com.example.cleanarchitechture.domain.Operation
import com.example.cleanarchitechture.domain.OperationsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val calculateUseCase: CalculateUseCase by lazy { Dependencies.getCalculateUseCase() }
    private val operationsUseCase: OperationsUseCase by lazy { Dependencies.getOperationsUseCase() }
    var first: String = ""
    var second: String = ""

    private var operations = MutableLiveData<List<Operation>>(listOf())

    fun getOperations(): LiveData<List<Operation>> {
        return operations
    }

    private var _calculationState = MutableLiveData<CalculationState>(CalculationState.Free)

    val calculationState: LiveData<CalculationState> = _calculationState

    fun calculate(): Int {
        _calculationState.value = CalculationState.Loading
        var result = 0
        viewModelScope.launch {
            result = calculateUseCase.calculate(first.toInt(), second.toInt())
            operations.value = operationsUseCase.getOperations()
            _calculationState.value = CalculationState.Result
            updateStatesWithDelay()
        }

        return result
    }

    private suspend fun updateStatesWithDelay() {
        delay(3000)
        _calculationState.value = CalculationState.Free
    }

    fun onOperationSelected(operation: Operation) {
        operationsUseCase.deleteOperation(operation)
        operations.value = operationsUseCase.getOperations()
    }

    init {
        operations.value = operationsUseCase.getOperations()
    }
}
