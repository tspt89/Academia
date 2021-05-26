package com.tspt.academia.Administradores

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentNuevoProductoBinding
import java.util.*

class NuevoProductoFragment : Fragment(R.layout.fragment_nuevo_producto) {

    private lateinit var binding: FragmentNuevoProductoBinding

    val db = Firebase.database.reference

    private var initDate = ""
    private var finDate = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNuevoProductoBinding.bind(view)

        setupDatePickers()

        binding.guardarBtn.setOnClickListener {
            val nombre = binding.nombreET.text.toString().toLowerCase().trim()
            val desc = binding.descrET.text.toString().trim()
            val precio = binding.precioET.text.toString().trim()

            if(validateInput(nombre,desc,precio,finDate)){
                val productDB = db.child("products").child("${UUID.randomUUID()}")

                productDB.child("nombre").setValue(nombre)
                productDB.child("descripcion").setValue(desc)
                productDB.child("precio").setValue("$$precio")
                productDB.child("fechaFinal").setValue(finDate)
                productDB.push()

                val action = NuevoProductoFragmentDirections.actionNuevoProductoFragmentToProductsAdminFragment()
                findNavController().navigate(action)

            }

        }
    }

    private fun setupDatePickers(){
        val today = Calendar.getInstance()
        binding.limiteDP.minDate = System.currentTimeMillis() + 10000
        binding.limiteDP.init(today[Calendar.YEAR], today[Calendar.MONTH], today[Calendar.DAY_OF_MONTH]) { v, year, month, day ->
            val month = month + 1
            finDate = "$day/$month/$year"
        }
    }

    private fun validateInput(name: String, desc: String, precio: String, limitDate: String): Boolean {
        if(name.isNullOrEmpty()){
            Toast.makeText(requireContext(),"Favor de ingresar el nombre del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        if(desc.isNullOrEmpty()){
            Toast.makeText(requireContext(),"Favor de ingresar la descripcion del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        if(precio.isNullOrEmpty()){
            Toast.makeText(requireContext(),"Favor de ingresar el precio del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        if(limitDate.isNullOrEmpty()){
            Toast.makeText(requireContext(),"Favor de ingresar el la fecha limite del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}