package com.example.cleanarchitechture.data.work

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.cleanarchitechture.Constants
import com.example.cleanarchitechture.domain.usecase.person.WorkController
import com.example.cleanarchitechture.data.work.worker.AddPersonWorker
import com.example.cleanarchitechture.data.work.worker.GetPersonsWorker

class WorkControllerImpl(private val workManager: WorkManager): WorkController {
    override fun addPerson(name: String, rating: Float) {
        val addPersonWorkRequest = OneTimeWorkRequestBuilder<AddPersonWorker>()
            .setInputData(workDataOf(Constants.NAME to name, Constants.RATING to rating))
            .build()
        val getPersonsWorkRequest = OneTimeWorkRequestBuilder<GetPersonsWorker>()
            .build()
        workManager
            .beginUniqueWork("AddPerson", ExistingWorkPolicy.REPLACE, addPersonWorkRequest)
            .then(getPersonsWorkRequest)
            .enqueue()
    }
}