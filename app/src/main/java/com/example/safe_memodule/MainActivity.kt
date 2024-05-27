package com.example.safe_memodule

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val lgnbtn = findViewById<Button>(R.id.lgnbtn);
        lgnbtn.setOnClickListener{
            val Intent = Intent(this,LoginActivity::class.java)
            startActivity(Intent);
        }
        val Regbtn = findViewById<Button>(R.id.Regbtn);
        Regbtn.setOnClickListener{
            val Intent = Intent(this,RegistrationActivity::class.java)
            startActivity(Intent);
        }
    }
}