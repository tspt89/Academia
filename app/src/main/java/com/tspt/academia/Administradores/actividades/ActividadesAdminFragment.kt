package com.tspt.academia.Administradores.actividades

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentActividadesAdminBinding
import com.tspt.academia.models.Actividad

class ActividadesAdminFragment : Fragment(R.layout.fragment_actividades_admin) {
    private lateinit var binding : FragmentActividadesAdminBinding

    private lateinit var actividadesAdapter: ActividadesAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentActividadesAdminBinding.bind(view)

        requireActivity().title = "Actividades"

        binding.nuevaActividadBtn.setOnClickListener {
            val action = ActividadesAdminFragmentDirections.actionActividadesAdminFragmentToNuevaActividadAdminFragment()
            findNavController().navigate(action)
        }

        actividadesAdapter = ActividadesAdapter(requireContext(),findNavController())
        binding.actividadesRV.adapter = actividadesAdapter
        binding.actividadesRV.layoutManager = LinearLayoutManager(requireContext())
        getActividades()

    }

    private fun getActividades(){
        val db = Firebase.database.reference.child("Actividades")

        val listener = object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val actividades = ArrayList<Actividad>()

                snapshot.children.forEach {
                    val id = it.key
                    val n = it.child("nombre").value?.toString()
                    val f = it.child("fecha").value?.toString()
                    val horaInicial = it.child("hora_inicial").value?.toString()
                    val horaFinal = it.child("hora_final").value?.toString()
                    val a = it.child("alumnos").value as HashMap<*, *>
                    val ak = ArrayList<String>()
                    a.keys.forEach{ ak.add(it.toString())}

                    val i = it.child("instructores").value as HashMap<*, *>
                    val ik = ArrayList<String>()
                    i.keys.forEach{ ik.add(it.toString())}

                    actividades.add(Actividad(id,n,f,horaInicial,horaFinal,ak,ik))
                }

                actividadesAdapter.setElements(actividades)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        db.addValueEventListener(listener)
    }
}