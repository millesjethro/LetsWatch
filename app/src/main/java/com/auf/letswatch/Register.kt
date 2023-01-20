package com.auf.letswatch

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.auf.letswatch.databinding.ActivityRegisterBinding
import com.google.firebase.database.*

class Register : AppCompatActivity(), View.OnClickListener{

    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityRegisterBinding
    private var isPassCorrect = false
    private var isUserNameCorrect = false

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideMenus()
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://letswatch-de7ce-default-rtdb.firebaseio.com/")
        binding.edttextUsername.addTextChangedListener(object : TextWatcher, ValueEventListener {

            override fun afterTextChanged(s: Editable) {
                database.child("Users").addListenerForSingleValueEvent(this)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(binding.edttextUsername.text?.length!! <6){
                    binding.edttextUsername.error = "Minimun of 6 characters"
                    isUserNameCorrect = false
                }
                else{
                    isUserNameCorrect = true
                }
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(binding.edttextUsername.text.toString())){
                    binding.edttextUsername.error = "Username Already Used"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        binding.edttextPassword.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(binding.edttextPassword.text?.length!! < 6){
                    binding.edttextPassword.error = "Minimum of 6 Characters"
                    isPassCorrect = false
                }
                else if(!checkString(binding.edttextPassword.text.toString())){
                    binding.edttextPassword.error = "Must Have at Least 1 Lowercase and Uppercase"
                    isPassCorrect = false
                }
                else if(binding.edttextPassword.text.toString() == binding.edttextUsername.text.toString()){
                    binding.edttextPassword.error = "Must not be the same as Username"
                    isPassCorrect = false
                }
                else{
                    isPassCorrect = true
                }
            }
        })
        binding.btnBack.setOnClickListener(this)
        binding.btnRRegister.setOnClickListener(this)
        listen()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            (R.id.btn_back)->{
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
            (R.id.btnR_register)->{
                if(binding.edttextEmail.text?.isEmpty() == true || binding.edttextLastname.text?.isEmpty() == true || binding.edttextFirstname.text?.isEmpty() == true || binding.edttextMidname.text?.isEmpty() == true || binding.edttextPassword.text?.isEmpty() == true || binding.editTextDate.text?.isEmpty() == true){
                    Toast.makeText(applicationContext, "Need All Fields to be filled", Toast.LENGTH_SHORT).show()
                }
                else if (!isPassCorrect ||  !isUserNameCorrect){
                    Toast.makeText(applicationContext, "Password and Username Must be in Correct Format", Toast.LENGTH_SHORT).show()
                }
                else{
                    val uname = ""+binding.edttextUsername.text
                    val name = ""+binding.edttextFirstname.text+" "+binding.edttextMidname.text+" "+binding.edttextLastname.text
                    val email = ""+binding.edttextEmail.text
                    val passowrd = ""+binding.edttextPassword.text
                    val date = ""+binding.editTextDate.text
                    writeNewUser(uname,email,name,passowrd,date)
                    Toast.makeText(applicationContext, "You Have Successfully Registered", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }

            }
        }
    }

    @IgnoreExtraProperties
    data class User(
        val email: String? = null,
        val username: String? = null,
        val pass: String? = null,
        val bdate: String? = null) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }

    fun writeNewUser(username: String, email: String, name: String, pass: String, bdate: String) {
        val user = User(email ,name, pass, bdate)

        database.child("Users").child(username).setValue(user)
    }



    @RequiresApi(Build.VERSION_CODES.R)
    fun hideMenus(){
        supportActionBar?.hide()
        window.insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        window.insetsController?.hide(WindowInsets.Type.navigationBars())
    }

    fun listen() {
        binding.editTextDate.addTextChangedListener(mDateEntryWatcher)
    }

    private val mDateEntryWatcher = object : TextWatcher {

        var edited = false
        val dividerCharacter = "/"
        //Date text changed
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (edited) {
                edited = false
                return
            }

            var working = getEditText()

            working = manageDateDivider(working, 2, start, before)
            working = manageDateDivider(working, 2, start, before)
            working = manageDateDivider(working, 5, start, before)

            edited = true
            binding.editTextDate.setText(working)
            binding.editTextDate.setSelection(binding.editTextDate.text?.length!!)
        }
        //Divider for the day month and year
        private fun manageDateDivider(working: String, position : Int, start: Int, before: Int) : String{
            if (working.length == position) {
                return if (before <= position && start < position)
                    working + dividerCharacter
                else
                    working.dropLast(1)
            }
            return working
        }
        //If the date is already at the length
        private fun getEditText() : String {
            return if (binding.editTextDate.text?.length!! >= 10)
                binding.editTextDate.text.toString().substring(0,10)
            else
                binding.editTextDate.text.toString()
        }

        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    }

    private fun checkString(str: String): Boolean {
        var ch: Char
        var capitalFlag = false
        var lowerCaseFlag = false
        for (i in 0 until str.length) {
            ch = str[i]
            if (Character.isUpperCase(ch)) {
                capitalFlag = true
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true
            }
            if (capitalFlag && lowerCaseFlag) return true
        }
        return false
    }

}

