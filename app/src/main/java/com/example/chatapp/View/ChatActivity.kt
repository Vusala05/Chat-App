package com.example.chatapp.View

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.Model.Message
import com.example.chatapp.Adapter.MessageAdapter
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("NAME_SHADOWING")
class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit  var messageList: ArrayList<Message>
    private lateinit var firebasefirestore: FirebaseFirestore
    var receiverRoom : String?=null
    var senderRoom : String?=null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        messageList = ArrayList()
        binding.chatRecycler.layoutManager = LinearLayoutManager(this)
        messageAdapter  = MessageAdapter(this,messageList)
        binding.chatRecycler.adapter = messageAdapter

        //adding the message with helping of fireStore
        firebasefirestore = FirebaseFirestore.getInstance()

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        supportActionBar?.title = name

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid +receiverUid


// Göndərən otağı üçün referens yaradılır
        val senderRoomRef = firebasefirestore.collection("chats").document(senderRoom!!).collection("messages")

// Firestore-da dəyişiklikləri dinləyin
        senderRoomRef.orderBy("timestamp").addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Səhv baş veribsə, bunu idarə edin
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                // Mesaj siyahısını təmizləyirik
                messageList.clear()

                // Bütün mesajları snapshot-dan götürürük
                for (document in snapshot.documents) {
                    val message = document.toObject(Message::class.java)
                    if (message != null) {
                        messageList.add(message)
                    }
                }

                // RecyclerView adapterini yeniləyirik
                messageAdapter.notifyDataSetChanged()
            }
        }

        binding.send.setOnClickListener {

            val message = binding.chatting.text.toString()


// Mesajları göndərən və alan istifadəçilərin otaqları üçün referenceləri yaradın
            val senderRoomRef = firebasefirestore.collection("chats").document(senderRoom!!).collection("messages")
            val receiverRoomRef = firebasefirestore.collection("chats").document(receiverRoom!!).collection("messages")

// Mesaj obyektini yaradın və hər iki otağa mesajı əlavə edin
            val messageObject = hashMapOf<String,Any>()
                messageObject["message"] = message
                messageObject["senderId"] = senderUid!!
                messageObject["timestamp"] = FieldValue.serverTimestamp()


// Göndərən otağa mesajı əlavə edin
            senderRoomRef.add(messageObject).addOnSuccessListener {
                // Göndərmə uğurlu oldu, qəbul edən otağa mesajı əlavə edin
                receiverRoomRef.add(messageObject)
                binding.chatting.setText("")
            }


        }

    }
}