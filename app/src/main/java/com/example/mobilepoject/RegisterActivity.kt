package com.example.mobilepoject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobilepoject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                if (task.isSuccessful) {
                    Toast.makeText(this, "계정 생성 성공.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    val user = firebaseAuth?.currentUser
                    startActivity(intent)
                    //updateUI()
                } else if(task.isCanceled) {
                    Toast.makeText(this, "계정 생성 취소.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "계정 생성 실패.", Toast.LENGTH_LONG).show()
                    //updateUI()
                }
            }
    }
}