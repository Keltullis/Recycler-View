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
import com.bignerdranch.android.recyclerviev.screens.UserDetailsFragment
import com.bignerdranch.android.recyclerviev.screens.UsersListFragment

class MainActivity : AppCompatActivity(),Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer,UsersListFragment())
                .commit()
        }
    }

    override fun showDetails(user: User) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer,UserDetailsFragment.newInstance(user.id))
            .commit()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun toast(messageRes: Int) {
        Toast.makeText(this,messageRes,Toast.LENGTH_SHORT).show()
    }
}