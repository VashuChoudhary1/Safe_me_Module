package com.example.safe_memodule

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.safe_memodule.databinding.ActivityLoginBinding
import com.example.safe_memodule.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private  lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
     //   setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.lgnbtn.setOnClickListener{
            val email = binding.lgnEmail.text.toString()
            val password = binding.lgnPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }

            }
            else{
                Toast.makeText(this,"Fields cannot be empty",Toast.LENGTH_SHORT).show()
            }
        }
        binding.regRedirectText.setOnClickListener{
            val regIntent = Intent(this,RegistrationActivity::class.java)
            startActivity(regIntent)
        }

    }
}