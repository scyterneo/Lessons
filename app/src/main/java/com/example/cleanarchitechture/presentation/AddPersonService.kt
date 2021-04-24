package com.example.cleanarchitechture.presentation

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.cleanarchitechture.Dependencies
import com.example.cleanarchitechture.domain.entity.Person
import com.example.cleanarchitechture.domain.usecase.person.EditPersonUseCase
import kotlinx.coroutines.*

class AddPersonService : Service() {
    private val personUseCase: EditPersonUseCase = Dependencies.getEditPersonUseCase()
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "AddPersonService onStartCommand")
        val name = intent.getStringExtra(Constants.NAME) ?: ""
        val rating = intent.getFloatExtra(Constants.RATING, 0f)

        ioScope.launch { personUseCase.addPerson(Person(name, rating)) }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        ioScope.cancel()
    }

    companion object {
        const val TAG = "AddPersonService"
    }
}