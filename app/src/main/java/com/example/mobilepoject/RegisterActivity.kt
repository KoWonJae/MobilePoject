package com.example.mobilepoject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobilepoject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var binding : ActivityRegisterBinding
    lateinit var rdb :DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "회원가입"
        init()
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonRegister.setOnClickListener {
            // 새로만들 계정의 아이디 비밀번호를 입력받음
            if (binding.editTextEmail.text.toString() != "" && binding.editTextPassword.text.toString() != "") {
                createUser(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString())
            } else if (binding.editTextPassword.text.toString().length < 6) {
                Toast.makeText(this, "비밀번호는 최소 6자리입니다.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createUser(email : String, password:String) {
        // 계정을 생성하는 함수
        firebaseAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if(!task.isSuccessful){
                    Log.d("Register", "계정 생성 실패")
                    return@addOnCompleteListener
                } else if(task.isCanceled){
                    Toast.makeText(this, "계정 생성 취소.", Toast.LENGTH_LONG).show()
                    return@addOnCompleteListener
                }

                Log.d("Main", "계정 생성 성공 with uid: ${task.result!!.user?.uid}")
                Toast.makeText(this, "계정 생성 성공.", Toast.LENGTH_LONG).show()

                // 회원정보 파이어베이스 데이터베이스에 등록(Firebase Database & Storage)
                saveUserToFirebaseDatabase()

            }.addOnFailureListener {
                Toast.makeText(this@RegisterActivity, "계정 생성 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.d("Main", "계정 생성 실패: ${it.message}")
            }
    }

    // Firebase Database에 등록한 사람 추가 (no 사진)
    private fun saveUserToFirebaseDatabase() {
        val user = User()
        val uid = firebaseAuth.uid!!
        user.uid = uid
        user.email = binding.editTextEmail.text.toString()
        user.username = binding.nameEdit.text.toString()

        rdb = FirebaseDatabase.getInstance().getReference("users/people/$uid")
        rdb.setValue(user)
            .addOnSuccessListener {
                Log.i("Register", "유저 등록됨")

                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Log.d("Register", "유저 등록 실패: ${it.message}")
            }
    }

}