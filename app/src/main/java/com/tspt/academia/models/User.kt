package com.tspt.academia.models

data class User(val id: String) {

    var uid: String = ""
    var date: String = ""
    var email: String = ""
    var nombre: String = ""
    var apellido: String = ""
    var role: Int = -1
    var telephone: String  = ""

    fun setAdministrator(n: String, a: String, t: String, e : String): User{
        this.nombre = n
        this.apellido = a
        this.telephone = t
        this.email = e
        this.role = 1
        return this
    }

}