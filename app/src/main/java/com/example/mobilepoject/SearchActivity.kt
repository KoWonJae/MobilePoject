package com.example.mobilepoject


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilepoject.databinding.ActivityMainBinding
import com.example.mobilepoject.databinding.ActivitySearchBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: MyProfileAdapter
    lateinit var rdb: DatabaseReference
    var flag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "다른 사람 검색"
        initSpinner()
        init()

    }

    private fun init(){
        layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        rdb = FirebaseDatabase.getInstance().getReference("Profiles/people")
        rdb = FirebaseDatabase.getInstance().getReference("users/people")
        val query = rdb.limitToLast(50)
//        val option = FirebaseRecyclerOptions.Builder<Profile>()
//            .setQuery(query, Profile::class.java)
//            .build()
        val option = FirebaseRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()
        adapter = MyProfileAdapter(option)
        adapter.itemClickListener = object :MyProfileAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, positon: Int) {
                binding.apply{
                    Toast.makeText(this@SearchActivity, adapter.getItem(positon).username, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SearchActivity, RecycleProfileActivity::class.java)
                    val searchProfile = adapter.getItem(positon)
                    intent.putExtra("uid", searchProfile.uid)
                    intent.putExtra("username", searchProfile.username)
                    intent.putExtra("email", searchProfile.email)
                    intent.putExtra("phoneNumber", searchProfile.phoneNumber)
                    intent.putExtra("selfinfo", searchProfile.selfinfo)
                    intent.putExtra("tag", searchProfile.tag)
                    intent.putExtra("profileImg", searchProfile.profileImageUrl)
                    intent.putExtra("site", searchProfile.site)
                    intent.putExtra("career", searchProfile.career)
                    startActivity(intent)
//                    Toast.makeText(this@SearchActivity, adapter.getItem(positon).name, Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this@SearchActivity, RecycleProfileActivity::class.java)
//                    val newProfile = adapter.getItem(positon)
//                    intent.putExtra("my_data", adapter.getItem(positon).name);
//                    intent.putExtra("grade", adapter.getItem(positon).email);
//                    intent.putExtra("phoneNumber", adapter.getItem(positon).phoneNumber);
//                    intent.putExtra("record", adapter.getItem(positon).selfinfo);
//                    intent.putExtra("tag", adapter.getItem(positon).tag);
//                    startActivity(intent)
                }
            }
        }


        binding.apply {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter

//            button2.setOnClickListener {
//                val intent = Intent(this@SearchActivity, ProfileActivity::class.java)
//                startActivity(intent)
//            }

            // 검색 버튼 누르지 않아도 editText에 입력된 데이터를 포함한 사용자 검색
            editText.addTextChangedListener {
                if(adapter!=null)
                    adapter.stopListening()

                var query1 = rdb.orderByChild("username").startAt(editText.text.toString()).endAt(editText.text.toString()+"\uFFFF")
                if(flag == 0) {
                    query1 = rdb.orderByChild("username").startAt(editText.text.toString()).endAt(editText.text.toString()+"\uFFFF")
                } else if(flag == 1) {
                    query1 = rdb.orderByChild("tag").startAt(editText.text.toString()).endAt(editText.text.toString()+"\uFFFF")
                }

                val option1 = FirebaseRecyclerOptions.Builder<User>()
                    .setQuery(query1, User::class.java)
                    .build()
                adapter = MyProfileAdapter(option1)
                recyclerView.adapter = adapter
                adapter.itemClickListener = object :MyProfileAdapter.OnItemClickListener{
                    override fun OnItemClick(view: View, positon: Int) {
                        binding.apply{
                            val intent = Intent(this@SearchActivity, RecycleProfileActivity::class.java)
                            val searchProfile = adapter.getItem(positon)
                            intent.putExtra("uid", searchProfile.uid)
                            intent.putExtra("my_data", searchProfile.username)
                            intent.putExtra("grade", searchProfile.email)
                            intent.putExtra("phoneNumber", searchProfile.phoneNumber)
                            intent.putExtra("record", searchProfile.selfinfo)
                            intent.putExtra("tag", searchProfile.tag)
                            intent.putExtra("profileImg", searchProfile.profileImageUrl)
                            intent.putExtra("site", searchProfile.site)
                            intent.putExtra("career", searchProfile.career)
                            startActivity(intent)
                        }
                    }
                }
                adapter.startListening()
            }




//            imageView.setOnClickListener{
//                if(adapter!=null)
//                    adapter.stopListening()
////                val query1 = rdb.orderByChild("name").equalTo(editText.text.toString())
////                val query = rdb.orderByChild("name").startAt(editText.text.toString())
////                val query = rdb.orderByChild("name").
////                val option1 = FirebaseRecyclerOptions.Builder<Profile>()
////                    .setQuery(query1, Profile::class.java)
////                    .build()
//
//                var query1 = rdb.orderByChild("username").equalTo(editText.text.toString())
//                if(flag == 0) {
//                    query1 = rdb.orderByChild("username").equalTo(editText.text.toString())
//                } else if(flag == 1) {
//                    query1 = rdb.orderByChild("tag").equalTo(editText.text.toString())
//                }
//
//                //val query1 = rdb.orderByChild("username").equalTo(editText.text.toString())
//                val option1 = FirebaseRecyclerOptions.Builder<User>()
//                    .setQuery(query1, User::class.java)
//                    .build()
//                adapter = MyProfileAdapter(option1)
//                recyclerView.adapter = adapter
//                adapter.itemClickListener = object :MyProfileAdapter.OnItemClickListener{
//                    override fun OnItemClick(view: View, positon: Int) {
//                        binding.apply{
////                            Toast.makeText(this@SearchActivity, adapter.getItem(positon).name, Toast.LENGTH_SHORT).show()
//                            Toast.makeText(this@SearchActivity, adapter.getItem(positon).username, Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this@SearchActivity, RecycleProfileActivity::class.java)
//                            val searchProfile = adapter.getItem(positon)
//                            intent.putExtra("uid", searchProfile.uid)
//                            intent.putExtra("my_data", searchProfile.username)
//                            intent.putExtra("grade", searchProfile.email)
//                            intent.putExtra("phoneNumber", searchProfile.phoneNumber)
//                            intent.putExtra("record", searchProfile.selfinfo)
//                            intent.putExtra("tag", searchProfile.tag)
//                            intent.putExtra("profileImg", searchProfile.profileImageUrl)
//                            intent.putExtra("site", searchProfile.site)
//                            intent.putExtra("career", searchProfile.career)
////                            intent.putExtra("my_datas", adapter.getItem(positon))
////                            intent.putExtra("my_data", adapter.getItem(positon).name);
////                            intent.putExtra("grade", adapter.getItem(positon).email);
////                            intent.putExtra("phoneNumber", adapter.getItem(positon).phoneNumber);
////                            intent.putExtra("record", adapter.getItem(positon).selfinfo);
////                            intent.putExtra("tag", adapter.getItem(positon).tag);
//
//                            startActivity(intent)
//                        }
//                    }
//                }
//                adapter.startListening()
//            }

        }
//        adapter.itemClickListener = object :MyProfileAdapter.OnItemClickListener{
//            override fun OnItemClick(view: View, positon: Int) {
//                binding.apply{
//                    profileName.setText(adapter.getItem(positon).pId.toString())
//                    pNameEdit.setText(adapter.getItem(positon).pName.toString())
//                    pQuantityEdit.setText(adapter.getItem(positon).pQuantity.toString())
//                }
//            }
//        }
        //data search_word from edittext and query for searching by name of profile
        val search_word =findViewById<EditText>(R.id.editText).text.toString()
        //검색문

    }

    private fun initSpinner() {
        // 검색을 이름으로 할건지 태그로 할건지 고르는 spinner 초기화하는 함수
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ArrayList<String>())
        adapter2.add("이름")
        adapter2.add("태그")
        binding.apply {
            spinner.adapter = adapter2
            spinner.setSelection(0)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(position) {
                        0 -> flag = 0 // 이름
                        1 -> flag = 1 // 태그
                    }
                }
            }
        }
    }

    override fun onStart(){
        super.onStart()
        adapter.startListening()
    }

    override fun onStop(){
        super.onStop()
        adapter.stopListening()

    }
}