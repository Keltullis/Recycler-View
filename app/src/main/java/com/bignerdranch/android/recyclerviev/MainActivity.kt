package com.bignerdranch.android.recyclerviev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.recyclerviev.databinding.ActivityMainBinding
import com.bignerdranch.android.recyclerviev.model.App
import com.bignerdranch.android.recyclerviev.model.User
import com.bignerdranch.android.recyclerviev.model.UserService
import com.bignerdranch.android.recyclerviev.model.UsersListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val usersService:UserService
        get() = (applicationContext as App).usersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter(object : UserActionListener{
            override fun onUserMove(user: User, moveBy: Int) {
                usersService.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(this@MainActivity,"pik pok on ${user.name}",Toast.LENGTH_SHORT).show()
            }
        })


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        // Добавляем лисенер
        usersService.addListener(usersListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }

    // Создаём лисенер который передаст заполненный список  в пустой
    private val usersListener:UsersListener = {
        adapter.users = it
    }
}