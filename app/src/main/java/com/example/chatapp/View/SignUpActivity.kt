package com.example.chatapp.View

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.util.UUID
import kotlin.random.Random

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebasefirestore: FirebaseFirestore
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var storage: FirebaseStorage
    private var selectedPicture : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()
       auth = Firebase.auth
        firebasefirestore = Firebase.firestore
        storage = Firebase.storage
        registerLauncher()

        binding.toSignIn.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

     fun signUp(view: View) {
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)
        if(selectedPicture != null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    val name = binding.name.text.toString()
                    val password = binding.newPassword.text.toString()
                    val email = binding.emailUp.text.toString()
                    val confirmPass  = binding.confirmPassword.text.toString()
                    if (password.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty() && confirmPass.isNotEmpty()){
                        if (password == confirmPass) {
                            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                                val user = hashMapOf<String,Any>()
                                user["name"] = binding.name.text.toString()
                                user["email"] = auth.currentUser!!.email!!
                                user["time"] = Timestamp.now()
                                user["uid"] = auth.currentUser!!.uid
                                user["url"] = downloadUrl

                                firebasefirestore.collection("Users").document(auth.currentUser!!.uid).set(user).addOnSuccessListener {
                                    val intent = Intent(this, HomeActivity::class.java)
                                     intent.putExtra("userName", name)
                                    startActivity(intent)
                                    finish()
                                }.addOnFailureListener { result->
                                    Toast.makeText(this,result.localizedMessage,Toast.LENGTH_LONG).show()

                                }

                            }.addOnFailureListener {
                                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()

                            }
                        }else {
                            Toast.makeText(this, "Enter same password!!!", Toast.LENGTH_LONG).show()
                        }
                    } else{
                        Toast.makeText(this,"Fill all ones!!!",Toast.LENGTH_LONG).show()
                    }


                }

            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }

    fun selectImage(view: View) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)) {
                Snackbar.make(view, "Permission need for status!!!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give permission") {
                        //request permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
            } else{
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            } else{
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
                    //start activity for result
        }
        }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult !=null) {
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let {
                        binding.image.setImageURI(it)
                    }
                }
            }

        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ boolean->
            if(boolean){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else{
                //permission denied
                Toast.makeText(this,"Permission needed!!!",Toast.LENGTH_LONG).show()

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


