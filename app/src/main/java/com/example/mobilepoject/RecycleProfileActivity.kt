package com.example.mobilepoject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilepoject.databinding.ActivityRecycleProfileBinding
import com.example.mobilepoject.messenger.ChatActivity
import com.squareup.picasso.Picasso


class RecycleProfileActivity : AppCompatActivity() {
    companion object{
        val USER_KEY = "USER_KEY"
    }

    lateinit var binding : ActivityRecycleProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_recycle_profile)
        binding = ActivityRecycleProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "프로필"
        init()
    }

    fun init(){
//        va intent = new Intent(ReceiverActivity.this, InputActivity2.class)
        val intent = getIntent()
        val uid = intent.getStringExtra("uid")
        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val selfinfo = intent.getStringExtra("selfinfo")
        val tag = intent.getStringExtra("tag")
        val profileImageUrl = intent.getStringExtra("profileImg")

        binding.apply {
            pname.setText(username)
            pemail.setText(email)
            pnumber.setText(phoneNumber)
            precord.setText(selfinfo)
            ptag.setText(tag)
            if(profileImageUrl != ""){
                Picasso.get().load(profileImageUrl).into(pimage)
            }

            startChatBtn.setOnClickListener {
                val tempUser = User(uid!!, username!!, phoneNumber!!, email!!, selfinfo!!, tag!!, profileImageUrl!!)
                val intent = Intent(this@RecycleProfileActivity, ChatActivity::class.java)
                intent.putExtra(USER_KEY, tempUser)
                startActivity(intent)
            }
        }

    }
}