package com.tspt.academia.Administradores

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentHomeAdminBinding

class HomeAdminFragment: Fragment(R.layout.fragment_home_admin) {

    private lateinit var binding : FragmentHomeAdminBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser!!.uid
        var name = ""
        FirebaseDatabase.getInstance().reference.child("users").child(user).child("nombre").get().addOnCompleteListener {
            name = it.result!!.value.toString()
            binding.bienvenido.text = "Bienvenido! $name"
        }

        binding = FragmentHomeAdminBinding.bind(view)

        binding.usuariosBtn.setOnClickListener {
            val action = HomeAdminFragmentDirections.actionHomeAdminFragmentToUsuariosAdminFragment()
            findNavController().navigate(action)
        }

        binding.actividadesBtn.setOnClickListener {
            val action = HomeAdminFragmentDirections.actionHomeAdminFragmentToActividadesAdminFragment()
            findNavController().navigate(action)
        }

        binding.productosBtn.setOnClickListener {
            val action = HomeAdminFragmentDirections.actionHomeAdminFragmentToProductsAdminFragment()
            findNavController().navigate(action)
        }
    }
}