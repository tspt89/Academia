package com.tspt.academia.Administradores.usuarios

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R
import com.tspt.academia.databinding.FragmentNuevoUsuarioBinding
import java.util.*

class NuevoUsuarioFragment : Fragment(R.layout.fragment_nuevo_usuario), AdapterView.OnItemSelectedListener{

    private lateinit var binding : FragmentNuevoUsuarioBinding

    private var rol = -1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNuevoUsuarioBinding.bind(view)

        requireActivity().title = "Nuevo Usuario"

        setup()
    }

    private fun setup(){
        val db = Firebase.database.reference
        binding.spinner.onItemSelectedListener = this

        binding.inscripcionAlumnoBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                binding.inscripcionAlumnoBtn.text = "" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year

            }, year, month, day)

            dpd.show()
        }

        binding.button.setOnClickListener {
            val f2 = FirebaseOptions.Builder().setDatabaseUrl("https://webacademia-67494-default-rtdb.firebaseio.com")
                .setApiKey("AIzaSyCls_dMxIaG6wlyOcTywgbmKx50mgTQYdE")
                .setApplicationId("1:843865031037:web:4f5880861d3930f1778c14")
                .build()

            when(rol){
                1 -> { //Administrador
                    val nombre = binding.nombreAdminET.text.toString().trim()
                    val apellidos = binding.apellidoAdminET.text.toString().trim()
                    val telefono = binding.telefonoAdminET.text.toString().trim()
                    val correo = binding.correoAdminET.text.toString().trim()
                    val passwd = binding.passwdAdminET.text.toString().trim()
                    val confPasswd = binding.confPasswdAdminET.text.toString().trim()
                    if(validateInputAdministrador(nombre, apellidos, telefono, correo, passwd, confPasswd)){
                        Toast.makeText(requireContext(),"Registrando usuario tipo Administrador", Toast.LENGTH_LONG).show()

                        try{
                            val app = FirebaseApp.initializeApp(requireContext(),f2,"App2")
                            val auth = FirebaseAuth.getInstance(app)

                            auth.createUserWithEmailAndPassword(correo,passwd).addOnCompleteListener {
                                if(it.isSuccessful){
                                    val uidUser = it.result!!.user!!.uid
                                    println("$nombre $apellidos $telefono $correo $passwd $confPasswd ")
                                    //Registro en la base de datos
                                    val user = db.child("users").child("$uidUser")

                                    user.child("nombre").setValue(nombre)
                                    user.child("apellidos").setValue(apellidos)
                                    user.child("email").setValue(correo)
                                    user.child("role").setValue(1.toLong())
                                    user.child("telefono").setValue(telefono)
                                    user.push()

                                    val admin = db.child("Roles")

                                    admin.child("Administrador").child("$uidUser").setValue(nombre)
                                    admin.push()

                                    Toast.makeText(requireContext(), "Usuario Administrador registrado existosamente!", Toast.LENGTH_LONG).show()
                                    auth.app.delete()
                                    val action = NuevoUsuarioFragmentDirections.actionNuevoUsuarioFragmentToUsuariosAdminFragment()
                                    findNavController().navigate(action)

                                } else {
                                    Toast.makeText(requireContext(), "Hubo un error al momento de registrarse... (${it.exception!!.message})",Toast.LENGTH_LONG).show()
                                    it.exception!!.printStackTrace()
                                }
                            }
                        }catch (e: Exception){
                            Toast.makeText(requireContext(), "Hubo un error al momento de registrarse... ${e.message}", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                }

                2 -> { //Instructor
                    val nombre = binding.nombreInstET.text.toString().trim()
                    val apellidos = binding.apellidoInstET.text.toString().trim()
                    val telefono = binding.telefonoInstET.text.toString().trim()
                    val correo = binding.correoInstET.text.toString().trim()
                    val fechaNacimiento = binding.fechaNacInstET.text.toString().trim()
                    val passwd = binding.passwdInstET.text.toString().trim()
                    val confPasswd = binding.confPasswdInstET.text.toString().trim()

                    if(validateInputInstructor(nombre,apellidos,telefono,correo,fechaNacimiento,passwd,confPasswd)){

                        Toast.makeText(requireContext(),"Registrando usuario tipo Instructor", Toast.LENGTH_LONG).show()

                        try{
                            val app = FirebaseApp.initializeApp(requireContext(),f2,"App2")
                            val auth = FirebaseAuth.getInstance(app)

                            auth.createUserWithEmailAndPassword(correo,passwd).addOnCompleteListener {
                                if(it.isSuccessful){
                                    val uidUser = it.result!!.user!!.uid
                                    println("$nombre $apellidos $telefono $correo $fechaNacimiento $passwd $confPasswd ")
                                    //Registro en la base de datos
                                    val user = db.child("users").child("$uidUser")

                                    user.child("nombre").setValue(nombre)
                                    user.child("apellidos").setValue(apellidos)
                                    user.child("email").setValue(correo)
                                    user.child("role").setValue(2.toLong())
                                    user.child("fecha_nacimiento").setValue(fechaNacimiento)
                                    user.child("telefono").setValue(telefono)
                                    user.push()

                                    val inst = db.child("Roles")

                                    inst.child("Instructor").child("$uidUser").setValue(nombre)
                                    inst.push()

                                    Toast.makeText(requireContext(), "Usuario Instructor registrado existosamente!", Toast.LENGTH_LONG).show()
                                    auth.app.delete()
                                    val action = NuevoUsuarioFragmentDirections.actionNuevoUsuarioFragmentToUsuariosAdminFragment()
                                    findNavController().navigate(action)

                                } else {
                                    Toast.makeText(requireContext(), "Hubo un error al momento de registrarse... (${it.exception!!.message})",Toast.LENGTH_LONG).show()
                                    it.exception!!.printStackTrace()
                                }
                            }
                        }catch (e: Exception){
                            Toast.makeText(requireContext(), "Hubo un error al momento de registrarse... ${e.message}", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                }

                3 -> { //Responsable
                    val nombre = binding.nombreRespET.text.toString().trim()
                    val apellidos = binding.apellidoRespET.text.toString().trim()
                    val telefono = binding.telefonoRespET.text.toString().trim()
                    val correo = binding.correoRespET.text.toString().trim()
                    val passwd = binding.passwdRespET.text.toString().trim()
                    val confPasswd = binding.confPasswdRespET.text.toString().trim()

                    if(validateInputResponsable(nombre,apellidos,telefono,correo,passwd,confPasswd)){

                        Toast.makeText(requireContext(),"Registrando usuario tipo Responsable", Toast.LENGTH_LONG).show()

                        try{
                            val app = FirebaseApp.initializeApp(requireContext(),f2,"App2")
                            val auth = FirebaseAuth.getInstance(app)

                            auth.createUserWithEmailAndPassword(correo,passwd).addOnCompleteListener {
                                if(it.isSuccessful){
                                    val uidUser = it.result!!.user!!.uid
                                    println("$nombre $apellidos $telefono $correo$passwd $confPasswd ")
                                    //Registro en la base de datos
                                    val user = db.child("users").child("$uidUser")

                                    user.child("nombre").setValue(nombre)
                                    user.child("apellidos").setValue(apellidos)
                                    user.child("email").setValue(correo)
                                    user.child("role").setValue(3.toLong())
                                    user.child("telefono").setValue(telefono)
                                    user.push()

                                    val inst = db.child("Roles")

                                    inst.child("Responsable").child("$uidUser").setValue(nombre)
                                    inst.push()

                                    Toast.makeText(requireContext(), "Usuario Instructor registrado existosamente!", Toast.LENGTH_LONG).show()
                                    auth.app.delete()
                                    val action = NuevoUsuarioFragmentDirections.actionNuevoUsuarioFragmentToUsuariosAdminFragment()
                                    findNavController().navigate(action)

                                } else {
                                    Toast.makeText(requireContext(), "Hubo un error al momento de registrarse... (${it.exception!!.message})",Toast.LENGTH_LONG).show()
                                    it.exception!!.printStackTrace()
                                }
                            }
                        }catch (e: Exception){
                            Toast.makeText(requireContext(), "Hubo un error al momento de registrarse... ${e.message}", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                }

                4 -> {
                    val nombre = binding.nombreTutorET.text.toString().trim()
                    val apellidos = binding.apellidoTutorET.text.toString().trim()
                    val telefono = binding.telefonoTutorET.text.toString().trim()
                    val correo = binding.correoTutorET.text.toString().trim()
                    val passwd = binding.passwdTutorET.text.toString().trim()
                    val confPasswd = binding.confPasswdTutorET.text.toString().trim()

                    if(validateInputTutor(nombre,apellidos,telefono,correo,passwd,confPasswd)){

                        Toast.makeText(requireContext(),"Registrando usuario tipo Tutor", Toast.LENGTH_LONG).show()

                        try{
                            val app = FirebaseApp.initializeApp(requireContext(),f2,"App2")
                            val auth = FirebaseAuth.getInstance(app)

                            auth.createUserWithEmailAndPassword(correo,passwd).addOnCompleteListener {
                                if(it.isSuccessful){
                                    val uidUser = it.result!!.user!!.uid
                                    println("$nombre $apellidos $telefono $correo$passwd $confPasswd ")
                                    //Registro en la base de datos
                                    val user = db.child("users").child("$uidUser")

                                    user.child("nombre").setValue(nombre)
                                    user.child("apellidos").setValue(apellidos)
                                    user.child("email").setValue(correo)
                                    user.child("role").setValue(4.toLong())
                                    user.child("telefono").setValue(telefono)
                                    user.push()

                                    val inst = db.child("Roles")

                                    inst.child("Tutor").child("$uidUser").setValue(nombre)
                                    inst.push()

                                    Toast.makeText(requireContext(), "Usuario Tutor registrado existosamente!", Toast.LENGTH_LONG).show()
                                    auth.app.delete()
                                    val action = NuevoUsuarioFragmentDirections.actionNuevoUsuarioFragmentToUsuariosAdminFragment()
                                    findNavController().navigate(action)

                                } else {
                                    Toast.makeText(requireContext(), "Hubo un error al momento de registrarse... (${it.exception!!.message})",Toast.LENGTH_LONG).show()
                                    it.exception!!.printStackTrace()
                                }
                            }
                        }catch (e: Exception){
                            Toast.makeText(requireContext(), "Hubo un error al momento de registrarse... ${e.message}", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                }

                5 -> { //Alumno
                    val nombre = binding.nombreAlumnoET.text.toString().trim()
                    val apellidos = binding.apellidoAlumnoET.text.toString().trim()
                    val telefono = binding.telefonoAlumnoET.text.toString().trim()
                    val correo = binding.correoAlumnoET.text.toString().trim()
                    val passwd = binding.passwdAlumnoET.text.toString().trim()
                    val confPasswd = binding.confPasswdAlumnoET.text.toString().trim()
                    val fechaInscripcion = binding.inscripcionAlumnoBtn.text.toString().trim()

                    if(validateInputAlumno(nombre,apellidos,telefono,correo,fechaInscripcion,passwd,confPasswd)){

                        Toast.makeText(requireContext(),"Registrando usuario tipo Alumno", Toast.LENGTH_LONG).show()

                        try{
                            val app = FirebaseApp.initializeApp(requireContext(),f2,"App2")
                            val auth = FirebaseAuth.getInstance(app)

                            auth.createUserWithEmailAndPassword(correo,passwd).addOnCompleteListener {
                                if(it.isSuccessful){
                                    val uidUser = it.result!!.user!!.uid

                                    //Registro en la base de datos
                                    val user = db.child("users").child(uidUser)

                                    user.child("nombre").setValue(nombre)
                                    user.child("apellidos").setValue(apellidos)
                                    user.child("email").setValue(correo)
                                    user.child("role").setValue(2.toLong())
                                    user.child("fecha_incripcion").setValue(fechaInscripcion)
                                    user.child("telefono").setValue(telefono)
                                    user.push()

                                    val inst = db.child("Roles")

                                    inst.child("Alumno").child(uidUser).setValue(nombre)
                                    inst.push()

                                    Toast.makeText(requireContext(), "Usuario Alumno registrado existosamente!", Toast.LENGTH_LONG).show()
                                    auth.app.delete()
                                    val action = NuevoUsuarioFragmentDirections.actionNuevoUsuarioFragmentToUsuariosAdminFragment()
                                    findNavController().navigate(action)

                                } else {
                                    Toast.makeText(requireContext(), "Hubo un error al momento de registrarse... (${it.exception!!.message})",Toast.LENGTH_LONG).show()
                                    it.exception!!.printStackTrace()
                                }
                            }
                        }catch (e: Exception){
                            Toast.makeText(requireContext(), "Hubo un error al momento de registrarse... ${e.message}", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun validateInputAdministrador(nombre: String, apellidos: String, telefono: String, correo:String, passwd: String, confPasswd:String ): Boolean {

        if(nombre.isNullOrEmpty()){
            binding.nombreAdminET.error = "Favor de ingresar un nombre"
            binding.nombreAdminET.requestFocus()
            return false
        }
        else if(apellidos.isNullOrEmpty()){
            binding.apellidoAdminET.error = "Favor de ingresar un apellido"
            binding.apellidoAdminET.requestFocus()
            return false
        }
        else if(telefono.isNullOrEmpty() || telefono.length != 10){
            binding.telefonoAdminET.error = "Favor de ingresar un telefono"
            binding.telefonoAdminET.requestFocus()
            return false
        }
        else if(correo.isNullOrEmpty()){
            binding.correoAdminET.error = "Favor de ingresar un correo"
            binding.correoAdminET.requestFocus()
            return false
        }

        else if(passwd.isNullOrEmpty() || passwd.length < 6){
            binding.passwdAdminET.error = "Favor de ingresar una contraseña"
            binding.passwdAdminET.requestFocus()
            return false
        }

        else if(passwd != confPasswd){
            binding.confPasswdAdminET.error = "Favor de verificar una contraseña"
            binding.confPasswdAdminET.requestFocus()
            return false
        }

        return true
    }

    private fun validateInputInstructor(nombre:String, apellidos: String, telefono: String, correo: String, fechaNac: String, passwd: String, confPasswd: String): Boolean {
        if(nombre.isNullOrEmpty()){
            binding.nombreInstET.error = "Favor de ingresar un nombre"
            binding.nombreInstET.requestFocus()
            return false
        }
        else if(apellidos.isNullOrEmpty()){
            binding.apellidoInstET.error = "Favor de ingresar un apellido"
            binding.apellidoInstET.requestFocus()
            return false
        }
        else if(telefono.isNullOrEmpty() || telefono.length != 10){
            binding.telefonoInstET.error = "Favor de ingresar un telefono"
            binding.telefonoInstET.requestFocus()
            return false
        }
        else if(correo.isNullOrEmpty()){
            binding.correoInstET.error = "Favor de ingresar un correo"
            binding.correoInstET.requestFocus()
            return false
        }

        else if(fechaNac.isNullOrEmpty() || fechaNac.split("/").size != 3){
            binding.fechaNacInstET.error = "Por favor ingrese una fecha de nacimiento valido"
            binding.fechaNacInstET.requestFocus()
        }

        else if(passwd.isNullOrEmpty() || passwd.length < 6){
            binding.passwdInstET.error = "Favor de ingresar una contraseña"
            binding.passwdInstET.requestFocus()
            return false
        }

        else if(passwd != confPasswd){
            binding.confPasswdInstET.error = "Favor de verificar una contraseña"
            binding.confPasswdInstET.requestFocus()
            return false
        }
        return true
    }

    private fun validateInputResponsable(nombre:String, apellidos: String, telefono: String, correo: String, passwd: String, confPasswd: String): Boolean {
        if(nombre.isNullOrEmpty()){
            binding.nombreRespET.error = "Favor de ingresar un nombre"
            binding.nombreRespET.requestFocus()
            return false
        }
        else if(apellidos.isNullOrEmpty()){
            binding.apellidoRespET.error = "Favor de ingresar un apellido"
            binding.apellidoRespET.requestFocus()
            return false
        }
        else if(telefono.isNullOrEmpty() || telefono.length != 10){
            binding.telefonoRespET.error = "Favor de ingresar un telefono"
            binding.telefonoRespET.requestFocus()
            return false
        }
        else if(correo.isNullOrEmpty()){
            binding.correoRespET.error = "Favor de ingresar un correo"
            binding.correoRespET.requestFocus()
            return false
        }
        else if(passwd.isNullOrEmpty() || passwd.length < 6){
            binding.passwdRespET.error = "Favor de ingresar una contraseña"
            binding.passwdRespET.requestFocus()
            return false
        }

        else if(passwd != confPasswd){
            binding.confPasswdRespET.error = "Favor de verificar una contraseña"
            binding.confPasswdRespET.requestFocus()
            return false
        }
        return true
    }

    private fun validateInputTutor(nombre:String, apellidos: String, telefono: String, correo: String, passwd: String, confPasswd: String): Boolean {
        if(nombre.isNullOrEmpty()){
            binding.nombreTutorET.error = "Favor de ingresar un nombre"
            binding.nombreTutorET.requestFocus()
            return false
        }
        else if(apellidos.isNullOrEmpty()){
            binding.apellidoTutorET.error = "Favor de ingresar un apellido"
            binding.apellidoTutorET.requestFocus()
            return false
        }
        else if(telefono.isNullOrEmpty() || telefono.length != 10){
            binding.telefonoTutorET.error = "Favor de ingresar un telefono"
            binding.telefonoTutorET.requestFocus()
            return false
        }
        else if(correo.isNullOrEmpty()){
            binding.correoTutorET.error = "Favor de ingresar un correo"
            binding.correoTutorET.requestFocus()
            return false
        }
        else if(passwd.isNullOrEmpty() || passwd.length < 6){
            binding.passwdTutorET.error = "Favor de ingresar una contraseña"
            binding.passwdTutorET.requestFocus()
            return false
        }

        else if(passwd != confPasswd){
            binding.confPasswdTutorET.error = "Favor de verificar una contraseña"
            binding.confPasswdTutorET.requestFocus()
            return false
        }
        return true
    }

    private fun validateInputAlumno(nombre:String, apellidos: String, telefono: String, correo: String, fechaInsc: String, passwd: String, confPasswd: String): Boolean {
        if(nombre.isNullOrEmpty()){
            binding.nombreAlumnoET.error = "Favor de ingresar un nombre"
            binding.nombreAlumnoET.requestFocus()
            return false
        }
        else if(apellidos.isNullOrEmpty()){
            binding.apellidoAlumnoET.error = "Favor de ingresar un apellido"
            binding.apellidoAlumnoET.requestFocus()
            return false
        }
        else if(telefono.isNullOrEmpty() || telefono.length != 10){
            binding.telefonoAlumnoET.error = "Favor de ingresar un telefono"
            binding.telefonoAlumnoET.requestFocus()
            return false
        }
        else if(correo.isNullOrEmpty()){
            binding.correoAlumnoET.error = "Favor de ingresar un correo"
            binding.correoAlumnoET.requestFocus()
            return false
        }

        else if(fechaInsc.isNullOrEmpty() || fechaInsc.split("/").size != 3){
            binding.inscripcionAlumnoBtn.error = "Por favor ingrese una fecha de nacimiento valido"
            binding.inscripcionAlumnoBtn.requestFocus()
        }

        else if(passwd.isNullOrEmpty() || passwd.length < 6){
            binding.passwdAlumnoET.error = "Favor de ingresar una contraseña"
            binding.passwdAlumnoET.requestFocus()
            return false
        }

        else if(passwd != confPasswd){
            binding.confPasswdAlumnoET.error = "Favor de verificar una contraseña"
            binding.confPasswdAlumnoET.requestFocus()
            return false
        }
        return true
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when(parent?.selectedItem) {
            "Administrador" -> {
                rol = 1
                binding.administradorLayout.visibility = View.VISIBLE
                binding.instructorLayout.visibility = View.GONE
                binding.responsableLayout.visibility = View.GONE
                binding.tutorLayout.visibility = View.GONE
                binding.alumnoLayout.visibility = View.GONE
            }

            "Instructor" -> {
                rol = 2
                binding.administradorLayout.visibility = View.GONE
                binding.instructorLayout.visibility = View.VISIBLE
                binding.responsableLayout.visibility = View.GONE
                binding.tutorLayout.visibility = View.GONE
                binding.alumnoLayout.visibility = View.GONE
            }

            "Responsable" -> {
                rol = 3
                binding.administradorLayout.visibility = View.GONE
                binding.instructorLayout.visibility = View.GONE
                binding.responsableLayout.visibility = View.VISIBLE
                binding.tutorLayout.visibility = View.GONE
                binding.alumnoLayout.visibility = View.GONE
            }

            "Tutor" -> {
                rol = 4
                binding.administradorLayout.visibility = View.GONE
                binding.instructorLayout.visibility = View.GONE
                binding.responsableLayout.visibility = View.GONE
                binding.tutorLayout.visibility = View.VISIBLE
                binding.alumnoLayout.visibility = View.GONE
            }

            "Alumno" -> {
                rol = 5
                binding.administradorLayout.visibility = View.GONE
                binding.instructorLayout.visibility = View.GONE
                binding.responsableLayout.visibility = View.GONE
                binding.tutorLayout.visibility = View.GONE
                binding.alumnoLayout.visibility = View.VISIBLE
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}