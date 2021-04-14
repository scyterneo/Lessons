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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

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
            val firstValue = try {
                first.toInt()
            } catch (ex: Exception) {
                0
            }
            val secondValue = try {
                second.toInt()
            } catch (ex: Exception) {
                0
            }
            result = calculateUseCase.calculate(firstValue, secondValue)
            _calculationState.value = CalculationState.Result
            freeStateWithDelay()
        }

        return result
    }

    private suspend fun freeStateWithDelay() {
        delay(1000)
        _calculationState.value = CalculationState.Free
    }

    fun onOperationSelected(operation: Operation) {
        viewModelScope.launch {
            operationsUseCase.deleteOperation(operation)
        }
    }

    init {
        viewModelScope.launch {
            operationsUseCase.getOperations().collect {
                operations.value = it
            }
        }
    }
}
