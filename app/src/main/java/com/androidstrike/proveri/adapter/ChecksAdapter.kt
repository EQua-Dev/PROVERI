package com.androidstrike.proveri.adapter

import android.R.id.message
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.proveri.History
import com.androidstrike.proveri.MainActivity
import com.androidstrike.proveri.R
import com.androidstrike.proveri.database.DBModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custom_history.view.*


class ChecksAdapter(val context: Context): RecyclerView.Adapter<ChecksAdapter.MyViewHolder>() {

    private var checksList = emptyList<DBModel>()
    private lateinit var view: ViewGroup
    private var activity = MainActivity()
    private var history = History()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.txt_report.setOnClickListener {
                val prodBatchNumber = itemView.txt_batch_number.text.toString().trim()
                val feds = listOf<String>("nafdac@nafdac.gov.ng", "info@ndlea.gov.ng")
                val subject = "Product Report"
                val reportMessage = "Please investigate this product with batch number: \n$prodBatchNumber"

                val email = Intent(Intent.ACTION_SEND)
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("nafdac@nafdac.gov.ng"))
                email.putExtra(Intent.EXTRA_SUBJECT, subject)
                email.putExtra(Intent.EXTRA_TEXT, reportMessage)

                //need this to prompts email client only

                //need this to prompts email client only
                email.type = "message/rfc822"

                context.startActivity(Intent.createChooser(email, "Choose an Email client :"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        view = parent
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_history,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = checksList[position]
        holder.itemView.txt_batch_number.text = currentItem.batch_no.toString()
        holder.itemView.txt_prod_name.text = currentItem.prod_name
        holder.itemView.txt_prod_review.text = currentItem.prod_review

        if (!currentItem.is_valid)
        holder.itemView.img_verified.setImageResource(R.drawable.ic_baseline_cancel_24)
        else{
            holder.itemView.img_verified.setImageResource(R.drawable.ic_baseline_verified_24)
        }

    }

    override fun getItemCount(): Int {
        return checksList.size
    }

    fun setData(checks: List<DBModel>){
        this.checksList = checks
        notifyDataSetChanged()
    }
}