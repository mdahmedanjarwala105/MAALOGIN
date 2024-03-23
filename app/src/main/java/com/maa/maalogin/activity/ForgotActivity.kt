package com.maa.maalogin.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.maa.maalogin.R

class ForgotActivity : AppCompatActivity() {

    private lateinit var mobileNumber: EditText
    private lateinit var email: EditText
    private lateinit var btnLogin: Button

    var nameVal: String? = "name"
    var mobileNumVal: String? = "0987654321"
    var emailVal: String? = "something@gmail.com"
    var password: String? = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mobileNumber = findViewById(R.id.et_mobile_number)
        email = findViewById(R.id.et_email)
        btnLogin = findViewById(R.id.btn_login)

        if (intent != null) {
            nameVal = intent.getStringExtra("name")
            emailVal = intent.getStringExtra("email")
            mobileNumVal = intent.getStringExtra("mobileNum")
            password = intent.getStringExtra("password")
        } else {
            finish()
            Toast.makeText(
                this@ForgotActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnLogin.setOnClickListener{
            val validMobileNumber = mobileNumber.text.toString()
            val validEmail = email.text.toString()

            if (validMobileNumber == "" || validEmail == "") {
                Toast.makeText(this@ForgotActivity, "Please Fill all Details", Toast.LENGTH_SHORT).show()
            } else {
                if (validMobileNumber == mobileNumVal && validEmail == emailVal) {
                    Toast.makeText(this@ForgotActivity, "Your password is $password", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this@ForgotActivity, "Please Create Account", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ForgotActivity, LoginActivity::class.java)
        intent.putExtra("name", nameVal.toString())
        intent.putExtra("email", emailVal.toString())
        intent.putExtra("mobileNum", mobileNumVal.toString())
        intent.putExtra("password", password.toString())
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}