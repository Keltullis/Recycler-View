package com.bignerdranch.android.recyclerviev.screens

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.recyclerviev.tasks.Task


class Event<T>(
    private val value: T
){
    private var handler:Boolean = false

    fun getValue():T?{
        if(handler) return null
        handler = true
        return value
    }

}

open class BaseViewModel:ViewModel() {

    private val tasks = mutableListOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        tasks.forEach { it.onCancel() }
    }

    fun <T> Task<T>.autoCancel(){
        tasks.add(this)
    }
}