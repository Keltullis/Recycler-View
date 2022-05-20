package com.bignerdranch.android.recyclerviev.screens

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.recyclerviev.Navigator
import com.bignerdranch.android.recyclerviev.model.App

// Это фабрика вью моделей,она нужна из-за того что в нашей вью модели есть конструктор и туда нужно что-то передать

class ViewModelFactory(private val app:App) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass){
            UsersListViewModel::class.java -> {
                UsersListViewModel(app.usersService)
            }
            UserDetailsViewModel::class.java -> {
                UserDetailsViewModel(app.usersService)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }

}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator