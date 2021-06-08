package com.example.mobilepoject


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobilepoject.databinding.ActivityProfileBinding
import com.example.mobilepoject.databinding.ActivityMainBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {
    lateinit var rdb: DatabaseReference
    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        rdb = FirebaseDatabase.getInstance().getReference("Profile/people")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<Profile>()
            .setQuery(query, Profile::class.java)
            .build()

        binding.apply {
            fun clearInput(){
                pname.text.clear()
                pnumber.text.clear()
                pgrade.text.clear()
                precord.text.clear()
                ptag.text.clear()
            }

            button.setOnClickListener {
                try {
                    val item = Profile(pname.text.toString()
                        ,pnumber.text.toString().toInt(), pgrade.text.toString(),
                        precord.text.toString(), ptag.text.toString())
                    rdb.child(pname.text.toString()).setValue(item)//ID
                    Toast.makeText(this@ProfileActivity,"프로필 저장", Toast.LENGTH_SHORT).show()

                } catch (e: java.lang.NumberFormatException){
                    Toast.makeText(this@ProfileActivity,"번호에 '-'를 제외한 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
//                clearInput()

            }


        }
    }
}