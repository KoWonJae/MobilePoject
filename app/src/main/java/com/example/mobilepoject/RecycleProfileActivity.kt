package com.example.mobilepoject

import android.content.Intent
import android.net.Uri
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
        binding = ActivityRecycleProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "프로필"
        init()
    }

    fun init(){
        val intent = getIntent()
        val uid = intent.getStringExtra("uid")
        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val selfinfo = intent.getStringExtra("selfinfo")
        val tag = intent.getStringExtra("tag")
        val profileImageUrl = intent.getStringExtra("profileImg")
        val career = intent.getStringExtra("career")
        val site = intent.getStringExtra("site")

        binding.apply {
            pname.setText(username)
            pemail.setText(email)
            pnumber.setText(phoneNumber)
            precord.setText(selfinfo)
            ptag.setText(tag)
            psite.setText(site)
            pcareer.setText(career)

            if(profileImageUrl != ""){
                Picasso.get().load(profileImageUrl).into(pimage)
            }
            else{
                Picasso.get().load(R.drawable.round_white_edittext).into(pimage)
            }

            startChatBtn.setOnClickListener {
                val tempUser = User(uid!!, username!!, phoneNumber!!, email!!, selfinfo!!, tag!!, profileImageUrl!!, career!!, site!!)
                val intent = Intent(this@RecycleProfileActivity, ChatActivity::class.java)
                intent.putExtra(USER_KEY, tempUser)
                startActivity(intent)
            }

            psite.setOnClickListener {
                val siteurl = psite.text.toString()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(siteurl))
                startActivity(intent)
            }

        }

    }
}