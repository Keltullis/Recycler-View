package com.bignerdranch.android.recyclerviev

import com.bignerdranch.android.recyclerviev.model.User

interface Navigator {

    fun showDetails(user: User)

    fun goBack()

    fun toast(messageRes:Int)
}