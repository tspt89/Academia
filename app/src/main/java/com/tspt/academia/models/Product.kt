package com.tspt.academia.models

data class Product(val id: String, val nombre: String, val descripcion: String, val fechaFinal: String, val precio: String ) {

    var amount = 0

    fun increaseAmount() =
        ++this.amount

    fun decreaseAmount() =
        --this.amount
}