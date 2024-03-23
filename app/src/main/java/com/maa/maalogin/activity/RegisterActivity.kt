package com.maa.maalogin.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.maa.maalogin.R
import com.maa.maalogin.database.RegisterDatabase
import com.maa.maalogin.database.RegisterEntity

class RegisterActivity : AppCompatActivity() {

    private lateinit var et_name: EditText
    private lateinit var et_email: EditText
    private lateinit var et_mobile_number: EditText
    private lateinit var et_password1: EditText
    private lateinit var et_password2: EditText
    private lateinit var btn_register: Button
    private lateinit var toolbar: Toolbar

    var valName: String? = "name"
    var valEmail: String? = "email@gmail.com"
    var valMobileNum: String? = "0987654321"
    var valPassword: String? = "password"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        et_name = findViewById(R.id.et_name)
        et_email = findViewById(R.id.et_email)
        et_mobile_number = findViewById(R.id.et_mobile_number)
        et_password1 = findViewById(R.id.et_password1)
        et_password2 = findViewById(R.id.et_password2)
        btn_register = findViewById(R.id.btn_register)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"

        if (intent != null) {
            valName = intent.getStringExtra("name")
            valEmail = intent.getStringExtra("email")
            valMobileNum = intent.getStringExtra("mobileNum")
            valPassword = intent.getStringExtra("password")
        }

        btn_register.setOnClickListener() {
            val password1 = et_password1.text.toString()
            val password2 = et_password2.text.toString()

            if (et_name.text.toString() == "" ||
                et_email.text.toString() == "" ||
                et_mobile_number.text.toString() == "" ||
                password1 == "" ||
                password2 == "") {
                Toast.makeText(this@RegisterActivity, "Please Fill all Details", Toast.LENGTH_SHORT).show()
            } else {
                if (password1 == password2) {

                    val registerEntity = RegisterEntity(
                        et_mobile_number.text.toString().toLong(),
                        et_name.text.toString(),
                        et_email.text.toString(),
                        et_password1.text.toString()
                    )

                    if (DBAsyncTask(applicationContext, registerEntity, 1).execute().get()) {
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        intent.putExtra("name", et_name.text.toString())
                        intent.putExtra("email", et_email.text.toString())
                        intent.putExtra("mobileNum", et_mobile_number.text.toString())
                        intent.putExtra("password", password1)
                        startActivity(intent)

                        finish()
                    }

                    if (!DBAsyncTask(applicationContext, registerEntity, 1).execute().get()) {
                        val async = DBAsyncTask(applicationContext, registerEntity, 2).execute()
                        val result = async.get()
                        if (result) {
                            Toast.makeText(this@RegisterActivity, "Added", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.putExtra("name", et_name.text.toString())
                            intent.putExtra("email", et_email.text.toString())
                            intent.putExtra("mobileNum", et_mobile_number.text.toString())
                            intent.putExtra("password", password1)
                            startActivity(intent)

                            finish()

                        } else {
                            Toast.makeText(this@RegisterActivity, "Some error occurred!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this@RegisterActivity, "Passwords doesn't match ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    class DBAsyncTask(val context: Context, private val registerEntity: RegisterEntity, private val mode: Int) : AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 -> Check DB if the book is favourite or not
        Mode 2 -> Save the book into DB as favourite
        Mode 3 -> Remove the favourite book
        * */

        private val db = Room.databaseBuilder(context, RegisterDatabase::class.java, "books-db").build()

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Boolean {

            when (mode) {

                1 -> {

//                  Check DB if the book is favourite or not
                    val book: RegisterEntity? = db.registerDao().getBookById(registerEntity.mobileNumber.toString())
                    db.close()
                    return book != null

                }

                2 -> {

//                  Save the book into DB as favourite
                    db.registerDao().insertBook(registerEntity)
                    db.close()
                    return true

                }
            }
            return false
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        intent.putExtra("name", valName)
        intent.putExtra("email", valEmail)
        intent.putExtra("mobileNum", valMobileNum)
        intent.putExtra("password", valPassword)
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}