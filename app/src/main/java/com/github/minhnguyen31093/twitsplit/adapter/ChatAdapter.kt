package com.github.minhnguyen31093.twitsplit.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.minhnguyen31093.twitsplit.R
import com.github.minhnguyen31093.twitsplit.models.Message
import com.github.minhnguyen31093.twitsplit.utils.TextUtils
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter(private var items: ArrayList<Message>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun add(newItems: ArrayList<Message>) {
        if (newItems.isNotEmpty()) {
            items.addAll(newItems)
            notifyItemRangeInserted(items.size - newItems.size, newItems.size)
        }
    }

    fun getItems(): ArrayList<Message> {
        return items
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var marginTop = 0
        private var marginBottom = 32
        private val marginHorizontal = 32

        fun bind(item: Message, position: Int) {
            marginTop = if (position == 0) {
                32
            } else {
                0
            }

            itemView.tvContent.text = item.content

            if (item.dateTime != -1L) {
                itemView.tvTime.text = TextUtils.getDateTimeFromLong(item.dateTime)
                itemView.tvTime.visibility = View.VISIBLE
                marginBottom = 32
            } else {
                itemView.tvTime.visibility = View.GONE
                marginBottom = 12
            }

            val param = itemView.cvItem.layoutParams as RecyclerView.LayoutParams
            param.setMargins(marginHorizontal, marginTop, marginHorizontal, marginBottom)
            itemView.cvItem.layoutParams = param
        }
    }
}