package com.example.cleanarchitechture.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cleanarchitechture.Dependencies
import com.example.cleanarchitechture.data.cloud.NetworkResult
import com.example.cleanarchitechture.domain.entity.Person
import com.example.cleanarchitechture.domain.usecase.person.EditPersonUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCase
import com.example.cleanarchitechture.extensions.launch
import io.reactivex.disposables.CompositeDisposable

class MainViewModel : ViewModel() {

    private val personUseCase: PersonsUseCase by lazy { Dependencies.getPersonsUseCase() }
    private val editPersonUseCase: EditPersonUseCase by lazy { Dependencies.getEditPersonUseCase() }

    var name: String = ""
    var rating: String = ""

    private var persons = MutableLiveData<List<Person>>(listOf())
    fun getPersons(): LiveData<List<Person>> {
        return persons
    }

    private var topPersons = MutableLiveData<List<Person>>(listOf())
    fun getTopPersons(): LiveData<List<Person>> {
        return topPersons
    }

    private var error = MutableLiveData<String>()
    fun getError(): LiveData<String> = error

    private val disposable = CompositeDisposable()

    private var _calculationState = MutableLiveData<CalculationState>(CalculationState.Free)

    val calculationState: LiveData<CalculationState> = _calculationState


    init {
        updatePersons()
    }

    fun addPerson() {
        val rating = try {
            this.rating.toFloat()
        } catch (exception: Exception) {
            0F
        }
        val person = Person(name, rating)
        launch {
            processNetworkResult(editPersonUseCase.addPerson(person)) {
                updatePersons()
            }
        }
    }

    fun onPersonSelected(person: Person) {
        launch {
            editPersonUseCase.deletePerson(person)
        }
    }

    private fun <T> processNetworkResult(
        networkResult: NetworkResult<T>,
        action: (T) -> Unit
    ) {
        when (networkResult) {
            is NetworkResult.Error -> {
                error.value = networkResult.exception.message
            }
            is NetworkResult.Success -> {
                action(networkResult.data)
            }
        }
    }

    private fun updatePersons() {
        launch {
            processNetworkResult(personUseCase.getPersons()) {
                persons.value = it
            }
        }
    }

/*
 init {


        val subscribe = personUseCase.getPersonsRX()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                persons.value = it
            }

        val subscribeTop = personUseCase.getPersonsRX()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d("MainViewModel", Thread.currentThread().name)
            }
            .observeOn(Schedulers.io())
            .map { persons ->
                Log.d("MainViewModel", Thread.currentThread().name)
                val sortedPersons = persons.sortedBy { it.rating }
                val middleRating = try {
                    sortedPersons[sortedPersons.size / 2].rating
                } catch (exception: Exception) {
                    0F
                }
                sortedPersons.filter { it.rating >= middleRating }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("MainViewModel", Thread.currentThread().name)
                topPersons.value = it
            }
        disposable.add(subscribe)
        disposable.add(subscribeTop)
  }*/

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}
