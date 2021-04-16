package com.example.cleanarchitechture.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitechture.Dependencies
import com.example.cleanarchitechture.domain.entity.Person
import com.example.cleanarchitechture.domain.usecase.person.EditPersonUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCase
import com.example.cleanarchitechture.extensions.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val personUseCase: PersonsUseCase by lazy { Dependencies.getPersonsUseCase() }
    private val editPersonUseCase: EditPersonUseCase by lazy { Dependencies.getEditPersonUseCase() }

    var name: String = ""
    var rating: String = ""

    private var persons = MutableLiveData<List<Person>>(listOf())

    fun getPersons(): LiveData<List<Person>> {
        return persons
    }

    private var _calculationState = MutableLiveData<CalculationState>(CalculationState.Free)

    val calculationState: LiveData<CalculationState> = _calculationState

    fun addPerson() {
        val rating = try {
            this.rating.toFloat()
        } catch (exception: Exception) {
            0F
        }
        val person = Person(name, rating)
        launch {
            withContext(Dispatchers.IO) {
                editPersonUseCase.addPerson(person)
            }
        }
    }

    fun onPersonSelected(person: Person) {
        launch {
            editPersonUseCase.deletePerson(person)
        }
    }

    init {
        launch {
            personUseCase.getPersons().collect {
                persons.value = it
            }
        }
    }
}
