package com.example.mobilepoject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilepoject.databinding.ActivityMainBinding
import com.example.mobilepoject.messenger.MessageActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var rdb: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()
        rdb = FirebaseDatabase.getInstance().getReference("Profile/people")

        if(firebaseAuth.currentUser == null) {
            // 계정이 로그인 되어있지 않으면 LoginActivity로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // 로그인 되어있다면 사용자의 프로필 출력
            val uid = firebaseAuth.currentUser?.uid
            rdb.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //TODO("Not yet implemented")
                    binding.apply {
                        pemail.text = snapshot.child(uid!!).child("email").value.toString()
                        pname.text = snapshot.child(uid!!).child("name").value.toString()
                        pnumber.text = snapshot.child(uid!!).child("phoneNumber").value.toString()
                        ptag.text = snapshot.child(uid!!).child("tag").value.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //TODO("Not yet implemented")
                }
            })
        }

        binding.searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding.settingBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // 채팅 목록으로 가기
        binding.chatBtn.setOnClickListener {
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        }


//        var userEmail = intent.getStringExtra("email")
//        binding.pemail.setText(userEmail)
//        val query1 = rdb.orderByChild("email").equalTo(userEmail)
//        val rdb2 = FirebaseDatabase.getInstance().getReference("Profile/people/kwj12132@naver").child("email")
//        print(rdb2)


//        val query = rdb.child(pname.text.toString()).child("name")


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_menu) {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}