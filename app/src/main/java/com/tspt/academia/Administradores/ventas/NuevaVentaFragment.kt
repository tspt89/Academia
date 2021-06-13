package com.tspt.academia.Administradores.ventas

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.Administradores.actividades.AlumnosActividadAdapter
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentNuevaVentaBinding
import com.tspt.academia.models.Product
import com.tspt.academia.models.User
import java.util.*
import kotlin.collections.ArrayList

class NuevaVentaFragment : Fragment(R.layout.fragment_nueva_venta) {

    private lateinit var binding : FragmentNuevaVentaBinding

    private val products = ArrayList<Product>()
    private val alumnos = ArrayList<User>()
    private lateinit var productAdapter : ProductoVentaAdapter
    private lateinit var alumnoAdapter : AlumnosActividadAdapter

    private var total = 0
    private var fecha = ""
    private val totalProducts = ArrayList<Product>()
    private var clienteArrayList = ArrayList<User>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNuevaVentaBinding.bind(view)

        requireActivity().title = "Nueva Venta"
        productAdapter = ProductoVentaAdapter(requireContext())
        binding.productosVentaRV.adapter = productAdapter
        binding.productosVentaRV.layoutManager = LinearLayoutManager(requireContext())

        alumnoAdapter = AlumnosActividadAdapter(requireContext())
        binding.clientesVentaRV.adapter = alumnoAdapter
        binding.clientesVentaRV.layoutManager = LinearLayoutManager(requireContext())


        //Fecha
        initialUI()

        binding.totalVentaTV.setOnClickListener {
            getVentaInfo()
            binding.totalVentaTV.text = "Total $${total}"
        }

        binding.generarVentaBtn.setOnClickListener {
            getVentaInfo()

            if(validateInput()){
                uploadVenta()
                val action = NuevaVentaFragmentDirections.actionNuevaVentaFragmentToVentasAdminFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun uploadVenta(){
        val db = Firebase.database.reference.child("ventas").child("${UUID.randomUUID()}")
        val cliente = clienteArrayList.get(0)

        db.child("fecha").setValue(fecha)
        db.child("vendedor").setValue(Firebase.auth.currentUser!!.uid.toString())
        db.child("cliente").setValue(cliente.id)
        db.child("total").setValue(total)



        totalProducts.forEach {
            val p = db.child("productos").child(it.id)
            p.child("id").setValue(it.id)
            p.child("amount").setValue(it.amount)
            p.child("unit_price").setValue(it.precio)
        }

    }

    private fun getVentaInfo(){
        //Obtener los productos con una cantidad > 0
        totalProducts.clear()
        total = 0
        products.forEach {
            if(it.amount != 0){
                val precio = it.precio.substring(1).toInt()
                total += precio * it.amount

                totalProducts.add(it)
            }
        }

        clienteArrayList = alumnoAdapter.getSelection()

        println("Total: $total")
    }

    private fun validateInput(): Boolean {

        if(clienteArrayList.size != 1){
            Toast.makeText(requireContext(), "Debe de seleccionar un solo alumno para generar esta venta", Toast.LENGTH_SHORT).show()
            return false
        }

        if(totalProducts.size == 0){
            Toast.makeText(requireContext(), "Debe de seleccionar al menos un producto para generar esta venta", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun initialUI(){
        val db = Firebase.database.reference
        val calendar = Calendar.getInstance()
        val d = calendar.get(Calendar.DAY_OF_MONTH)
        val m = calendar.get(Calendar.MONTH)
        val y = calendar.get(Calendar.YEAR)
        fecha = "Fecha: $d/$m/$y"
        binding.fechaNuevaVenta.text =fecha

        val id = Firebase.auth.currentUser!!.uid
        db.child("users/$id/nombre").get().addOnCompleteListener {
            if(it.isSuccessful){
                val name = it.result!!.value.toString()
                binding.vendedorVentaTV.text = "Vendedor: $name"
            }
        }

        getProducts()
        getAlumnos()
    }

    fun getAlumnos(){
        val db = Firebase.database.reference

        db.child("Roles/Alumno").get().addOnCompleteListener {
            if(it.isSuccessful){
                it.result!!.children.forEach { a ->
                    println("Alumno: ${a.key}")
                    val id = a.key.toString()
                    db.child("users/$id").get().addOnCompleteListener {
                        if(it.isSuccessful){
                            val n = it.result!!.child("nombre").value.toString()
                            val a = it.result!!.child("apellidos").value.toString()
                            val e = it.result!!.child("email").value.toString()
                            val r = it.result!!.child("role").value.toString()
                            val t = it.result!!.child("telefono").value.toString()

                            println("$n $a $e $r $t")
                            alumnos.add(User(id).setAlumno(n,a,t,e,""))
                            alumnoAdapter.notifyDataSetChanged()
                        }
                    }
                }
                alumnoAdapter.setElements(alumnos)
            }

        }
    }

    fun getProducts(){
        val db = Firebase.database.reference

        db.child("products").get().addOnCompleteListener { t ->
            if(t.isSuccessful){
                t.result!!.children.forEach {
                    println("Cada hijo de producto: $it")

                    val id = it.key.toString()
                    val d = it.child("descripcion").value.toString()
                    val p = it.child("precio").value.toString()
                    val n = it.child("nombre").value.toString()

                    products.add(Product(id,n,d,"",p))
                }

                productAdapter.setElements(products)
                productAdapter.notifyDataSetChanged()
            }
        }
    }
}