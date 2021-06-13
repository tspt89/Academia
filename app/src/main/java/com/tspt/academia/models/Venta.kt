package com.tspt.academia.models

data class Venta(val id: String, val cliente: String, val vendedor: String, val total: Int, val fecha: String, val productos: ArrayList<HashMap<String, Any>>){

}
