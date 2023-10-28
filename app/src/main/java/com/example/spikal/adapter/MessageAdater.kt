package com.example.spikal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spikal.R
import com.example.spikal.databinding.RecieverItemLayoutBinding
import com.example.spikal.databinding.SentItemLayoutBinding
import com.example.spikal.model.MessageModel
import com.google.firebase.auth.FirebaseAuth

class MessageAdater(var context: Context,var list:ArrayList<MessageModel>):RecyclerView.Adapter<RecyclerView.ViewHolder> (){


    var ITEM_SENT=1
    var ITEM_RECIEVE=2



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return if (viewType == ITEM_SENT)
            SentViewHolder(
                LayoutInflater.from(context).inflate(R.layout.sent_item_layout,parent,false
                )
            )
        else

                RecieverViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.reciever_item_layout,parent,false
                    )
                )



    }


    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().uid==list[position].senderId) ITEM_SENT
        else ITEM_RECIEVE
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message =list[position]

        if(holder.itemViewType==ITEM_SENT){

            val viewHolder=holder as SentViewHolder
            viewHolder.binding.userMessage.text=message.message

        }
        else{

            val viewHolder=holder as RecieverViewHolder

            viewHolder.binding.recieverMessage.text=message.message

        }




    }



    inner class SentViewHolder(view:View):RecyclerView.ViewHolder(view){

        var binding=SentItemLayoutBinding.bind(view)
    }

    inner class RecieverViewHolder(view:View):RecyclerView.ViewHolder(view){

        var binding=RecieverItemLayoutBinding.bind(view)
    }



}