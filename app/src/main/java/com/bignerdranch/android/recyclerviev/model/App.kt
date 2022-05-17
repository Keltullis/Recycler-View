package com.bignerdranch.android.recyclerviev.model

import android.app.Application

class App:Application() {
    val usersService = UserService()
}