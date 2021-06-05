package com.tspt.academia.Administradores.usuarios

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        requireActivity().title = "Usuarios"

        binding = FragmentUsuariosAdminBinding.bind(view)

        getUsers()

        binding.nuevoUsuarioBtn.setOnClickListener {
            val action = UsuariosAdminFragmentDirections.actionUsuariosAdminFragmentToNuevoUsuarioFragment()
            findNavController().navigate(action)
        }
    }

    private fun getUsers(){
        val db = Firebase.database.reference.child("users")

        binding.usuariosRV.adapter = UsuariosAdapter(requireContext(), users)
        binding.usuariosRV.layoutManager = LinearLayoutManager(requireContext())

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                users.clear()

                snapshot.children.forEach {
                    val rol = it.child("role").value?.toString()?.toInt()
                    println("${it.key} - $rol")
                    if(rol != null){
                        when (rol){
                            1 -> {  // Administrador
                                val n = it.child("nombre").value?.toString()
                                val a = it.child("apellidos").value?.toString()
                                val e = it.child("email").value?.toString()
                                val t = it.child("telefono").value?.toString()
                                println("User RV: $n $a $e $t")
                                users.add(User(it.key.toString()).setAdministrator(n, a, t, e))
                            }

                            2 -> {  // Instructor
                                val n = it.child("nombre").value?.toString()
                                val a = it.child("apellidos").value?.toString()
                                val e = it.child("email").value?.toString()
                                val t = it.child("telefono").value?.toString()
                                val f = it.child("fecha_nacimiento").value?.toString()
                                println("User RV: $n $a $e $t")
                                users.add(User(it.key.toString()).setInstructor(n,a,t,e,f))
                            }

                            3 -> {  // Responsable
                                val n = it.child("nombre").value?.toString()
                                val a = it.child("apellidos").value?.toString()
                                val e = it.child("email").value?.toString()
                                val t = it.child("telefono").value?.toString()
                                println("User RV: $n $a $e $t")
                                users.add(User(it.key.toString()).setResponsable(n,a,t,e))
                            }

                            4 -> {  // Tutor
                                val n = it.child("nombre").value?.toString()
                                val a = it.child("apellidos").value?.toString()
                                val e = it.child("email").value?.toString()
                                val t = it.child("telefono").value?.toString()
                                println("User RV: $n $a $e $t")
                                users.add(User(it.key.toString()).setTutor(n,a,t,e))
                            }

                            5 -> {  // Alumno
                                val n = it.child("nombre").value?.toString()
                                val a = it.child("apellidos").value?.toString()
                                val e = it.child("email").value?.toString()
                                val t = it.child("telefono").value?.toString()
                                val f = it.child("fecha_inscripcion").value?.toString()
                                println("User RV: $n $a $e $t")
                                users.add(User(it.key.toString()).setAlumno(n,a,t,e,f))
                            }
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