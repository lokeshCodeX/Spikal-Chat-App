package com.example.spikal.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.spikal.MainActivity
import com.example.spikal.databinding.ActivityProfileBinding
import com.example.spikal.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class ProfileActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var storage:FirebaseStorage
    private lateinit var selectImg:Uri
    private lateinit var dialog: AlertDialog.Builder





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dialog=AlertDialog.Builder(this)
            .setMessage("Profile updating...")
            .setCancelable(false)


        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        storage=FirebaseStorage.getInstance()


        binding.imgUserImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.continueBtn.setOnClickListener{
            val userName = binding.userName.text.toString().trim()

            if (userName.isEmpty()){
                Toast.makeText(this, "Please enter the name", Toast.LENGTH_SHORT).show()
            } else if (selectImg == null){
                Toast.makeText(this, "Please upload your Image", Toast.LENGTH_SHORT).show()
            } else {
                uploadData()
            }
        }

    }


    private fun uploadData(){

        val reference=storage.reference.child("Profile").child(Date().time.toString())


        reference.putFile(selectImg).addOnCompleteListener{

            if(it.isSuccessful){


                reference.downloadUrl.addOnSuccessListener { task ->

                    uploadInfo(task.toString())
                }
            }
        }

    }


    private fun uploadInfo(imgUrl:String){


        val user=UserModel(auth.uid.toString(),binding.userName.text .toString(),auth.currentUser?.phoneNumber.toString(),imgUrl)


        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                // Success listener
                Toast.makeText(this, "Data inserted..", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { exception ->
                // Failure listener
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data !=null) {

            if (data.data != null) {
                selectImg = data.data!!

                binding.imgUserImage.setImageURI(selectImg)
            }
        }
    }
}