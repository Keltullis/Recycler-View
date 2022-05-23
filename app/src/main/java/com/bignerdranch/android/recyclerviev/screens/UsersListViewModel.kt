package com.bignerdranch.android.recyclerviev.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.recyclerviev.R
import com.bignerdranch.android.recyclerviev.UserActionListener
import com.bignerdranch.android.recyclerviev.model.User
import com.bignerdranch.android.recyclerviev.model.UserService
import com.bignerdranch.android.recyclerviev.model.UsersListener
import com.bignerdranch.android.recyclerviev.tasks.*

data class UserListItem(val user: User,val isInProgress:Boolean)

class UsersListViewModel(private val usersService: UserService): BaseViewModel(),UserActionListener{

    // Вьюмодель не должна знать ни об активити,ни о фрагменте
    // Принято создавать 2 поля ливдаты,приватня мутабл и обычную
    // В мьютабл данные будут изменятся,но поле приватное поэтому работать с ним может только сама вью модель
    // а к обычно лайвдате можно достучатся снаружи,но изменять нельзя
    private val _users = MutableLiveData<Result<List<UserListItem>>>()
    val users:LiveData<Result<List<UserListItem>>> = _users

    private val _actionShowDetails = MutableLiveData<Event<User>>()
    val actionShowDetails:MutableLiveData<Event<User>> = _actionShowDetails

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast:LiveData<Event<Int>> = _actionShowToast

    private val userIdInProgress = mutableSetOf<Long>()
    private var usersResult :Result<List<User>> = EmptyResult()
        set(value){
            field = value
            notifyUpdates()
        }


    // Берём данные которые пришли и передаём ливдате
    // Напрмер,подвинули пользователя,там вызвался метод notifyChanges,тот вызвал наш слушатель и передал ему список пользователей,а слушатель тут же обновил лайв дату
    private val listener:UsersListener = {
        usersResult = if(it.isEmpty()){
            EmptyResult()
        } else{
            SuccessResult(it)
        }
    }

    init {
        usersService.addListener(listener)
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener)
    }

    fun loadUsers(){
        usersResult = PendingResult()
        usersService.loadUsers()
            .onError {
                usersResult = ErrorResult(it)
            }
            .autoCancel()
    }

    override fun onUserMove(user: User, moveBy:Int){
        if(isInProgress(user)) return
        addProgressTo(user)
        usersService.moveUser(user, moveBy)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_move_user)
            }
            .autoCancel()
    }

    override fun onUserDelete(user: User){
        if(isInProgress(user)) return
        addProgressTo(user)
        usersService.deleteUser(user)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }

    override fun onUserDetails(user: User){
        _actionShowDetails.value = Event(user)
    }

    private fun addProgressTo(user: User){
        userIdInProgress.add(user.id)
        notifyUpdates()
    }
    private fun removeProgressFrom(user: User){
        userIdInProgress.remove(user.id)
        notifyUpdates()
    }

    private fun isInProgress(user: User):Boolean{
        return userIdInProgress.contains(user.id)
    }

    private fun notifyUpdates(){
        _users.postValue(usersResult.map { users ->
            users.map { user -> UserListItem(user,isInProgress(user))}
        })
    }


}