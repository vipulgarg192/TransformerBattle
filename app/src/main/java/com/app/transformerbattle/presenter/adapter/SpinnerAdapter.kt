package com.app.transformerbattle.presenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.transformerbattle.databinding.SpinnerDropdownBinding
import com.app.transformerbattle.network.model.TransformerDto

class CountryArrayAdapter(context: Context,var transformerList: List<TransformerDto>) : ArrayAdapter<TransformerDto>(context, 0, transformerList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val transformer = getItem(position)

        val binding =  SpinnerDropdownBinding.inflate( LayoutInflater.from(parent.context),
                parent,
                false)
        binding.text.text = transformer?.name.toString()
        return binding.root
    }

}