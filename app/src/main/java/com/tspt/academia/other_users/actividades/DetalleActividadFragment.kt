package com.tspt.academia.other_users.actividades

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentDetalleActividadBinding
import com.tspt.academia.models.User

class DetalleActividadFragment : Fragment(R.layout.fragment_detalle_actividad) {

    private lateinit var binding: FragmentDetalleActividadBinding
    private val args : DetalleActividadFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetalleActividadBinding.bind(view)

        val actividad = args.actividad


        binding.nombreActividadTV.setText(actividad.nombre)
        binding.idActividadTV.text = actividad.id
        binding.fechaActividadTV.text = actividad.fecha
        binding.horaInicioActividadTV.text = actividad.horaInicial
        binding.horaFinActividadTV.text = actividad.horaFinal

        val instructores = ArrayList<User>()
        binding.instructoresActividadTV.adapter = UsuariosAdapter(requireContext(),instructores)
        binding.instructoresActividadTV.layoutManager = LinearLayoutManager(requireContext())
        val db = Firebase.database.reference.child("users")
        actividad.instructores!!.forEach {
            println("Instrucotes $it")

            db.child(it).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val n = it.result!!.child("nombre").value?.toString()
                    val a = it.result!!.child("apellidos").value?.toString()
                    val e = it.result!!.child("email").value?.toString()
                    val t = it.result!!.child("telefono").value?.toString()
                    val f = it.result!!.child("fecha_nacimiento").value?.toString()
                    println("User RV: $n $a $e $t")
                    instructores.add(User(it.result!!.key.toString()).setInstructor(n,a,t,e,f))
                    binding.instructoresActividadTV.adapter!!.notifyDataSetChanged()
                }
            }

        }


        val alumnos = ArrayList<User>()
        binding.alumnosRV.adapter = UsuariosAdapter(requireContext(),alumnos)
        binding.alumnosRV.layoutManager = LinearLayoutManager(requireContext())

        actividad.alumnos!!.forEach {
            println("Alumnos $it")

            db.child(it).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val n = it.result!!.child("nombre").value?.toString()
                    val a = it.result!!.child("apellidos").value?.toString()
                    val e = it.result!!.child("email").value?.toString()
                    val t = it.result!!.child("telefono").value?.toString()
                    val f = it.result!!.child("fecha_inscripcion").value?.toString()
                    println("User RV: $n $a $e $t")
                    alumnos.add(User(it.result!!.key.toString()).setAlumno(n,a,t,e,f))
                    binding.alumnosRV.adapter!!.notifyDataSetChanged()
                }
            }

        }


    }

}