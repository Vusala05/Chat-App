package com.example.chatapp.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivitySignInBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var firebasefirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        firebasefirestore = Firebase.firestore

        supportActionBar?.hide()
        binding.newAccount.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


    }

    fun signIn(view: View) {

        val passwordSign = binding.password.text.toString()
        val emailSign = binding.email.text.toString()

        if (passwordSign.isNotEmpty()  && emailSign.isNotEmpty()){
                auth.signInWithEmailAndPassword(emailSign,passwordSign).addOnSuccessListener {
                    val intent = Intent(this, HomeActivity::class.java)

                        startActivity(intent)
                        finish()

                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()

                }

        } else{
            Toast.makeText(this,"Fill all ones!!!", Toast.LENGTH_LONG).show()
        }

    }

}


