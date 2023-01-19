package com.auf.letswatch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.auf.letswatch.R
import com.auf.letswatch.databinding.FragmentAccountSettingsBinding
import com.auf.letswatch.databinding.FragmentFavoriteBinding
import com.google.firebase.database.*
import java.util.*


class AccountSettings : Fragment(), ValueEventListener {
    private var _binding: FragmentAccountSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountSettingsBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://letswatch-de7ce-default-rtdb.firebaseio.com/")
        database.child("Users").addListenerForSingleValueEvent(this)
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PROFILE", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString(USERNAME_KEY,"")
        var isThesame = false
        binding.btnChangepass.isEnabled = false

        binding.btnLogout.setOnClickListener{
            val editor = sharedPreferences?.edit()
            editor?.putString(KEEPMESIGNIN_KEY,"FALSE")
            editor?.putString(USERNAME_KEY,"")
            editor?.apply()

            val intent = Intent(context, Login::class.java)
            startActivity(intent)

        }

        binding.edttextOldpass.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                database.child("Users").addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.hasChild(username.toString())){
                            val getPass = snapshot.child(username.toString()).child("pass").value.toString()
                            if(getPass == binding.edttextOldpass.text.toString()){
                                binding.btnChangepass.isEnabled = true
                                isThesame = true
                            }
                            else if(binding.edttextOldpass.text.toString() == ""){
                                binding.btnChangepass.isEnabled = false
                            }
                            else{
                                binding.btnChangepass.isEnabled = false
                                binding.edttextOldpass.error = "Wrong Old pass"
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }
        })

        binding.btnChangepass.setOnClickListener{
            if(isThesame == true) {
                val childUpdates =
                    hashMapOf<String, Any>("pass" to binding.edttextNewpass.text.toString())
                database.child("Users").child(username.toString()).updateChildren(childUpdates)
                Toast.makeText(context, "Changed Password Successfully", Toast.LENGTH_SHORT).show()
                binding.edttextOldpass.text.clear()
                binding.edttextNewpass.text.clear()
            }
            else{
                Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PROFILE", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString(USERNAME_KEY,"")
        if(snapshot.hasChild(username.toString())){
            val getEmail = snapshot.child(username.toString()).child("email").value.toString()
            val getUsrname = snapshot.child(username.toString()).child("username").value.toString()
            val getBdate = snapshot.child(username.toString()).child("bdate").value.toString()

            binding.txtAccemail.text = getEmail
            binding.txtAccbday.text = getBdate
            binding.txtAccname.text = getUsrname
            binding.txtAccuname.text = username.toString()


        }
    }

    override fun onCancelled(error: DatabaseError) {

    }



}