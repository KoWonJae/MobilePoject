package com.example.mobilepoject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilepoject.databinding.ActivityRecycleProfileBinding


class RecycleProfileActivity : AppCompatActivity() {
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
        val intent = getIntent();
        val myData = intent.getStringExtra("my_data")
        val grade = intent.getStringExtra("grade")
        val number = intent.getIntExtra("phoneNumber", 0)
        val record = intent.getStringExtra("record")
        val tag = intent.getStringExtra("tag")


        binding.apply {
            pname.setText(myData)
            pemail.setText(grade)
            pnumber.setText(number.toString())
            precord.setText(record)
            ptag.setText(tag)

        }

    }
}