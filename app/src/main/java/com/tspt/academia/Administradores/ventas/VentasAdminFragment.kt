package com.tspt.academia.Administradores.ventas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentVentasAdminBinding
import com.tspt.academia.models.Venta

class VentasAdminFragment : Fragment(R.layout.fragment_ventas_admin) {

    private lateinit var binding: FragmentVentasAdminBinding

    private val ventas = ArrayList<Venta>()
    private lateinit var ventasAdapter : VentasAdminAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVentasAdminBinding.bind(view)

        requireActivity().title = "Ventas"

        binding.nuevaVentaBtn.setOnClickListener {
            val action = VentasAdminFragmentDirections.actionVentasAdminFragmentToNuevaVentaFragment()
            findNavController().navigate(action)
        }

        getVentas()
        ventasAdapter = VentasAdminAdapter(requireContext())
        binding.ventasRV.adapter = ventasAdapter
        binding.ventasRV.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun getVentas(){
        val db = Firebase.database.reference.child("ventas")

        db.get().addOnCompleteListener {
            if(it.isSuccessful){
                it.result!!.children.forEach { v ->
                    val id = v.key.toString()
                    val cliente = v.child("cliente").value.toString()
                    val vendedor = v.child("vendedor").value.toString()
                    val fecha = v.child("fecha").value.toString()
                    val total = v.child("total").value.toString().toInt()
                    val prod = v.child("productos").children
                    val a = arrayListOf<HashMap<String,Any>>()


                    prod.forEach {
                        println("$id - ${it}")
                        val hashMap = HashMap<String, Any>()

                        hashMap.put("product", it.key!!)
                        hashMap.put("amount", it.child("amount").value.toString().toInt())
                        hashMap.put("unit_price", it.child("unit_price").value.toString())
                        a.add(hashMap)
                    }

                    ventas.add(Venta(id,cliente,vendedor,total,fecha,a))
                    ventasAdapter.notifyDataSetChanged()
                }
                ventasAdapter.setElements(ventas)
            }
        }


    }

    private fun getVentasByDate(){
        val db = Firebase.database.reference.child("ventas")

        db.get().addOnCompleteListener {
            if(it.isSuccessful){
                it.result!!.children.forEach { v ->
                    val id = v.key.toString()
                    val cliente = v.child("cliente").value.toString()
                    val vendedor = v.child("vendedor").value.toString()
                    val fecha = v.child("fecha").value.toString()
                    val total = v.child("total").value.toString().toInt()
                    val prod = v.child("productos").children
                    val a = arrayListOf<HashMap<String,Any>>()


                    prod.forEach {
                        println("$id - ${it}")
                        val hashMap = HashMap<String, Any>()

                        hashMap.put("product", it.key!!)
                        hashMap.put("amount", it.child("amount").value.toString().toInt())
                        hashMap.put("unit_price", it.child("unit_price").value.toString())
                        a.add(hashMap)
                    }

                    ventas.add(Venta(id,cliente,vendedor,total,fecha,a))
                    ventasAdapter.notifyDataSetChanged()
                }
                ventasAdapter.setElements(ventas)
            }
        }


    }
}