package com.example.demogetapk.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demogetapk.fecth.UserModel
import com.example.demogetapk.databinding.ItemUserRowLayoutBinding

class UserAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    private var userModelList = emptyList<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserRowLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClicked(userModel: UserModel)
    }

    inner class MyViewHolder(private val binding: ItemUserRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val fileNameTextView = binding.textFirstName
        val fileLastTextView = binding.textLastName
        val imgOptionBtn = binding.imgOptionBtn

        init {
            imgOptionBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = userModelList[position]
                    listener.onItemClicked(item)

                }
            }
        }

    }


    override fun getItemCount(): Int = userModelList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userModel = userModelList[position]
        holder.fileNameTextView.text = userModel.firstName
        holder.fileLastTextView.text = userModel.lastName



    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(userModel: List<UserModel>) {
        userModelList = userModel
        notifyDataSetChanged()
    }
}
