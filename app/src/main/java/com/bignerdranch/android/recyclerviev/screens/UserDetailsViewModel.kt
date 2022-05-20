package com.bignerdranch.android.recyclerviev.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.recyclerviev.model.UserDetails
import com.bignerdranch.android.recyclerviev.model.UserService

class UserDetailsViewModel(private val usersService: UserService):ViewModel() {

    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails :LiveData<UserDetails> = _userDetails

    fun loadUser(userId:Long){
        if(_userDetails.value != null) return

        _userDetails.value = usersService.getById(userId)
    }

    fun deleteUser(){
        val userDetails = this.userDetails.value ?: return
        usersService.deleteUser(userDetails.user)
    }
}