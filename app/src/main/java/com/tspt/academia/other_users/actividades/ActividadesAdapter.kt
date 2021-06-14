package com.tspt.academia.other_users.actividades

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.tspt.academia.R
import com.tspt.academia.models.Actividad

class ActividadesAdapter(val context: Context, val navController: NavController) :
    RecyclerView.Adapter<ActividadesAdapter.ViewHolder>() {

    var actividades = emptyList<Actividad>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.nombreActividadTV)
        val fecha: TextView = view.findViewById(R.id.descripcionActividadTV)
        val detalle: ImageView = view.findViewById(R.id.infoActividadIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_cardview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val n = actividades[position].nombre
        val f =
            "${actividades[position].nombre} ${actividades[position].horaFinal} - ${actividades[position].horaFinal}"

        holder.nombre.text = n
        holder.fecha.text = f

        holder.detalle.setOnClickListener {

            val action = ActividadesFragmentDirections.actionActividadesFragmentToDetalleActividadFragment(actividades[position])
            navController.navigate(action)
        }


    }

    override fun getItemCount(): Int = actividades.size

    fun setElements(actividades: ArrayList<Actividad>) {
        this.actividades = actividades
        notifyDataSetChanged()
    }

}

