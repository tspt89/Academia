package com.tspt.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Actividad(val id: String?, val nombre: String?, var fecha: String?, var horaInicial: String?, var horaFinal:String?, val alumnos : ArrayList<String>, val instructores : ArrayList<String>?) : Parcelable
