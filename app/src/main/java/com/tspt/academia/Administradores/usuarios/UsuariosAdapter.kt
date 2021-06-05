package com.tspt.academia.Administradores.usuarios

import android.content.Context
import android.media.Image
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tspt.academia.R
import com.tspt.academia.models.User

class UsuariosAdapter(val context: Context, val usuarios: ArrayList<User>) :
    RecyclerView.Adapter<UsuariosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_cardview_admin, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nombre = usuarios[position].nombre
        val correo = usuarios[position].email

        when (usuarios[position].role) {
            1 -> {
                holder.rol.text = "Administrador"
                holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.admin))
            }

            2 -> {
                holder.rol.text = "Instructor"
                holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.instructor))
            }
        }

        holder.nombre.text = nombre
        holder.correo.text = correo

    }

    override fun getItemCount(): Int = usuarios.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.nombreUserTV)
        val correo: TextView = view.findViewById(R.id.correoTV)
        val rol: TextView = view.findViewById(R.id.rol)
        val cardView: CardView = view.findViewById(R.id.userCardView)
        val info: ImageView = view.findViewById(R.id.info_user)
    }
}