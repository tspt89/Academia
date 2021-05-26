package com.tspt.academia.Administradores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tspt.academia.models.Product
import com.tspt.academia.R

class ProductsAdminAdapter(val context: Context) : RecyclerView.Adapter<ProductsAdminAdapter.ViewHolder>() {

    var products = emptyList<Product>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img : ImageView = view.findViewById(R.id.productIV)
        val title : TextView = view.findViewById(R.id.nombreProductoTV)
        val desc : TextView = view.findViewById(R.id.descripcionProductoTV)
        val precio : TextView = view.findViewById(R.id.precioProductoTV)
        val info : ImageView = view.findViewById(R.id.infoIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.products_cardview_admin, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = products[position].nombre
        val d = products[position].descripcion
        val p = products[position].precio

        holder.title.text = t
        holder.desc.text = d
        holder.precio.text = p
        holder.info.setOnClickListener {
            Toast.makeText(context, "Entra a otro fragment", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int = products.size

    fun setElements(products: ArrayList<Product>){
        this.products = products
        notifyDataSetChanged()
    }
}