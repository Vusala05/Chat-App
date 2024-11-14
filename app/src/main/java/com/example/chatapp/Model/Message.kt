package com.example.chatapp.Model

class Message(
    val message: String = "",  // Default dəyər əlavə edin
    val senderId: String = ""   // Default dəyər əlavə edin
) {

    constructor() : this("", "")
}
