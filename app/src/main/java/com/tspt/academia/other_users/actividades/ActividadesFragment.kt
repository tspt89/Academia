package com.tspt.academia.other_users.actividades

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentActividadesBinding
import com.tspt.academia.models.Actividad

class ActividadesFragment : Fragment(R.layout.fragment_actividades) {

    private lateinit var binding : FragmentActividadesBinding

    private lateinit var actividadesAdapter: ActividadesAdapter
    private val actividades = ArrayList<Actividad>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentActividadesBinding.bind(view)

        requireActivity().title = "Mis actividades"

        actividadesAdapter = ActividadesAdapter(requireContext(),findNavController())
        getActividades()
        actividadesAdapter.setElements(actividades)
        binding.actividadesRV.adapter = actividadesAdapter
        binding.actividadesRV.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getActividades(){
        //Obtener todas las actividades de este usuario
        val act = ArrayList<String>()
        val ac = Firebase.database.reference.child("users").child(Firebase.auth.currentUser!!.uid).child("actividades")

        ac.get().addOnCompleteListener {
            if(it.isSuccessful){
                it.result!!.children.forEach { a ->
                    println("Id de la actividad: ${a.key}")
                    act.add(a.key.toString())
                }
            }
        }


        val db = Firebase.database.reference.child("Actividades")
        val listener = object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                snapshot.children.forEach {
                    val id = it.key
                    if(act.contains(id)){
                        println("Si contiene la actividad")

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
                        actividadesAdapter.notifyDataSetChanged()
                    }

                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        db.addValueEventListener(listener)
    }
}