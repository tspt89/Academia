package com.tspt.academia.Administradores

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
import com.tspt.academia.databinding.FragmentProductsAdminBinding
import com.tspt.academia.models.Product

class ProductsAdminFragment : Fragment(R.layout.fragment_products_admin) {

    private lateinit var binding : FragmentProductsAdminBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductsAdminBinding.bind(view)

        binding.nuevoProductoBtn.setOnClickListener {
            val action = ProductsAdminFragmentDirections.actionProductsAdminFragmentToNuevoProductoFragment()
            findNavController().navigate(action)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        val adapter = ProductsAdminAdapter(requireContext())
        binding.productosRV.adapter = adapter
        binding.productosRV.layoutManager = LinearLayoutManager(requireContext())

        val db = Firebase.database.reference.child("products")
        val products = ArrayList<Product>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                //val post = dataSnapshot.getValue<Post>()
                // ...
                products.clear()

                val count = dataSnapshot.childrenCount
                dataSnapshot.children.forEach {
                    val id = it.key.toString()
                    val n = it.child("nombre").getValue() as String
                    val d = it.child("descripcion").getValue().toString()
                    val f = it.child("fechaFinal").getValue().toString()
                    val p = it.child("precio").getValue().toString()

                    products.add(Product(id,n,d,f,p))

                    adapter.setElements(products)

                }



                println("Hay $count productos")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        db.addValueEventListener(postListener)
    }
}