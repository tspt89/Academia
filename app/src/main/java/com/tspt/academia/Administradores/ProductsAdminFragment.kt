package com.tspt.academia.Administradores

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentProductsAdminBinding

class ProductsAdminFragment : Fragment(R.layout.fragment_products_admin) {

    private lateinit var binding : FragmentProductsAdminBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductsAdminBinding.bind(view)

        binding.nuevoProductoBtn.setOnClickListener {
            val action = ProductsAdminFragmentDirections.actionProductsAdminFragmentToNuevoProductoFragment()
            findNavController().navigate(action)
        }
    }
}