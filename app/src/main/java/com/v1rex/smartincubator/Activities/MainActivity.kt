package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_btn.setOnClickListener { startActivity(Intent(this@MainActivity, LoginActivity::class.java)) }

        register_btn.setOnClickListener { startActivity(Intent(this@MainActivity, RegisterActivity::class.java)) }

    }

}
