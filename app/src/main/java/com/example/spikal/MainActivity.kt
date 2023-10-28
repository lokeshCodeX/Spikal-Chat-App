package com.example.spikal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.example.spikal.Activity.NumberActivity

import com.example.spikal.adapter.ViewPagerAdapter
import com.example.spikal.databinding.ActivityMainBinding
import com.example.spikal.ui.CallFragment
import com.example.spikal.ui.ChatFragment
import com.example.spikal.ui.StatusFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding?=null
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding!!.root)


        val fragmentArrayList=ArrayList<Fragment>()
        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())

        auth= FirebaseAuth.getInstance()

        if (auth.currentUser ==null){

            val intent=Intent(this@MainActivity,NumberActivity::class.java)
            startActivity(intent)
            finish()
        }

        val adapter=ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)
        binding!!.viewPager.adapter=adapter

        binding!!.tabs.setupWithViewPager(binding!!.viewPager)





    }
}