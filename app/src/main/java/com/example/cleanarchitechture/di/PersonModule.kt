package com.example.cleanarchitechture.di

import com.example.cleanarchitechture.BuildConfig
import com.example.cleanarchitechture.Dependencies
import com.example.cleanarchitechture.data.cloud.APIService
import com.example.cleanarchitechture.data.cloud.CloudSource
import com.example.cleanarchitechture.domain.SomeUseCase
import com.example.cleanarchitechture.domain.SomeUseCaseImpl
import com.example.cleanarchitechture.domain.usecase.person.EditPersonUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsCloudRepository
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCase
import com.example.cleanarchitechture.domain.usecase.person.PersonsUseCaseImpl
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersonModule {

    private const val BASE_URL =
        "https://eu-api.backendless.com/E49B1F96-2BBB-4579-833F-7D3F5E6C84F8/E7A8D6D8-229A-4BFC-9801-F3A60E597E3B/services/Person/"


    @Provides
    fun providesPersonsCloudRepository(cloudSource: CloudSource): PersonsCloudRepository {
        return cloudSource
    }

    @Provides
    @Singleton
    fun providesCloudSource(apiService: APIService): CloudSource {
        return CloudSource(apiService)
    }

    @Provides
    fun providesSomeUseCase(someUseCaseImpl: SomeUseCaseImpl): SomeUseCase {
        return someUseCaseImpl
    }

    @Provides
    fun providesSomeUseCaseImpl() : SomeUseCaseImpl {
        return SomeUseCaseImpl()
    }

    @Provides
    fun providesPersonsUseCase(impl: PersonsUseCaseImpl): PersonsUseCase {
        return impl
    }

    @Provides
    fun providesEditPersonUseCase(impl: PersonsUseCaseImpl): EditPersonUseCase {
        return impl
    }


    @Provides
    fun providesPersonUseCaseImpl(personsCloudRepository: PersonsCloudRepository): PersonsUseCaseImpl {
        return PersonsUseCaseImpl(personsCloudRepository)
    }

    @Provides
    fun provideLoggerInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        }
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): APIService{
        return retrofit.create(APIService::class.java)
    }
}
