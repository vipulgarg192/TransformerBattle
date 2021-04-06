package com.app.transformerbattle.presenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.transformerbattle.R
import com.app.transformerbattle.databinding.ItemHomeFragmentBinding
import com.app.transformerbattle.network.model.TransformerDto
import com.app.transformerbattle.utils.ItemClickEventHandler
import com.bumptech.glide.Glide

class TransformerListAdapter(
    private val context: Context,
    private val itemClickEventHandler: ItemClickEventHandler,
    private var arrayList: List<TransformerDto>
) : RecyclerView.Adapter<TransformerListAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemHomeFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transformer: TransformerDto) {
            binding.textName.text = transformer.name.trim()
            binding.textTeam.text = transformer.team.trim()
            Glide.with(context).load(transformer.team_icon).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeFragmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
        holder.binding.btnUpdate.setOnClickListener {
            itemClickEventHandler.itemClick(position)
        }
    }

    override fun getItemCount(): Int = arrayList.size


    fun setData(triviaList: List<TransformerDto>) {
        this.arrayList = triviaList
        notifyDataSetChanged()
    }
}