package com.example.mobilepoject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mobilepoject.databinding.ActivityLoginBinding
import com.example.mobilepoject.messenger.MessageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
                    //finish()
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
                // 뒤로가기 눌러도 다시 이 화면으로 안 돌아옴
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

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

                    val uid = firebaseAuth.uid
                    // 파이어베이스 Profiles -> users 로 통합
                    val ref = FirebaseDatabase.getInstance().getReference("/users/people/$uid")
                    ref.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            MessageActivity.currentUser = snapshot.getValue(User::class.java)
                            val username = MessageActivity.currentUser?.username
                            Toast.makeText(this@LoginActivity, username + " 님, 환영합니다.", Toast.LENGTH_SHORT).show()
                            Log.d("User", "Current User ${MessageActivity.currentUser?.username}")
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                } else {
                    Toast.makeText(this, "로그인 실패.", Toast.LENGTH_LONG).show()
                    binding.editTextEmail.text.clear()
                    binding.editTextPassword.text.clear()
                }
            }
    }
}