package com.tspt.academia.Administradores.usuarios

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentUsuariosAdminBinding
import com.tspt.academia.models.User

class UsuariosAdminFragment : Fragment(R.layout.fragment_usuarios_admin) {

    private lateinit var binding : FragmentUsuariosAdminBinding

    private var users: ArrayList<User> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUsuariosAdminBinding.bind(view)

        getUsers()


    }

    private fun getUsers(){
        val db = Firebase.database.reference.child("users")

        binding.usuariosRV.adapter = UsuariosAdapter(requireContext(), users)
        binding.usuariosRV.layoutManager = LinearLayoutManager(requireContext())

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach {
                    val rol = it.child("role").value.toString().toInt()
                    println("${it.key} - $rol")

                    when (rol){
                        1 -> {  // Administrador
                            val n = it.child("nombre").value as String
                            val a = it.child("apellidos").value as String
                            val e = it.child("email").value as String
                            val t = it.child("telefono").value as String

                            users.add(User(it.key.toString()).setAdministrator(n, a, t, e))
                        }
                    }
                }

                binding.usuariosRV.adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Hubo un error en la conexión con la base de datos. ${error.message}", Toast.LENGTH_LONG).show()

            }
        }

        db.addValueEventListener(listener)
    }
}