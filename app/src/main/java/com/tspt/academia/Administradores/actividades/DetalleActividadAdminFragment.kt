package com.tspt.academia.Administradores.actividades

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentDetalleActividadAdminBinding
import com.tspt.academia.models.Actividad
import java.util.*

class DetalleActividadAdminFragment : Fragment(R.layout.fragment_detalle_actividad_admin) {

    private lateinit var binding : FragmentDetalleActividadAdminBinding
    private val args: DetalleActividadAdminFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = args.actividad

        binding = FragmentDetalleActividadAdminBinding.bind(view)

        setupUI(activity)

        binding.guardarBtn.setOnClickListener {
            saveActividad(activity)
        }
    }

    private fun setupUI(actividad : Actividad){
        println("Actividad: $actividad")
        binding.nombreActividadTV.setText(actividad.nombre)
        binding.idActividadTV.text = "ID: ${actividad.id}"
        binding.fechaActividadTV.text = actividad.fecha
        binding.horaInicioActividadTV.text = actividad.horaInicial
        binding.horaFinActividadTV.text = actividad.horaFinal

        binding.fechaActividadTV.setOnClickListener {
            val fecha = actividad.fecha!!.split("/")
            val d = fecha[0].toInt()
            val m = fecha[1].toInt()
            val y = fecha[2].toInt()


            val datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                actividad.fecha = "$dayOfMonth/${(month+1)}/$year"
                binding.fechaActividadTV.text = actividad.fecha
            },y,m,d)

            datePicker.show()
        }

        binding.horaInicioActividadTV.setOnClickListener {
            val hora = actividad.horaInicial!!.split(":")
            val h = hora[0].toInt()
            val m = hora[1].toInt()

            val tpd = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                actividad.horaInicial = "$hourOfDay:$minute"
                binding.horaInicioActividadTV.text = actividad.horaInicial
            },h, m, true)

            tpd.show()
        }

        binding.horaFinActividadTV.setOnClickListener {
            val hora = actividad.horaInicial!!.split(":")
            val h = hora[0].toInt()
            val m = hora[1].toInt()

            val tpd = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                actividad.horaFinal = "$hourOfDay:$minute"
                binding.horaFinActividadTV.text = actividad.horaFinal
            },h, m, true)

            tpd.show()
        }



    }

    private fun saveActividad(actividad: Actividad){


        /*var db = Firebase.database.reference
        val actividad = db.child("Actividades").child("$id")
        actividad.child("nombre").setValue(n)
        actividad.child("fecha").setValue(fecha)
        actividad.child("hora_inicial").setValue(horaInicial)
        actividad.child("hora_final").setValue(horaFinal)*/
    }
}