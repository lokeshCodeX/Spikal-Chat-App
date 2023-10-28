package com.example.spikal.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import com.example.spikal.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please waiting...")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

        val phoneNumber = "+91" + intent.getStringExtra("number")



        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {


                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Log.e("OtpActivity", "Verification failed: ${p0.message}")
                    Toast.makeText(this@OtpActivity, "Verification failed. Please try again.", Toast.LENGTH_SHORT).show()
                }




                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    dialog.dismiss()
                    verificationId = p0

                }



            })

            .build()



        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.button.setOnClickListener {
            if (binding.OtpNumber.text!!.isEmpty()) {
                Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show()
            } else {
                dialog.show()

                val credential = PhoneAuthProvider.getCredential(verificationId, binding.OtpNumber.text.toString())

                auth.signInWithCredential(credential)
                    .addOnCompleteListener {


                        if (it.isSuccessful) {


                            dialog.dismiss()
                            val intent=
                                Intent(this@OtpActivity, ProfileActivity::class.java)

                            startActivity(intent)

                            finish()

                        } else {



                            dialog.dismiss()
                            Toast.makeText(this@OtpActivity, "Error", Toast.LENGTH_SHORT).show()
                        }

                    }


            }
        }

    }
}