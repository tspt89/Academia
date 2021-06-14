package com.tspt.academia.other_users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentHomeUsersBinding

class HomeUsersFragment : Fragment(R.layout.fragment_home_users) {

    private lateinit var binding: FragmentHomeUsersBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeUsersBinding.bind(view)
        val rol = getRol()

        setupUI(rol)

        binding.actividadesBtn.setOnClickListener {
            val action = HomeUsersFragmentDirections.actionHomeUsersFragmentToActividadesFragment()
            findNavController().navigate(action)
        }
    }

    private fun getRol(): Int {
        val id = Firebase.auth.currentUser!!.uid
        var rol = -1

        Firebase.database.reference.child("users").child(id).child("role").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    println("Rol del usuario: ${it.result!!.value}")
                    setupUI(it.result!!.value.toString().toInt())
                } else {
                    rol = -2
                }
            }



        return rol
    }

    private fun setupUI(rol: Int) {
        println("Rol para setup del UI $rol")
        when (rol) {
            2 -> {
                println("Entra al rol: 2")
                binding.actividadesBtn.visibility = View.VISIBLE
            }

            3 -> {
                println("Entra al rol: 3")
                binding.actividadesBtn.visibility = View.GONE
            }

            4 -> {
                println("Entra al rol: 4")
                binding.actividadesBtn.visibility = View.GONE
            }

            5 -> {
                println("Entra al rol: 5")
                binding.actividadesBtn.visibility = View.VISIBLE
            }
        }
    }
}