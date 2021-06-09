package com.example.mobilepoject


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobilepoject.databinding.ActivityProfileBinding
import com.example.mobilepoject.databinding.ActivityMainBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
                //pname.text.clear()
                pnumber.text.clear()
                pemail.text.clear()
                precord.text.clear()
                ptag.text.clear()
            }

            fun reviseEmail(email :String) : String {
                val rEmail = email.replace(".com", "")
                return rEmail
            }

            // 현재 로그인한 사용자의 정보로 editText text 변경
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            rdb.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //TODO("Not yet implemented")
                    binding.apply {
                        pemail.setText(snapshot.child(uid!!).child("email").value.toString())
                        pname.setText(snapshot.child(uid!!).child("name").value.toString())
                        precord.setText(snapshot.child(uid!!).child("selfinfo").value.toString())
                        pnumber.setText(snapshot.child(uid!!).child("phoneNumber").value.toString())
                        ptag.setText(snapshot.child(uid!!).child("tag").value.toString())
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    //TODO("Not yet implemented")
                }
            })

            button.setOnClickListener {
//                try {
//                    val item = Profile(pname.text.toString()
//                        ,pnumber.text.toString().toInt(), pemail.text.toString(),
//                        precord.text.toString(), ptag.text.toString())
//                    rdb.child(reviseEmail(pemail.text.toString())).setValue(item)//ID
//                    Toast.makeText(this@ProfileActivity,"프로필 저장", Toast.LENGTH_SHORT).show()
//
//                } catch (e: java.lang.NumberFormatException){
//                    Toast.makeText(this@ProfileActivity,"번호에 '-'를 제외한 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show()
//                }
//                clearInput()

                // 버튼을 누르면 editText text로 사용자의 데이터 변경
                try {
                    val item = Profile(pname.text.toString()
                        ,pnumber.text.toString().toInt(), pemail.text.toString(),
                        precord.text.toString(), ptag.text.toString())
                    rdb.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(item)//ID
                    Toast.makeText(this@ProfileActivity,"프로필 저장", Toast.LENGTH_SHORT).show()

                } catch (e: java.lang.NumberFormatException){
                    Toast.makeText(this@ProfileActivity,"번호에 '-'를 제외한 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
//                clearInput()
            }




        }
    }
}