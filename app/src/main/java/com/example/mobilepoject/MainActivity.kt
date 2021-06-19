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
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var rdb: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "내 프로필"
        init()
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()
        // 파이어베이스 Profiles -> users 로 통합
        rdb = FirebaseDatabase.getInstance().getReference("users/people")

        if(firebaseAuth.currentUser == null) {
            // 계정이 로그인 되어있지 않으면 LoginActivity로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
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

        // 현재 로그인한 사용자의 정보로 editText text 변경
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        rdb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    if(uid != null) {
                        val currentUser = snapshot.child(uid)
                        // 프로필 이미지 그리기
                        if(currentUser.child("profileImageUrl").value.toString() != ""){
                            Picasso.get().load(currentUser.child("profileImageUrl").value.toString()).into(pimage)
                        }
                        else{
                            Picasso.get().load(R.drawable.round_white_edittext).into(pimage)
                        }
                        // 프로필 텍스트 넣기
                        pname.setText(currentUser.child("username").value.toString())
                        pemail.setText(currentUser.child("email").value.toString())
                        precord.setText(currentUser.child("selfinfo").value.toString())
                        pnumber.setText(currentUser.child("phoneNumber").value.toString())
                        ptag.setText(currentUser.child("tag").value.toString())
                        pcareer.setText(currentUser.child("career").value.toString())
                        psite.setText(currentUser.child("site").value.toString())
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

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