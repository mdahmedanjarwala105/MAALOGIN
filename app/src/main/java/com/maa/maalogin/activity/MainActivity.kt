package com.maa.maalogin.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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

class MainActivity : AppCompatActivity() {

    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var mobileNumber: TextView
    private lateinit var password: TextView
    private lateinit var deleteUser: Button
    private lateinit var logOut: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name = findViewById(R.id.txt_name)
        email = findViewById(R.id.txt_email)
        mobileNumber = findViewById(R.id.txt_mobile_number)
        password = findViewById(R.id.txt_password1)
        deleteUser = findViewById(R.id.btnDelete)
        logOut = findViewById(R.id.btnLogOut)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Your Account"

        name.text = sharedPreferences.getString("name", "")
        email.text = sharedPreferences.getString("email", "")
        mobileNumber.text = sharedPreferences.getString("mobileNumber", "")
        password.text = sharedPreferences.getString("password", "")

        deleteUser.setOnClickListener{

            clearSharedPreference()

            val registerEntity = RegisterEntity(
                mobileNumber.text.toString().toLong(),
                name.text.toString(),
                email.text.toString(),
                password.text.toString()
            )

            if (DBAsyncTask(applicationContext, registerEntity, 1).execute().get()) {
                val async = DBAsyncTask(applicationContext, registerEntity, 2).execute()
                val result = async.get()

                if(result) {
                    Toast.makeText(this@MainActivity, "Account Deleted", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)

                    finish()

                } else {
                    Toast.makeText(this@MainActivity, "Some error occurred!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)

                    finish()
                }

            } else {
                Toast.makeText(this@MainActivity, "Some error occurred!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)

                finish()
            }
        }

        logOut.setOnClickListener{
            clearSharedPreference()

            val registerEntity = RegisterEntity(
                mobileNumber.text.toString().toLong(),
                name.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
            if (DBAsyncTask(applicationContext, registerEntity, 1).execute().get()) {
                Toast.makeText(this@MainActivity, "Logging Out", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.putExtra("name", name.text.toString())
                intent.putExtra("email", email.text.toString())
                intent.putExtra("mobileNum", mobileNumber.text.toString())
                intent.putExtra("password", password.text.toString())
                startActivity(intent)

                finish()
            }
        }

    }

    private fun clearSharedPreference() {
        sharedPreferences.edit().clear().apply()
    }

    class DBAsyncTask(val context: Context, private val registerEntity: RegisterEntity, private val mode: Int) : AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 -> Check DB if the book is favourite or not
        Mode 2 -> Save the book into DB as favourite
        Mode 3 -> Remove the favourite book
        * */

        private val db =
            Room.databaseBuilder(context, RegisterDatabase::class.java, "books-db").build()

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Boolean {

            when (mode) {

                1 -> {

//                  Check DB if the book is favourite or not
                    val book: RegisterEntity? =
                        db.registerDao().getBookById(registerEntity.mobileNumber.toString())
                    db.close()
                    return book != null

                }


                2 -> {

//                  Remove the favourite book
                    db.registerDao().deleteBook(registerEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}