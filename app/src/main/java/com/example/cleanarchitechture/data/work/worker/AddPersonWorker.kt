package com.example.cleanarchitechture.data.work.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cleanarchitechture.Constants
import com.example.cleanarchitechture.Dependencies
import com.example.cleanarchitechture.data.cloud.NetworkResult
import com.example.cleanarchitechture.domain.entity.Person

class AddPersonWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val editPersonUseCase = Dependencies.getEditPersonUseCase()

    override suspend fun doWork(): Result {
        val result: Result

        val name = inputData.getString(Constants.NAME)
        val rating = inputData.getFloat(Constants.RATING, Float.MIN_VALUE)

        if (name == null || rating == Float.MIN_VALUE) {
            Log.d(TAG, "Incorrect Person Data")
            result = Result.failure()
            return result
        }
        val person = Person(name, rating)
        val requestResult = editPersonUseCase.addPerson(person)
        result = when (requestResult) {
            is NetworkResult.Success -> Result.success()
            is NetworkResult.Error -> Result.retry()
        }


        Log.d(TAG, "$result")
        return result
    }

    companion object {
        const val TAG = "Worker.AddPerson"
    }
}