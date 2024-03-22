package com.maa.maalogin.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.maa.maalogin.R

class LoginActivity : AppCompatActivity() {

    private lateinit var et_mobile_number : EditText
    private lateinit var et_password : EditText
    private lateinit var btn_login : Button
    private lateinit var txt_register : TextView
    private lateinit var txt_forgotPass: TextView
    private lateinit var sharedPreferences: SharedPreferences

    private var name: String? = "name"
    private var email:String? = "email"
    private var mobileNum: String? = "0987654321"
    private var password: String? = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        setContentView(R.layout.activity_login)

        if(isLoggedIn) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.activity_login)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        et_mobile_number = findViewById(R.id.et_mobile_number)
        et_password = findViewById(R.id.et_password)
        btn_login = findViewById(R.id.btn_login)
        txt_register = findViewById(R.id.txt_register)
        txt_forgotPass = findViewById(R.id.txt_forgotPass)

        if (intent != null) {
            name = intent.getStringExtra("name")
            email = intent.getStringExtra("email")
            mobileNum = intent.getStringExtra("mobileNum")
            password = intent.getStringExtra("password")
        } else {
            finish()
            Toast.makeText(
                this@LoginActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        btn_login.setOnClickListener{
            val validMobileNum = et_mobile_number.text.toString()
            val validPassword = et_password.text.toString()

            if (validMobileNum == "" || validPassword == "") {
                Toast.makeText(this@LoginActivity, "Please Fill all Details", Toast.LENGTH_SHORT).show()
            } else {
                if (mobileNum == validMobileNum && password == validPassword) {
                    sharedPreferences(name.toString(), email.toString(), mobileNum.toString(), password.toString())
                    Toast.makeText(this@LoginActivity, "Logged In", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Please Create Account", Toast.LENGTH_SHORT).show()
                }
            }
        }

        txt_register.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        txt_forgotPass.setOnClickListener{
            val intent = Intent(this@LoginActivity, ForgotActivity::class.java)
            intent.putExtra("name", name.toString())
            intent.putExtra("mobileNum", mobileNum.toString())
            intent.putExtra("email", email.toString())
            intent.putExtra("password", password.toString())
            startActivity(intent)
        }
    }

    private fun sharedPreferences(name: String, email: String, mobileNumber: String, password: String) {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("name", name).apply()
        sharedPreferences.edit().putString("email", email).apply()
        sharedPreferences.edit().putString("mobileNumber", mobileNumber).apply()
        sharedPreferences.edit().putString("password", password).apply()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}