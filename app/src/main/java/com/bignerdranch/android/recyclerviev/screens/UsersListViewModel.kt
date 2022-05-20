package com.bignerdranch.android.recyclerviev.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.recyclerviev.model.User
import com.bignerdranch.android.recyclerviev.model.UserService
import com.bignerdranch.android.recyclerviev.model.UsersListener

class UsersListViewModel(private val usersService: UserService): ViewModel() {

    // Вьюмодель не должна знать ни об активити,ни о фрагменте
    // Принято создавать 2 поля ливдаты,приватня мутабл и обычную
    // В мьютабл данные будут изменятся,но поле приватное поэтому работать с ним может только сама вью модель
    // а к обычно лайвдате можно достучатся снаружи,но изменять нельзя
    private val _users = MutableLiveData<List<User>>()
    val users:LiveData<List<User>> = _users


    // Берём данные которые пришли и передаём ливдате
    // Напрмер,подвинули пользователя,там вызвался метод notifyChanges,тот вызвал наш слушатель и передал ему список пользователей,а слушатель тут же обновил лайв дату
    private val listener:UsersListener = {
        _users.value = it
    }

    init {
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener)
    }

     private fun loadUsers(){
        usersService.addListener(listener)
    }

    fun moveUser(user: User, moveBy:Int){
        usersService.moveUser(user, moveBy)
    }

    fun deleteUser(user: User){
        usersService.deleteUser(user)
    }



}