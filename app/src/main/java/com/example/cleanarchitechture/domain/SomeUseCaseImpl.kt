package com.example.cleanarchitechture.domain

import javax.inject.Inject

class SomeUseCaseImpl @Inject constructor() : SomeUseCase {
    override fun toFloat(input: String): Float {
        return try { input.toFloat() } catch (exception: Exception) { 0F }
    }
}