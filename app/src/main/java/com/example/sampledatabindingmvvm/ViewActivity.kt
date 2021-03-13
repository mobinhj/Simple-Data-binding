package com.example.sampledatabindingmvvm

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.sampledatabindingmvvm.Json.JsonUser
import com.example.sampledatabindingmvvm.Retrofit.IUsersApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("DEPRECATION")
class ViewActivity : AppCompatActivity() {

    private  var editTextUserName : EditText? = null
    private  var editTextEmail : EditText? = null
    private  var buttonLogin : Button? = null
    private  val baseUrl = "https://jsonplaceholder.typicode.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        view()
        editTextUserName!!.addTextChangedListener {
            editTextUserName!!.background.mutate().setColorFilter(
                    resources.getColor(android.R.color.holo_blue_light), PorterDuff.Mode.SRC_ATOP)
        }
        editTextEmail!!.addTextChangedListener {
            editTextEmail!!.background.mutate().setColorFilter(
                    resources.getColor(android.R.color.holo_blue_light), PorterDuff.Mode.SRC_ATOP)
        }
        val progress = ProgressDialog(this)
        buttonLogin?.setOnClickListener{
            progress.setMessage("waiting")

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val userApi = retrofit.create(IUsersApi::class.java)

            val userNameEditText = editTextUserName!!.text.toString()
            val emailEditText = editTextEmail!!.text.toString()
            if (userNameEditText.isNotEmpty() && emailEditText.isNotEmpty()){
                progress.show()

                val callUser = userApi.getUser(userNameEditText ,emailEditText)
                val intent = Intent(this , ViewModel::class.java)

                callUser.enqueue(object : Callback<JsonUser> {
                    override fun onResponse(call: Call<JsonUser>, response: Response<JsonUser>) {
                        progress.dismiss()
                        if ( response.body()!!.count() == 0){
                            Toast.makeText(this@ViewActivity , "User Not Found" , Toast.LENGTH_LONG).show()
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
                            Toast.makeText(this@ViewActivity , "User Not Found" , Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<JsonUser>, t: Throwable) {
                        Log.e("console" , t.message ,t)
                        progress.dismiss()

                        Toast.makeText(this@ViewActivity , "$t.message" , Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                if (userNameEditText.isEmpty() && emailEditText.isEmpty()){
                    editTextUserName!!.background.mutate().setColorFilter(
                            resources.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP)
                    editTextEmail!!.background.mutate().setColorFilter(
                            resources.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP)
                } else if (emailEditText.isEmpty()){
                    editTextEmail!!.background.mutate().setColorFilter(
                            resources.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP)
                }else if (userNameEditText.isEmpty()) {
                    editTextUserName!!.background.mutate().setColorFilter(
                            resources.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP)
                }
                Toast.makeText(this , "Please enter all the values", Toast.LENGTH_LONG).show()
            }
        }
    }
    private  fun view (){
        editTextUserName = findViewById<View>(R.id.userName_EditText) as EditText
        editTextEmail = findViewById<View>(R.id.email_EditText) as EditText
        buttonLogin = findViewById<View>(R.id.login_button) as Button
    }
}