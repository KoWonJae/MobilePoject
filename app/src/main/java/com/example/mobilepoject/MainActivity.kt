package com.example.mobilepoject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilepoject.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val tabTitleArr = arrayListOf<String>("프로필", "검색", "채팅") // 탭바 타이틀
    lateinit var adapter : MyFragmentStateAdapter
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser == null) {
            // 계정이 로그인 되어있지 않으면 LoginActivity로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        adapter = MyFragmentStateAdapter(this)
        binding.viewpager2.adapter = adapter

        TabLayoutMediator(binding.tablayout, binding.viewpager2) {
                tab, position ->
            tab.text = tabTitleArr[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_menu) {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}