package com.auf.letswatch

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.auf.letswatch.databinding.ActivityLoginBinding
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import java.util.*

const val USERNAME_KEY = "KEY_USERNAME"
const val KEEPMESIGNIN_KEY = "KEY_KEEPMEIN"

class Login : AppCompatActivity(), View.OnClickListener, ValueEventListener {

    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityLoginBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideMenus()
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://letswatch-de7ce-default-rtdb.firebaseio.com/")
        isItSignin()
        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val sharedPreferences = getSharedPreferences("MY_PROFILE",Context.MODE_PRIVATE)
        when(p0!!.id){
            (R.id.btn_register)->{
                val intent = Intent(this, Register::class.java)
                startActivity(intent)
            }
            (R.id.btn_login)->{
                if(binding.btnKeepmelogin.isChecked){
                    val editor = sharedPreferences.edit()
                    editor.putString(KEEPMESIGNIN_KEY,"TRUE")
                    editor.apply()
                }
                else{
                    val editor = sharedPreferences.edit()
                    editor.putString(KEEPMESIGNIN_KEY,"FALSE")
                    editor.apply()
                }

                database.child("Users").addListenerForSingleValueEvent(this)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.R)
    fun hideMenus(){
        supportActionBar?.hide()
        window.insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        window.insetsController?.hide(WindowInsets.Type.navigationBars())
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        val sharedPreferences = getSharedPreferences("MY_PROFILE",Context.MODE_PRIVATE)

        if (snapshot.hasChild(binding.edttxtUsername.text.toString())){
            val getPass = snapshot.child(binding.edttxtUsername.text.toString()).child("pass").value.toString()
            if(getPass == binding.edttxtPassword.text.toString()){
                val editor = sharedPreferences.edit()
                editor.putString(USERNAME_KEY,binding.edttxtUsername.text.toString())
                editor.apply()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(applicationContext, "Wrong Credentials", Toast.LENGTH_SHORT).show()
                Log.e("",getPass)
            }
        }
        else{
            Toast.makeText(applicationContext, "Wrong Credentials", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCancelled(error: DatabaseError) {

    }

    fun isItSignin(){
        val sharedPreferences = getSharedPreferences("MY_PROFILE",Context.MODE_PRIVATE)
        val keepmesignin = sharedPreferences?.getString(KEEPMESIGNIN_KEY,"")
        if(keepmesignin.toString() == "TRUE") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}