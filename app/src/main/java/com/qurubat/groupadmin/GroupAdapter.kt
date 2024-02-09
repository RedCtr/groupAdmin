package com.qurubat.groupadmin

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qurubat.groupadmin.databinding.CustomGroupTwoBinding
import com.qurubat.groupadmin.databinding.CustomGroupsBinding
import com.qurubat.groupadmin.models.Group

class GroupAdapter(
    private val onButtonClick: ButtonViewClicked
): ListAdapter<Group,GroupAdapter.ViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object :DiffUtil.ItemCallback<Group>() {
            override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem == newItem
            }

        }
    }

    class ViewHolder(
        private val binding: CustomGroupTwoBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group, onButtonClick: ButtonViewClicked) {
            binding.apply {
                groupName.text = group.groupName
                groupDesc.isVisible = !group.groupDescription.isNullOrEmpty()
                groupDesc.text = group.groupDescription
                groupGenre.text = group.genre
                countryName.text = group.countryName


                root.setOnClickListener {
                    // Create an Intent with ACTION_VIEW and the group link URL
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(group.groupLink))
                    // Check if WhatsApp is installed on the device
                    if(intent.resolveActivity(it.context.packageManager) != null) {
                        it.context.startActivity(intent)
                    } else Toast.makeText(it.context,"The WhatsApp Link is invalid",Toast.LENGTH_LONG).show()
                }

                clickGroup.setOnClickListener {
                    // Create an Intent with ACTION_VIEW and the group link URL
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(group.groupLink))
                    // Check if WhatsApp is installed on the device
                    if(intent.resolveActivity(it.context.packageManager) != null) {
                        it.context.startActivity(intent)
                    } else Toast.makeText(it.context,"The WhatsApp Link is invalid",Toast.LENGTH_LONG).show()

                }

                addGroup.setOnClickListener {
                    onButtonClick.saveToFireStore(adapterPosition)
                }

                removeGroup.setOnClickListener {
                    onButtonClick.removeFromFireStore(adapterPosition)
                }


            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).apply {
            if (this != null) holder.bind(this,onButtonClick)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(
           CustomGroupTwoBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
       )
    }
}

interface ButtonViewClicked {
    fun saveToFireStore(position: Int)
    fun removeFromFireStore(position: Int)
}