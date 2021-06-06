package com.tspt.academia.Administradores.actividades

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentActividadesAdminBinding

class ActividadesAdminFragment : Fragment(R.layout.fragment_actividades_admin) {
    private lateinit var binding : FragmentActividadesAdminBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentActividadesAdminBinding.bind(view)

        requireActivity().title = "Actividades"

        binding.nuevaActividadBtn.setOnClickListener {
            val action = ActividadesAdminFragmentDirections.actionActividadesAdminFragmentToNuevaActividadAdminFragment()
            findNavController().navigate(action)
        }



    }
}