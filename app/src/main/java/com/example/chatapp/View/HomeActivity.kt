package com.example.chatapp.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.Model.User
import com.example.chatapp.Adapter.UserAdapter
import com.example.chatapp.databinding.ActivityHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private  var userList= ArrayList<User>()
    private lateinit var userAdapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        db = Firebase.firestore

        if(auth.currentUser != null) {
            getName()
            getData()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList,true)
        binding.recyclerView.adapter = userAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logOut){
            val intent = Intent(this, SignInActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getName(){
        val currentUser = auth.currentUser?.uid
        if(currentUser != null){
            db.collection("Users").document(currentUser).get().addOnSuccessListener { document ->

                if(document !=null){
                    val name = document.getString("name")
                    supportActionBar?.title = name
                }
            }
        }

    }


    @SuppressLint("NotifyDataSetChanged")

    private fun getData() {
        db.collection("Users").addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (value != null && !value.isEmpty) {
                    val documents = value.documents
                    userList.clear()
                    for (document in documents) {
                        val name = document.getString("name")
                        val uid = document.getString("uid")
                        val url = document.getString("url")
                        val status = document.getString("status") ?: "offline"
                        if (name != null && uid != null && url!=null) {
                            val user = User(name, uid, url, status)
                            if(auth.currentUser?.uid != user.uuid) {
                                userList.add(user)
                            }
                        }
                    }
                    userAdapter.notifyDataSetChanged()
                }
            }
        }

    }
    private fun getStatus( status:String){
        val reference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
        val user= hashMapOf<String,Any>()
        user["status"] = status
        reference.update(user).addOnSuccessListener {
            Toast.makeText(this,"Status updated!!",Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Status can not be updated!!",Toast.LENGTH_LONG).show()
        }

    }
    override fun onResume() {
        super.onResume()
        getStatus("online")
    }

    override fun onPause() {
        super.onPause()
        getStatus("offline")
    }


}









