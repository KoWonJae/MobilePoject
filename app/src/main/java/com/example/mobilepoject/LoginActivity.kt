package com.example.mobilepoject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobilepoject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseAuthListener: FirebaseAuth.AuthStateListener
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()

        binding.apply {
            buttonRegister.setOnClickListener {
                // RegisterActivity로 이동
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }

            buttonLogin.setOnClickListener {
                // 아이디 비밀번호를 입력으로 로그인
                if (binding.editTextEmail.text.toString() != "" && binding.editTextPassword.text.toString() != "") {
                    loginUser(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString())
                } else {
                    Toast.makeText(this@LoginActivity, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show()
                }
            }
        }

        firebaseAuthListener = FirebaseAuth.AuthStateListener {
            // 로그인이 되어있으면 MainActivity로 이동
            val firebaseUser = firebaseAuth.currentUser
            if(firebaseUser != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth?.addAuthStateListener { firebaseAuthListener!! }
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth?.addAuthStateListener { firebaseAuthListener!! }
    }

    fun loginUser(email :String, password:String) {
        // 로그인 정보 확인하는 함수
        firebaseAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) {task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "로그인 성공.", Toast.LENGTH_LONG).show()
                    firebaseAuth.addAuthStateListener(firebaseAuthListener)
                } else {
                    Toast.makeText(this, "로그인 실패.", Toast.LENGTH_LONG).show()
                }
            }
    }
}