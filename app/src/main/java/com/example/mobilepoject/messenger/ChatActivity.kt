package com.example.mobilepoject.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.mobilepoject.R
import com.example.mobilepoject.User
import com.example.mobilepoject.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatActivity : AppCompatActivity() {
    companion object {
        val TAG = "Chat Log"
    }

    lateinit var binding: ActivityChatBinding

    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.apply {
            recyclerviewChat.adapter = adapter

            toUser = intent.getParcelableExtra<User>(MessageActivity.USER_KEY)
            supportActionBar?.title = toUser?.username

            getPrevMessages()

            sendChatBtn.setOnClickListener {
                Log.d(TAG, "Attempt to send message")
                sendMessage()
                chatEdittext.text.clear()
            }
        }
    }

    private fun getPrevMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null){
                    //                Log.d(TAG, chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val currentUser = MessageActivity.currentUser
                        adapter.add(ChatFromItem(chatMessage.text, currentUser ?: return))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }

                binding.recyclerviewChat.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    // Firebase로 메시지 보내기
    private fun sendMessage() {
        binding.apply {
            val text = chatEdittext.text.toString()

            val fromId = FirebaseAuth.getInstance().uid
            if (fromId == null) return

            val user = intent.getParcelableExtra<User>(MessageActivity.USER_KEY)
            val toId = user!!.uid

            val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

            val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

            val chatMessage = ChatMessage(ref.key!!, text, fromId, toId, System.currentTimeMillis()/1000)

            ref.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved our chat message: ${ref.key}")
                    chatEdittext.text.clear()
                    recyclerviewChat.scrollToPosition(adapter.itemCount - 1)
                }
            toRef.setValue(chatMessage)

            val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
            latestMessageRef.setValue(chatMessage)
            val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
            latestMessageToRef.setValue(chatMessage)
        }
    }

}

class ChatFromItem(val text: String, val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.from_textview).text = text

        if(user.profileImageUrl != ""){
            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.findViewById<ImageView>(R.id.from_imageview))
        }
        else{
            Picasso.get().load(R.drawable.round_white_edittext).into(viewHolder.itemView.findViewById<ImageView>(R.id.from_imageview))
        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String, val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.to_textview).text = text

        // 사용자 프로필 사진 보여주기
        if(user.profileImageUrl != ""){
            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.findViewById<ImageView>(R.id.to_imageview))
        }
        else{
            Picasso.get().load(R.drawable.round_white_edittext).into(viewHolder.itemView.findViewById<ImageView>(R.id.from_imageview))
        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}