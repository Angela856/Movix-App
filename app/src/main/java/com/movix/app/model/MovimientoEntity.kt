package com.movix.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimientos")
data class MovimientoEntity(
    @PrimaryKey val id: String,
    val codigoProducto: String,
    val tipo: String,
    val cantidad: Int,
    val motivo: String,
    val fecha: String,
    val usuario: String
)