package com.tspt.academia.Administradores.ventas

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentVentasAdminBinding
import com.tspt.academia.models.Venta
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class VentasAdminFragment : Fragment(R.layout.fragment_ventas_admin) {

    private lateinit var binding: FragmentVentasAdminBinding

    private val ventas = ArrayList<Venta>()
    private lateinit var ventasAdapter : VentasAdminAdapter

    lateinit var fechaIniciar: LocalDate
    lateinit var fechaFin: LocalDate



    @RequiresApi(Build.VERSION_CODES.O)
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

        binding.desdeBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val _d = c.get(Calendar.DAY_OF_MONTH)
            val _m = c.get(Calendar.MONTH)
            val _y = c.get(Calendar.YEAR)

            val dp = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                var convertDate = LocalDate.parse("%02d/%02d/$year".format(dayOfMonth,month), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                println("Fecha de inicio: $convertDate")
                fechaIniciar = convertDate
            },_y,_m,_d)

            dp.show()
        }

        binding.hastaBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val _d = c.get(Calendar.DAY_OF_MONTH)
            val _m = c.get(Calendar.MONTH)
            val _y = c.get(Calendar.YEAR)

            val dp = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                var convertDate = LocalDate.parse("%02d/%02d/$year".format(dayOfMonth,month), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                println("Fecha de inicio: $convertDate")
                fechaFin = convertDate

                if(fechaIniciar != null && fechaFin != null && fechaIniciar < fechaFin){
                    getVentasByDate(fechaIniciar, fechaFin)
                } else {
                    Toast.makeText(requireContext(), "Debe de seleccionar dos fechas", Toast.LENGTH_LONG).show()
                }
            },_y,_m,_d)

            dp.show()


        }

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseToLocalDate(f: String): LocalDate {
        val arrayDate = f.substring(7).split("/")

        return LocalDate.parse("%02d/%02d/${arrayDate[2].toInt()}".format(arrayDate[0].toInt(),arrayDate[1].toInt()), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getVentasByDate(fechaInicio: LocalDate, fechaFin: LocalDate){
        val db = Firebase.database.reference.child("ventas")

        db.get().addOnCompleteListener {
            if(it.isSuccessful){
                ventas.clear()
                it.result!!.children.forEach { v ->
                    val id = v.key.toString()
                    val fecha = parseToLocalDate(v.child("fecha").value.toString())
                    if(fecha in fechaInicio..fechaFin){
                        val cliente = v.child("cliente").value.toString()
                        val vendedor = v.child("vendedor").value.toString()
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

                        ventas.add(Venta(id,cliente,vendedor,total,fecha.toString(),a))
                        ventasAdapter.notifyDataSetChanged()
                    }



                }
                ventasAdapter.setElements(ventas)
            }
        }


    }
}