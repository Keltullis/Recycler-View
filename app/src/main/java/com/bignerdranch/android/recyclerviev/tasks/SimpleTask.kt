package com.bignerdranch.android.recyclerviev.tasks

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future


// Симпл такс пока что будет реализован на базе экзекутер сервиса

private val executorService = Executors.newCachedThreadPool()

private val handler = Handler(Looper.getMainLooper())


class SimpleTask<T>(
    private val callable: Callable<T>
):Task<T> {

    private val future:Future<*>
    private var result : Result<T> = PendingResult()

    init {
        future = executorService.submit{
            result =  try {
                SuccessResult(callable.call())
            } catch (e:Throwable){
                ErrorResult(e)
            }
            notifyListeners()
        }
    }

    private var valueCallBack:Callback<T>? = null
    private var errorCallback:Callback<Throwable>? = null

    override fun onSuccess(callback: Callback<T>): Task<T> {
        this.valueCallBack = callback
        notifyListeners()
        return this
    }

    override fun onError(callback: Callback<Throwable>): Task<T> {
        this.errorCallback = callback
        notifyListeners()
        return this
    }

    override fun onCancel() {
        clear()
        future.cancel(true)
    }

    override fun await(): T {
        future.get()
        val result = this.result
        if (result is SuccessResult) return result.data
        else throw (result as ErrorResult).error
    }

    private fun notifyListeners(){
        handler.post{
            val result = this.result
            val callback = this.valueCallBack
            val errorCallback = this.errorCallback

            if(result is SuccessResult && callback != null){
                callback(result.data)
                clear()
            } else if(result is ErrorResult && errorCallback != null){
                errorCallback.invoke(result.error)
                clear()
            }
        }
    }

    private fun clear() {
        valueCallBack = null
        errorCallback = null
    }

}