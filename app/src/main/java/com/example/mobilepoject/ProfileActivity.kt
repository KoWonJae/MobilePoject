package com.example.mobilepoject


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobilepoject.databinding.ActivityProfileBinding
import com.example.mobilepoject.databinding.ActivityMainBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class ProfileActivity : AppCompatActivity() {
    lateinit var rdb: DatabaseReference
    lateinit var binding: ActivityProfileBinding

    // 프로필 사진 선택용
    var selectedPhotoUri: Uri? = null
    val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> binding.pimage.setImageURI(result.data?.data)
        selectedPhotoUri = result.data?.data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "프로필 수정"
        init()
    }

    private fun init(){
        rdb = FirebaseDatabase.getInstance().getReference("users/people")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()

        binding.apply {
            fun clearInput(){
                pemail.text.clear()
                pnumber.text.clear()
                ptag.text.clear()
                precord.text.clear()
            }

            fun reviseEmail(email :String) : String {
                val rEmail = email.replace(".com", "")
                return rEmail
            }

            // 프로필 이미지 선택
            pimage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                getContent.launch(intent)
            }

            // 현재 로그인한 사용자의 정보로 editText text 변경
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            rdb.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.apply {
                        if(uid != null) {
                            val currentUser = snapshot.child(uid)

                            // 프로필 사진 있으면 보여주기
                            if(currentUser.child("profileImageUrl").value.toString() != ""){
                                Picasso.get().load(currentUser.child("profileImageUrl").value.toString()).into(pimage)
                            }
                            else{
                                Picasso.get().load(R.drawable.round_white_edittext).into(pimage)
                            }
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

            button.setOnClickListener {
                // 버튼을 누르면 editText text로 사용자의 데이터 변경
                try {
                    // 프로필 이미지 있는지 확인. 있으면 파이어베이스 storage에 저장
                    var profileImageUrl = ""

                    // 프로필 사진 있으면 바꾸기 or 추가하기
                    if(selectedPhotoUri != null){
                        val filename = UUID.randomUUID().toString()
                        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

                        ref.putFile(selectedPhotoUri!!)
                            .addOnSuccessListener {
                                Log.d("Register", "이미지 업로드 성공: ${it.metadata?.path}")

                                ref.downloadUrl.addOnSuccessListener {
                                    Log.d("Register", "파일 위치: $it")

                                    profileImageUrl = it.toString()

                                    // 파이어베이스 Profile -> user 로 통합
                                    changeProfile(profileImageUrl)
                                }
                            }.addOnFailureListener {
                            }
                    }
                    // 프로필 사진 없으면 이미지 공백으로 두기
                    else{
                        changeProfile(profileImageUrl)
                    }
                } catch (e: java.lang.NumberFormatException){
                    Toast.makeText(this@ProfileActivity,"번호에 '-'를 제외한 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                finish()
            }

        }
    }

    private fun changeProfile(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val item = User(uid, binding.pname.text.toString()
            ,binding.pnumber.text.toString(), binding.pemail.text.toString(),
            binding.precord.text.toString(), binding.ptag.text.toString(), profileImageUrl, binding.psite.text.toString(), binding.pcareer.text.toString())
        rdb.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(item)//ID
        Toast.makeText(this@ProfileActivity,"프로필 저장", Toast.LENGTH_SHORT).show()
    }
}