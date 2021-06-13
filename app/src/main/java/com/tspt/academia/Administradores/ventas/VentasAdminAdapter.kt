package com.tspt.academia.Administradores.ventas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tspt.academia.R
import com.tspt.academia.models.Venta

class VentasAdminAdapter(val context: Context) : RecyclerView.Adapter<VentasAdminAdapter.ViewHolder>() {

    var ventas = emptyList<Venta>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idTV: TextView = view.findViewById(R.id.idVentaTV)
        val nombreClienteTV: TextView = view.findViewById(R.id.nombreClienteTV)
        val totalTV: TextView = view.findViewById(R.id.totalVentaTV)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VentasAdminAdapter.ViewHolder  =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ventas_cardview,parent,false))

    override fun onBindViewHolder(holder: VentasAdminAdapter.ViewHolder, position: Int) {
        val venta = ventas[position]

        val id = venta.id.split("-")[4]
        val f = venta.fecha
        val total = venta.total

        println("Ventas adapter = $id $f $total")
        holder.idTV.text = id
        holder.nombreClienteTV.text = f
        holder.totalTV.text = "$${total}"

    }

    override fun getItemCount(): Int = ventas.size

    fun setElements(v: ArrayList<Venta>) {
        this.ventas = v
        notifyDataSetChanged()
    }

}

