package com.example.mobilepoject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilepoject.databinding.SearchProfileRowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso

//class MyProfileAdapter(options: FirebaseRecyclerOptions<Profile>)
//    : FirebaseRecyclerAdapter<Profile, MyProfileAdapter.ViewHolder>(options) {
class MyProfileAdapter(options: FirebaseRecyclerOptions<User>)
    : FirebaseRecyclerAdapter<User, MyProfileAdapter.ViewHolder>(options) {

    interface OnItemClickListener{
        fun OnItemClick(view: View, positon: Int)
    }

    var itemClickListener: OnItemClickListener?=null

    inner class ViewHolder(val binding: SearchProfileRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                itemClickListener!!.OnItemClick(it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchProfileRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Profile) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: User) {
        holder.binding.apply {
//            profileName.text = model.name.toString()
//            profilephonenumber.text = model.phoneNumber.toInt()
//            profilegrade.text = model.grade.toString()
//            profilerecord.text = model.record.toString()
//            profiletag.text = model.tag.toString()
//            productid.text = model.pId.toString()
//            productname.text = model.pName.toString()
//            productquantity.text = model.pQuantity.toString()
            profileName.text = model.username
            profileTag.text = model.tag
            if(model.profileImageUrl != ""){
                Picasso.get().load(model.profileImageUrl).into(pimage)
            }

        }
    }
}
