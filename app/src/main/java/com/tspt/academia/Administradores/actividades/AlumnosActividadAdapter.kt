package com.tspt.academia.Administradores.actividades

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tspt.academia.R
import com.tspt.academia.models.User

class AlumnosActividadAdapter(val context: Context) : RecyclerView.Adapter<AlumnosActividadAdapter.ViewHolder>() {

    var users = emptyList<User>()

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nombre : TextView = view.findViewById(R.id.nombreUserTV)
        val apellido : TextView = view.findViewById(R.id.correoTV)
        val checkBox : ImageView = view.findViewById(R.id.checkboxActividadIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_nueva_actividad_cardview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val n = users[position].nombre
        val a = users[position].apellido
        users[position].isSelected = false

        holder.nombre.text = n
        holder.apellido.text = a

        holder.checkBox.setOnClickListener {
            val isSelected = !users[position].isSelected
            users[position].isSelected = isSelected

            if(isSelected){
                holder.checkBox.setImageDrawable(context.getDrawable(R.drawable.checkbox_selected))
            } else {
                holder.checkBox.setImageDrawable(context.getDrawable(R.drawable.checkbox_empty))
            }
        }
    }

    override fun getItemCount(): Int = users.size

    fun setElements(users: ArrayList<User>){
        this.users = users
        notifyDataSetChanged()
    }

    fun getSelection(): ArrayList<User> {
        val u : ArrayList<User> = ArrayList()

        users.forEach {
            if(it.isSelected){
                u.add(it)
                println("Se agrega uno...")
            }
        }

        return u
    }

}
