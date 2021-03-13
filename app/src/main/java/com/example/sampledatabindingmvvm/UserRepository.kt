package com.example.sampledatabindingmvvm

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sampledatabindingmvvm.Json.JsonUser
import com.example.sampledatabindingmvvm.Retrofit.IUsersApi
import org.koin.dsl.module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface UserRepository {

    fun fetchUsers(): List<User>
}
class UserRepositoryI : UserRepository {
    lateinit var  context : Context
    private  var editTextUserName : EditText? = null
    private  var editTextEmail : EditText? = null
    private  var buttonLogin : Button? = null
    private  val baseUrl = "https://jsonplaceholder.typicode.com/"

    override fun fetchUsers () : List<User> {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val userApi = retrofit.create(IUsersApi::class.java)

        val userNameEditText = editTextUserName!!.text.toString()
        val emailEditText = editTextEmail!!.text.toString()
        val callUser = userApi.getUser(userNameEditText ,emailEditText)
        val intent = Intent(context , ViewModel::class.java)
        callUser.enqueue(object : Callback<JsonUser> {
            override fun onResponse(call: Call<JsonUser>, response: Response<JsonUser>) {
                if ( response.body()!!.count() == 0){
                    Toast.makeText(context , "User Not Found" , Toast.LENGTH_LONG).show()
                    return
                }
                val user = response.body()!![0]
                val userName = user.username
                val email = user.email
                if (userName.isNotEmpty() && email.isNotEmpty()){
                    intent.putExtra("user_name" , userName)
                    intent.putExtra("email" , email)
                    startActivity(intent)
                }else {
                    Toast.makeText(context , "User Not Found" , Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<JsonUser>, t: Throwable) {
                Log.e("console" , t.message ,t)

                Toast.makeText(context, "$t.message" , Toast.LENGTH_SHORT).show()
            }
        })
        return
    }
}
class UserPresenter (private val repo : UserRepository) {
    fun getUser() : List<User> = repo.fetchUsers()
}
val appModule  = module {
    single<UserRepository> {UserRepositoryI()}
    factory {UserPresenter(get())  }
}
