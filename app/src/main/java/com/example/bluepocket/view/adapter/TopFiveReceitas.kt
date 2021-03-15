package com.example.bluepocket.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluepocket.R
import com.example.bluepocket.model.Movimentation
import java.util.*

class TopFiveReceitasAdapter(context: Context, val listMovimentation: ArrayList<Movimentation>) :
    RecyclerView.Adapter<TopFiveReceitasViewHolder>(){

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopFiveReceitasViewHolder {
        val view = layoutInflater.inflate(R.layout.item_recycleview,parent,false)

        var holder = TopFiveReceitasViewHolder(view)

        Collections.sort(listMovimentation.map {
            it.date
        })

        return holder
    }

    override fun getItemCount(): Int {
        if (listMovimentation.size < 5){
            return listMovimentation.size
        }else{
            return 5
        }
    }

    override fun onBindViewHolder(holder: TopFiveReceitasViewHolder, position: Int) {
        holder.movimentationName.text = listMovimentation[position].name
        holder.movimentationType.text = listMovimentation[position].type
        holder.movimentationMov.text = "Receitas"
        holder.movimentationDate.text = listMovimentation[position].date
        holder.movimentationValue.text = "R$ "+listMovimentation[position].value.toString()    }
    }





class TopFiveReceitasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val movimentationName: TextView = view.findViewById(R.id.item_recycleview_name)
    val movimentationType: TextView = view.findViewById(R.id.item_recycleview_type)
    val movimentationMov: TextView = view.findViewById(R.id.item_recycleview_movimentacao)
    val movimentationDate: TextView = view.findViewById(R.id.item_recycleview_date)
    val movimentationValue: TextView = view.findViewById(R.id.item_recycleview_value)

}