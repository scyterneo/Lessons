package com.example.cleanarchitechture

import androidx.work.WorkManager
import com.example.cleanarchitechture.data.cloud.APIService
import com.example.cleanarchitechture.data.cloud.CloudSource
import com.example.cleanarchitechture.data.db.LocalDatabaseSource
import com.example.cleanarchitechture.data.work.WorkControllerImpl
import com.example.cleanarchitechture.domain.usecase.person.EditPersonUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCaseImpl
import com.example.cleanarchitechture.domain.usecase.person.WorkController
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Dependencies {

    private val localDatabaseSource: LocalDatabaseSource by lazy { LocalDatabaseSource(App.instance) }
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
    private val cloudSource: CloudSource by lazy { CloudSource(apiService) }
    private val workManager: WorkManager by lazy { WorkManager.getInstance(App.instance) }

    private const val BASE_URL =
        "https://eu-api.backendless.com/E49B1F96-2BBB-4579-833F-7D3F5E6C84F8/E7A8D6D8-229A-4BFC-9801-F3A60E597E3B/services/Person/"


    fun getPersonsUseCase(): PersonsUseCase =
        PersonsUseCaseImpl(cloudSource)
    // PersonsUseCaseImpl(localDatabaseSource, cloudSource, getWorkController())

    fun getEditPersonUseCase(): EditPersonUseCase =
        PersonsUseCaseImpl(cloudSource)
    //PersonsUseCaseImpl(localDatabaseSource, cloudSource, getWorkController())

    private fun getWorkController(): WorkController =
        WorkControllerImpl(workManager)


    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    private val apiService: APIService = retrofit.create(APIService::class.java)

}
