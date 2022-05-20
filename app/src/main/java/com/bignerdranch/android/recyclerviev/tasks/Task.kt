package com.bignerdranch.android.recyclerviev.tasks


typealias Callback <T> = (T) -> Unit

interface Task<T> {

    fun onSuccess(callback: Callback<T>):Task<T>

    fun onError(callback: Callback<Throwable>) :Task<T>

    fun onCancel()

    fun await():T

}