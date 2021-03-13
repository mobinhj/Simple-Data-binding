package com.example.sampledatabindingmvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.sampledatabindingmvvm.databinding.ActivityProfileBinding

class ViewModel : AppCompatActivity() {
    private var userName : String? = null
    private var email : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userInfoFromLoginPage

        val user = User()
        user.userName = "User Name : $userName"
        user.email = "Email : $email"

        val binding : ActivityProfileBinding = DataBindingUtil.setContentView(this ,R.layout.activity_profile)
        binding.user = user
    }
    private val userInfoFromLoginPage: Unit
       get() {
            val bundle = intent.extras
            if (bundle != null) {
                userName = bundle.getString("user_name")
                email = bundle.getString("email")

            }
        }
}