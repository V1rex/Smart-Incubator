package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import com.google.firebase.auth.FirebaseAuth
import com.v1rex.smartincubator.R

class MainActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth? = null
    private var mLoginBtn: Button? = null
    private var mRegisterBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLoginBtn = findViewById<View>(R.id.login_btn) as Button

        mLoginBtn!!.setOnClickListener { startActivity(Intent(this@MainActivity, LoginActivity::class.java)) }

        mRegisterBtn = findViewById<View>(R.id.register_btn) as Button

        mRegisterBtn!!.setOnClickListener { startActivity(Intent(this@MainActivity, RegisterActivity::class.java)) }

    }

}
