package com.example.cleanarchitechture.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitechture.Dependencies
import com.example.cleanarchitechture.domain.entity.Person
import com.example.cleanarchitechture.domain.usecase.person.EditPersonUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCase
import com.example.cleanarchitechture.extensions.launch
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

    private var topPersons = MutableLiveData<List<Person>>(listOf())
    fun getTopPersons(): LiveData<List<Person>> {
        return topPersons
    }

    private val disposable = CompositeDisposable()

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
//        launch {
//            personUseCase.getPersons().collect {
//                persons.value = it
//            }
//        }

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
                val middleRating = sortedPersons[sortedPersons.size / 2].rating
                sortedPersons.filter { it.rating >= middleRating }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("MainViewModel", Thread.currentThread().name)
                topPersons.value = it
            }
        disposable.add(subscribe)
        disposable.add(subscribeTop)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}
