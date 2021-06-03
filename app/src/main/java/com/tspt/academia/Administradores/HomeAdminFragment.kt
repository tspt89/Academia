package com.tspt.academia.Administradores

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentHomeAdminBinding

class HomeAdminFragment: Fragment(R.layout.fragment_home_admin) {

    private lateinit var binding : FragmentHomeAdminBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeAdminBinding.bind(view)

        binding.usuariosBtn.setOnClickListener {
            val action = HomeAdminFragmentDirections.actionHomeAdminFragmentToUsuariosAdminFragment()
            findNavController().navigate(action)
        }

        binding.productosBtn.setOnClickListener {
            val action = HomeAdminFragmentDirections.actionHomeAdminFragmentToProductsAdminFragment()
            findNavController().navigate(action)
        }
    }
}