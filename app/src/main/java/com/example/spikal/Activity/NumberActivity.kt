package com.example.spikal.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.spikal.MainActivity
import com.example.spikal.R
import com.example.spikal.databinding.ActivityNumberBinding
import com.google.firebase.auth.FirebaseAuth

class NumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNumberBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNumberBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth=  FirebaseAuth.getInstance()

        if (auth.currentUser!=null){

            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            binding.phoneNumber.setOnClickListener{
                if(binding.phoneNumber.text!!.isEmpty()){
                    Toast.makeText(this,"Please enter your phone number",Toast.LENGTH_SHORT).show()

                }
                else{
                    val intent=Intent(this,OtpActivity::class.java)
                    intent.putExtra("number",binding.phoneNumber.text!!.toString())
                    startActivity(intent)
                }
            }
        }
    }




}