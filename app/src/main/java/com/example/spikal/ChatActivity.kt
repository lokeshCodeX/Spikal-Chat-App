package com.example.spikal

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isEmpty
import com.example.spikal.adapter.MessageAdater
import com.example.spikal.databinding.ActivityChatBinding
import com.example.spikal.model.MessageModel
import com.example.spikal.ui.ChatFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date




class ChatActivity : AppCompatActivity() {

    private lateinit var binding:ActivityChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var senderUid:String
    private lateinit var recieverUid:String
    private lateinit var senderRoom:String
    private lateinit var recieverRoom:String

    private lateinit var list:ArrayList<MessageModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityChatBinding.inflate(layoutInflater)



        setContentView(binding.root)

        senderUid=FirebaseAuth.getInstance().uid.toString()

        recieverUid=intent.getStringExtra("uid")!!


        senderRoom=senderUid+recieverUid
        recieverRoom=senderUid+recieverUid


        list=ArrayList()



        database=FirebaseDatabase.getInstance()


        binding.imageView2.setOnClickListener(){

            if (binding.messageBox.text.isEmpty()){

                Toast.makeText(this,"please enter your message",Toast.LENGTH_SHORT).show()

            }
            else{


                val message=MessageModel(binding.messageBox.text.toString(),senderUid,Date().time)


                val randomKey=database.reference.push().key

                database.reference.child("chats")
                    .child(senderRoom).child("message").child(randomKey!!).setValue(message)
                    .addOnSuccessListener {


                        database.reference.child("chats")
                            .child(recieverRoom).child("message")
                            .child(randomKey!!).setValue(message)
                            .addOnSuccessListener {

                                binding.messageBox.text=null

                                Toast.makeText(this,"message send.",Toast.LENGTH_SHORT).show()


                            }


                    }




            }



        }


        database.reference.child("chats").child(senderRoom).child("message")

            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {


                    list.clear()

                    for (snapshot1 in snapshot.children)
                    {

                        val data=snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)


                    }

                    binding.recyclerView.adapter = MessageAdater(this@ChatActivity,list)

                }

                override fun onCancelled(error: DatabaseError) {


                    Toast.makeText(this@ChatActivity,"error:$error",Toast.LENGTH_LONG).show()


                }


            })


    }
}