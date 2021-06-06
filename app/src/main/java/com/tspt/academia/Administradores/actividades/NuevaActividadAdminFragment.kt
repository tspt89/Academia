package com.tspt.academia.Administradores.actividades

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.NuevaActividadAdminBinding
import com.tspt.academia.models.User
import java.util.*
import kotlin.collections.ArrayList

class NuevaActividadAdminFragment : Fragment(R.layout.nueva_actividad_admin) {

    private lateinit var binding: NuevaActividadAdminBinding

    private val alumnos: ArrayList<User> = ArrayList()
    private val instructores: ArrayList<User> = ArrayList()

    private lateinit var alumnosAdapter: AlumnosActividadAdapter
    private lateinit var instructoresAdapter: AlumnosActividadAdapter

    private var initHour = 0
    private var initMinute = 0
    private var endHour = 0
    private var endMinute = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = "Nueva Actividad"

        binding = NuevaActividadAdminBinding.bind(view)

        binding.fechaBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val d = calendar.get(Calendar.DAY_OF_MONTH)
            val m = calendar.get(Calendar.MONTH)
            val y = calendar.get(Calendar.YEAR)

            val datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                binding.fechaBtn.text = "$dayOfMonth/$month/$year"
            }, y, m, d)
            datePicker.datePicker.minDate = calendar.timeInMillis
            datePicker.show()
        }

        binding.horaInicialBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val h = c.get(Calendar.HOUR_OF_DAY)
            val m = c.get(Calendar.MINUTE)

            val dpd = TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                initHour = hourOfDay
                initMinute = minute
                binding.horaInicialBtn.text = "$hourOfDay:$minute"

            }, h,m, true)

            dpd.show()
        }

        binding.horaFinalBtn.setOnClickListener {
            val dpd = TimePickerDialog(requireActivity(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                endHour = hourOfDay
                endMinute = minute
                binding.horaFinalBtn.text = "$hourOfDay:$minute"

            }, initHour, initMinute, true)

            dpd.show()
        }

        alumnosAdapter = AlumnosActividadAdapter(requireContext())
        binding.alumnosRV.adapter = alumnosAdapter
        binding.alumnosRV.layoutManager = LinearLayoutManager(requireContext())

        instructoresAdapter = AlumnosActividadAdapter(requireContext())
        binding.instructoresRV.adapter = instructoresAdapter
        binding.instructoresRV.layoutManager = LinearLayoutManager(requireContext())

        getAlumnos()
        getInstructores()

        binding.guardarActividadBtn.setOnClickListener {
            registerActivity()
        }
    }

    private fun getAlumnos(){
        val db = Firebase.database.reference.child("Roles/Alumno")

        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                alumnos.clear()

                println("HIJOS DE ALUMNOS")
                snapshot.children.forEach { alumno ->
                    val alumno = Firebase.database.reference.child("users/${alumno.key}")

                    alumno.get().addOnCompleteListener {
                        if(it.isSuccessful){
                            val res = it.result!!
                            println("${alumno.key} -> ${it.result}")
                            val n = res.child("nombre").value?.toString()
                            val a = res.child("apellido").value?.toString()
                            val c = res.child("email").value?.toString()
                            val f = res.child("fecha_inscripcion").value?.toString()
                            val t = res.child("telefono").value?.toString()

                            alumnos.add(User("${alumno.key}").setAlumno(n,a,t,c,f))
                            alumnosAdapter.setElements(alumnos)
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Hubo un error al momento de obtener los datos... (${error.message})",Toast.LENGTH_LONG).show()
                error.toException().printStackTrace()
            }

        }

        db.addValueEventListener(listener)
    }

    private fun getInstructores(){
        val db = Firebase.database.reference.child("Roles/Instructor")

        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                instructores.clear()

                println("HIJOS DE Instructores")
                snapshot.children.forEach { inst ->
                    val instructor = Firebase.database.reference.child("users/${inst.key}")

                    instructor.get().addOnCompleteListener {
                        if(it.isSuccessful){
                            val res = it.result!!
                            println("${instructor.key} -> ${it.result}")
                            val n = res.child("nombre").value?.toString()
                            val a = res.child("apellido").value?.toString()
                            val c = res.child("email").value?.toString()
                            val f = res.child("fecha_nacimiento").value?.toString()
                            val t = res.child("telefono").value?.toString()

                            instructores.add(User("${instructor.key}").setInstructor(n,a,t,c,f))
                            instructoresAdapter.setElements(instructores)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Hubo un error al momento de obtener los datos... (${error.message})",Toast.LENGTH_LONG).show()
                error.toException().printStackTrace()
            }

        }

        db.addValueEventListener(listener)
    }

    private fun registerActivity(){
        val inst = instructoresAdapter.getSelection()
        val alm = alumnosAdapter.getSelection()
        val n = binding.nombreActividadTV.text.toString().trim()
        val fecha = binding.fechaBtn.text.toString().trim()
        val horaInicial = binding.horaInicialBtn.text.toString()
        val horaFinal = binding.horaFinalBtn.text.toString()

        println("Register activity: ${inst.size} ${alm.size}")

        if(validateInput(inst, alm, n, fecha, horaInicial, horaFinal)){
            Toast.makeText(requireContext(), "Registrando actividad...", Toast.LENGTH_LONG).show()
            val id = UUID.randomUUID().toString()

            var db = Firebase.database.reference
            val actividad = db.child("Actividades").child("$id")
            actividad.child("nombre").setValue(n)
            actividad.child("fecha").setValue(fecha)
            actividad.child("hora_inicial").setValue(horaInicial)
            actividad.child("hora_final").setValue(horaFinal)

            //Agregando instructores
            val i = actividad.child("instructores")
            inst.forEach {
                i.child(it.id).setValue(it.nombre)
            }

            //Agregando alumnos
            val a = actividad.child("alumnos")
            alm.forEach {
                a.child(it.id).setValue(it.nombre)
            }

            actividad.push()

            //Agregar a los usuarios la actividad correspondiente
            val usuarios : ArrayList<User> = ArrayList()
            usuarios.addAll(inst)
            usuarios.addAll(alm)

            usuarios.forEach {
                db = Firebase.database.reference
                db.child("users/${it.id}/actividades").child(id).setValue(n)
                db.push()
            }

            val action = NuevaActividadAdminFragmentDirections.actionNuevaActividadAdminFragmentToActividadesAdminFragment()
            findNavController().navigate(action)

        }


    }

    private fun validateInput(inst: ArrayList<User>, alm: ArrayList<User>, n: String, fecha: String, horaInicial: String, horaFinal: String): Boolean {
        if(fecha.contains("Fecha", true)){
            Toast.makeText(requireContext(), "Tiene que seleccionar una fecha para esta nueva actividad...", Toast.LENGTH_LONG).show()
            return false
        }

        if(n.isEmpty()){
            Toast.makeText(requireContext(), "Tiene que ingresar un nombre para esta nueva actividad...", Toast.LENGTH_LONG).show()
            return false
        }

        if(!horaInicial.contains(":")){
            Toast.makeText(requireContext(), "Tiene que ingresar una hora inicial para esta nueva actividad...", Toast.LENGTH_LONG).show()
            return false
        }

        if(!horaFinal.contains(":")){
            Toast.makeText(requireContext(), "Tiene que ingresar una hora final para esta nueva actividad...", Toast.LENGTH_LONG).show()
            return false
        }

        if(inst.isEmpty()){
            Toast.makeText(requireContext(), "Tiene que seleccionar al menos un instructor para esta actividad...", Toast.LENGTH_LONG).show()
            return false
        } else if(alm.isEmpty()){
            Toast.makeText(requireContext(), "Tiene que seleccionar al menos un alumno para esta actividad...", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

}