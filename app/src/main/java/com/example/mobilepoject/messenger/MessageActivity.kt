package com.example.mobilepoject.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mobilepoject.User
import com.example.mobilepoject.databinding.ActivityMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class MessageActivity : AppCompatActivity() {
    companion object {
        val USER_KEY = "USER_KEY"
        var currentUser: User? = null
        val TAG = "latest messages"
    }

    lateinit var binding: ActivityMessageBinding
    // Groupie 사용 (recyclerview 와 비슷한데 더 편리하다고 함)
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "채팅"
        init()
    }

    private fun init(){
        binding.apply {
            recyclerViewMessage.adapter = adapter
            recyclerViewMessage.addItemDecoration(DividerItemDecoration(this@MessageActivity, DividerItemDecoration.VERTICAL))

            // 클릭시 채팅창 나오게 하기 (use with fetchMessagingUsers)
            adapter.setOnItemClickListener { item, view ->
                val row = item as LatestMessageItem

                val intent = Intent(this@MessageActivity, ChatActivity::class.java)
                intent.putExtra(USER_KEY, row.chatPartnerUser)
                startActivity(intent)
            }

            fetchMe()

            // [테스트용] Firebase에 등록된 모든 사용자 목록
//            fetchUsers()

            // [실전용] 나와 채팅중인 사용자 목록
            fectchMessagingUsers()
        }
    }

    val latestMessageMap = HashMap<String, ChatMessage>()

    private fun fectchMessagingUsers() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return

                latestMessageMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                adapter.add(LatestMessageItem(chatMessage))

                latestMessageMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessageMap.values.forEach {
            adapter.add(LatestMessageItem(it))
        }
    }

    // Firebase에서 전체 유저 정보 가져와서 리스트로 보여주기
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/people")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))
                    }
                }

                // 클릭시 채팅 화면 나오기
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem

                    val intent = Intent(view.context, ChatActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    // Firebase에서 내 정보 가져오기
    private fun fetchMe() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/people/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Log.d("User", "Current User ${currentUser?.username}")
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}
