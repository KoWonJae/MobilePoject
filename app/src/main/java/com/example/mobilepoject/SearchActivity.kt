package com.example.mobilepoject


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilepoject.databinding.ActivityMainBinding
import com.example.mobilepoject.databinding.ActivitySearchBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: MyProfileAdapter
    lateinit var rdb: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "다른 사람 검색"
        init()

    }

    private fun init(){
        layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rdb = FirebaseDatabase.getInstance().getReference("Profile/people")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<Profile>()
            .setQuery(query, Profile::class.java)
            .build()
        adapter = MyProfileAdapter(option)
        adapter.itemClickListener = object :MyProfileAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, positon: Int) {
                binding.apply{
                    Toast.makeText(this@SearchActivity, adapter.getItem(positon).name, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SearchActivity, RecycleProfileActivity::class.java)
                    val newProfile = adapter.getItem(positon)
                    intent.putExtra("my_data", adapter.getItem(positon).name);
                    intent.putExtra("grade", adapter.getItem(positon).email);
                    intent.putExtra("phoneNumber", adapter.getItem(positon).phoneNumber);
                    intent.putExtra("record", adapter.getItem(positon).selfinfo);
                    intent.putExtra("tag", adapter.getItem(positon).tag);
                    startActivity(intent)
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

            imageView.setOnClickListener{
                if(adapter!=null)
                    adapter.stopListening()
                val query1 = rdb.orderByChild("name").equalTo(editText.text.toString())
//                val query = rdb.orderByChild("name").startAt(editText.text.toString())
//                val query = rdb.orderByChild("name").
                val option1 = FirebaseRecyclerOptions.Builder<Profile>()
                    .setQuery(query1, Profile::class.java)
                    .build()
                adapter = MyProfileAdapter(option1)
                recyclerView.adapter = adapter
                adapter.itemClickListener = object :MyProfileAdapter.OnItemClickListener{
                    override fun OnItemClick(view: View, positon: Int) {
                        binding.apply{
                            Toast.makeText(this@SearchActivity, adapter.getItem(positon).name, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SearchActivity, RecycleProfileActivity::class.java)
                            val newProfile = adapter.getItem(positon)
//                            intent.putExtra("my_datas", adapter.getItem(positon))
                            intent.putExtra("my_data", adapter.getItem(positon).name);
                            intent.putExtra("grade", adapter.getItem(positon).email);
                            intent.putExtra("phoneNumber", adapter.getItem(positon).phoneNumber);
                            intent.putExtra("record", adapter.getItem(positon).selfinfo);
                            intent.putExtra("tag", adapter.getItem(positon).tag);



                            startActivity(intent)
                        }
                    }
                }
                adapter.startListening()
            }

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
    override fun onStart(){
        super.onStart()
        adapter.startListening()
    }

    override fun onStop(){
        super.onStop()
        adapter.stopListening()

    }
}