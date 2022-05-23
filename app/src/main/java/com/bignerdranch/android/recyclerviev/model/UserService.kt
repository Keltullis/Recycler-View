package com.bignerdranch.android.recyclerviev.model

import com.bignerdranch.android.recyclerviev.tasks.SimpleTask
import com.bignerdranch.android.recyclerviev.tasks.Task
import com.github.javafaker.Faker
import java.util.*
import java.util.concurrent.Callable
import kotlin.collections.ArrayList

// класс контроллер

typealias UsersListener = (users:List<User>) -> Unit

class UserService {

    private var users = mutableListOf<User>()

    private var loaded  = false

    private val listeners = mutableListOf<UsersListener>()


    fun loadUsers():Task<Unit> = SimpleTask<Unit>(Callable {
        Thread.sleep(2000)
        val faker = Faker.instance()
        IMAGES.shuffle()
        users = (1..100).map { User(
            id = it.toLong(),
            name = faker.name().name(),
            company = faker.company().name(),
            photo = IMAGES[it % IMAGES.size]
        ) }.toMutableList()
        loaded = true
        notifyChanges()
    })

    fun getById(id:Long):Task<UserDetails> = SimpleTask<UserDetails>(Callable {
        Thread.sleep(2000)
        val user = users.first{it.id == id}
        return@Callable UserDetails(
            user = user,
            details = Faker.instance().overwatch().quote()
        )
    })



    fun deleteUser(user: User):Task<Unit> = SimpleTask<Unit>(Callable {
        //users.remove(user)
        val indexToDelete = users.indexOfFirst { it.id == user.id }
        if(indexToDelete != -1){
            // Создаём новый список
            users = ArrayList(users)
            users.removeAt(indexToDelete)
            notifyChanges()
        }
    })

    fun moveUser(user: User, moveBy:Int):Task<Unit> = SimpleTask<Unit>(Callable {
        Thread.sleep(2000)
        val oldIndex = users.indexOfFirst { it.id == user.id }
        // если такого пользователя не нашли,то индекс будет равен -1
        if(oldIndex == -1) return@Callable
        val newIndex = oldIndex + moveBy
        if(newIndex < 0 || newIndex >= users.size) return@Callable
        users = ArrayList(users)
        Collections.swap(users,oldIndex,newIndex)
        notifyChanges()
    })

    // Добавляем лисенер и сразу же запускаем его и передаём заполненный список пользователей
    fun addListener(listener: UsersListener){
        listeners.add(listener)
        if(loaded) {
            listener.invoke(users)
        }
    }

    fun removeListener(listener:UsersListener){
        listeners.remove(listener)
    }

    // Обновление данных,удалили пользователя,рассказали слушателю об этом,а тот присвоил изменённый список в адаптер
    private fun notifyChanges(){
        if(!loaded) return
        listeners.forEach {
            it.invoke(users)
        }
    }

    companion object{
        private val IMAGES = mutableListOf(
            "https://images.unsplash.com/photo-1600267185393-e158a98703de?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NjQ0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1579710039144-85d6bdffddc9?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Njk1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODE0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1620252655460-080dbec533ca?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzQ1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1613679074971-91fc27180061?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzUz&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1485795959911-ea5ebf41b6ae?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzU4&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1545996124-0501ebae84d0?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/flagged/photo-1568225061049-70fb3006b5be?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Nzcy&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1567186937675-a5131c8a89ea?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODYx&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1546456073-92b9f0a8d413?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800"
        )
    }
}