package com.example.mobilepoject.messenger

import android.widget.ImageView
import android.widget.TextView
import com.example.mobilepoject.R
import com.example.mobilepoject.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class UserItem(val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.username_textview).text = user.username

        // 프로필 이미지 없으면 빈칸, 있으면 보여주기
        if(user.profileImageUrl != ""){
            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.findViewById<ImageView>(R.id.userimage_imageview))
        }
        else{
            Picasso.get().load(R.drawable.round_white_edittext).into(viewHolder.itemView.findViewById<ImageView>(R.id.userimage_imageview))
        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_user_row
    }
}
