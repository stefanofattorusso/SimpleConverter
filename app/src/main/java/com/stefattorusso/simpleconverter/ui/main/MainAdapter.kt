package com.stefattorusso.simpleconverter.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stefattorusso.simpleconverter.R
import com.stefattorusso.simpleconverter.model.RateModel
import com.stefattorusso.simpleconverter.ui.main.mvvm.MainViewModel
import com.stefattorusso.simpleconverter.utils.loadResource


class MainAdapter(
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<MainAdapter.RateViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, MyDiffUtils())

    override fun getItemCount(): Int = mDiffer.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rate_item_layout, parent, false)
        return RateViewHolder(view)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    fun updateData(newData: List<RateModel>) {
        mDiffer.submitList(newData)
    }

    private fun getItem(position: Int): RateModel = mDiffer.currentList[position]

    inner class RateViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val rateName = view.findViewById<TextView>(R.id.rate_name)
        private val rateCode = view.findViewById<TextView>(R.id.rate_code)
        private val flagImage = view.findViewById<ImageView>(R.id.flag_image)
        private val rateEditText = view.findViewById<EditText>(R.id.rate_edit)

        init {
            view.setOnClickListener(this)
            rateEditText.doOnTextChanged { text, _, _, _ ->
                text.toString().trim().takeIf { it.isNotBlank() && adapterPosition == 0 }
                    ?.let { viewModel.onRateChange(it) }
            }
        }

        override fun onClick(v: View?) {
            viewModel.onItemSelected(adapterPosition)
        }

        fun bindData(data: RateModel) {
            rateCode.text = data.code
            rateName.text = data.name
            flagImage.loadResource(data.flag)
            rateEditText.apply {
                setText(data.value)
                isEnabled = data.base
            }
        }
    }

    inner class MyDiffUtils : DiffUtil.ItemCallback<RateModel>() {

        override fun areItemsTheSame(oldItem: RateModel, newItem: RateModel): Boolean {
            return if (oldItem.base && newItem.base) {
                true
            } else {
                oldItem.code == newItem.code
            }
        }

        override fun areContentsTheSame(oldItem: RateModel, newItem: RateModel): Boolean {
            return if (oldItem.base && newItem.base) {
                oldItem.code == newItem.code
            } else {
                oldItem.value == newItem.value
            }
        }
    }
}

