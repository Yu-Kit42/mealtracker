package edu.sungil.mealtracker.model

interface Response<T> {
    val code: Int
    val message: String
    val data: T?

    fun isError(): Boolean
}
