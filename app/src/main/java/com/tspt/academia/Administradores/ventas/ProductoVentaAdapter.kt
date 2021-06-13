package com.tspt.academia.Administradores.ventas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tspt.academia.R
import com.tspt.academia.models.Product

class ProductoVentaAdapter(val context: Context) : RecyclerView.Adapter<ProductoVentaAdapter.ViewHolder>() {

    var products = emptyList<Product>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title : TextView = view.findViewById(R.id.nombreProductoTV)
        val desc : TextView = view.findViewById(R.id.descripcionProductoTV)
        val precio : TextView = view.findViewById(R.id.precioProductoTV)
        val add : ImageView = view.findViewById(R.id.addProductIV)
        val remove : ImageView = view.findViewById(R.id.remProductIV)
        val amount : TextView = view.findViewById(R.id.amaountProductTV)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.producto_venta_admin_cardview, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = products[position].nombre
        val d = products[position].descripcion
        val p = products[position].precio
        val a = products[position].amount


        holder.title.text = t
        holder.desc.text = d
        holder.precio.text = p
        holder.amount.text = a.toString()

        holder.add.setOnClickListener {
            var a = products[position].amount
            if(a < 10){
               holder.amount.text = products[position].increaseAmount().toString()
            } else {
                Toast.makeText(context, "Limite del producto excedido.", Toast.LENGTH_SHORT).show()
            }
        }

        holder.remove.setOnClickListener {
            var a = products[position].amount
            if(a > 0){
                holder.amount.text = products[position].decreaseAmount().toString()
            } else {
                Toast.makeText(context, "Limite del producto excedido.", Toast.LENGTH_SHORT).show()
            }
        }



    }

    override fun getItemCount(): Int = products.size

    fun setElements(products: ArrayList<Product>){
        this.products = products
        notifyDataSetChanged()
    }
}