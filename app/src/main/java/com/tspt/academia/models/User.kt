package com.tspt.academia.models

data class User(val id: String) {


    var date: String? = ""
    var email: String? = ""
    var nombre: String? = ""
    var apellido: String? = ""
    var role: Int? = -1
    var telephone: String? = ""

    fun setAdministrator(n: String?, a: String?, t: String?, e : String?): User{
        this.nombre = n
        this.apellido = a
        this.telephone = t
        this.email = e
        this.role = 1
        return this
    }

    fun setInstructor(n: String?, a: String?, t: String?, e : String?, fechaNac: String?): User{
        this.nombre = n
        this.apellido = a
        this.telephone = t
        this.email = e
        this.date = fechaNac
        this.role = 2
        return this
    }

    fun setResponsable(n: String?, a: String?, t: String?, e : String?): User{
        this.nombre = n
        this.apellido = a
        this.telephone = t
        this.email = e
        this.role = 3
        return this
    }

    fun setTutor(n: String?, a: String?, t: String?, e : String?): User{
        this.nombre = n
        this.apellido = a
        this.telephone = t
        this.email = e
        this.role = 4
        return this
    }

    fun setAlumno(n: String?, a: String?, t: String?, e : String?, fechaInsc: String?): User{
        this.nombre = n
        this.apellido = a
        this.telephone = t
        this.email = e
        this.date = fechaInsc
        this.role = 5
        return this
    }

}