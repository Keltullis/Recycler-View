package com.bignerdranch.android.recyclerviev.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.recyclerviev.R
import com.bignerdranch.android.recyclerviev.model.UserDetails
import com.bignerdranch.android.recyclerviev.model.UserService
import com.bignerdranch.android.recyclerviev.tasks.EmptyResult
import com.bignerdranch.android.recyclerviev.tasks.PendingResult
import com.bignerdranch.android.recyclerviev.tasks.Result
import com.bignerdranch.android.recyclerviev.tasks.SuccessResult

class UserDetailsViewModel(private val usersService: UserService):BaseViewModel() {

    private val _state = MutableLiveData<State>()
    val state:LiveData<State> = _state

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast : LiveData<Event<Int>> = _actionShowToast

    private val _actionGoBack = MutableLiveData<Event<Unit>>()
    val actionGoBack:LiveData<Event<Unit>> = _actionGoBack


    private val currentState :State get() = state.value!!

    init {
        _state.value = State(
            userDetailsResult = EmptyResult(),
            deletingInProgress = false
        )
    }

    fun loadUser(userId:Long){
        if(currentState.userDetailsResult is SuccessResult) return

        _state.value = currentState.copy(userDetailsResult = PendingResult())

        usersService.getById(userId)
            .onSuccess {
                _state.value = currentState.copy(userDetailsResult = SuccessResult(it))
            }
            .onError {
                _actionShowToast.value = Event(R.string.cant_load_user_details)
                _actionGoBack.value = Event(Unit)
            }
            .autoCancel()
    }

    fun deleteUser(){
        val userDetailsResult = currentState.userDetailsResult
        if(userDetailsResult !is SuccessResult) return
        usersService.deleteUser(userDetailsResult.data.user)
            .onSuccess {
                _actionShowToast.value = Event(R.string.user_has_been_deleted)
                _actionGoBack.value = Event(Unit)
            }
            .onError {
                _state.value = currentState.copy(deletingInProgress = false)
            }
            .autoCancel()
    }

    data class State(
        val userDetailsResult:Result<UserDetails>,
        private val deletingInProgress:Boolean
    ){
        val showContent:Boolean get() = userDetailsResult is SuccessResult
        val showProgress:Boolean get() = userDetailsResult is PendingResult || deletingInProgress
        val enableDeleteButton:Boolean get() = !deletingInProgress
    }
}