package com.example.mobilepoject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobilepoject.databinding.ActivityRegisterBinding
import com.example.mobilepoject.messenger.MessageActivity
import com.example.mobilepoject.messenger.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var binding : ActivityRegisterBinding
    lateinit var rdb :DatabaseReference
    var selectedPhotoUri: Uri? = null
    // startActivityForResult가 이걸로 바뀜
    val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> binding.photoBtn.setImageURI(result.data?.data)
        selectedPhotoUri = result.data?.data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()

        // 갤러리에서 이미지 선택
        binding.photoBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            // startActivityForResult가 이걸로 바뀜
            getContent.launch(intent)
        }

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

//                Log.d("Main", "계정 생성 성공 with uid: ${task.result!!.user?.uid}")
                Toast.makeText(this, "계정 생성 성공.", Toast.LENGTH_LONG).show()

                // 가입한 사용자의 임시 프로필을 만들어 데이터베이스에 삽입
                val profile = Profile()
                profile.email = email
                profile.name = binding.nameEdit.text.toString()
                rdb = FirebaseDatabase.getInstance().getReference("Profiles/people")
                rdb.child(firebaseAuth?.currentUser!!.uid).setValue(profile)

                val loginUser = firebaseAuth?.currentUser

                // 회원정보 등록(Firebase Database & Storage)
                // 파라미터 제거
                saveUserToFirebaseDatabase()

            }.addOnFailureListener {
                Toast.makeText(this@RegisterActivity, "계정 생성 실패: ${it.message}", Toast.LENGTH_SHORT).show()
//                Log.d("Main", "계정 생성 실패: ${it.message}")
            }
    }

    // Firebase Database에 회원 등록한 사람 추가
    private fun saveUserToFirebaseDatabase() {
        //파라미터 제거

        if(selectedPhotoUri == null) return

        val uid = firebaseAuth.uid!!
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, binding.nameEdit.text.toString(), selectedPhotoUri.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.i("Register", "유저 등록됨")

                uploadImageToFirebaseStorage()

                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
//                Log.d("Register", "유저 등록 실패: ${it.message}")
            }
    }

    // Firebase Storage에 사진 업로드
    private fun uploadImageToFirebaseStorage() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
//                Log.d("Register", "이미지 업로드 성공: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
//                    Log.d("Register", "파일 위치: $it")
                }
            }.addOnFailureListener {
//                    Log.d("Register", "이미지 업로드 실패: $it")
            }
    }
}