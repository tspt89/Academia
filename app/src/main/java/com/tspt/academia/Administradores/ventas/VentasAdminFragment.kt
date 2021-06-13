package com.tspt.academia.Administradores.ventas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentVentasAdminBinding

class VentasAdminFragment : Fragment(R.layout.fragment_ventas_admin) {

    private lateinit var binding: FragmentVentasAdminBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVentasAdminBinding.bind(view)

        requireActivity().title = "Ventas"

        binding.nuevaVentaBtn.setOnClickListener {
            val action = VentasAdminFragmentDirections.actionVentasAdminFragmentToNuevaVentaFragment()
            findNavController().navigate(action)
        }
    }
}