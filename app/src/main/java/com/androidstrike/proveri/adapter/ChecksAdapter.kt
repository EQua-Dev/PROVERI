package com.androidstrike.proveri.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.proveri.R
import com.androidstrike.proveri.database.DBModel
import kotlinx.android.synthetic.main.custom_history.view.*

class ChecksAdapter: RecyclerView.Adapter<ChecksAdapter.MyViewHolder>() {

    private var checksList = emptyList<DBModel>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_history,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = checksList[position]
        holder.itemView.txt_batch_number.text = currentItem.batch_no.toString()
        holder.itemView.txt_prod_name.text = currentItem.prod_name

        if (!currentItem.is_valid)
        holder.itemView.img_verified.setImageResource(R.drawable.ic_baseline_cancel_24)
        else
            holder.itemView.img_verified.setImageResource(R.drawable.ic_baseline_verified_24)
    }

    override fun getItemCount(): Int {
        return checksList.size
    }

    fun setData(checks: List<DBModel>){
        this.checksList = checks
        notifyDataSetChanged()
    }
}