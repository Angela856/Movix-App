package com.movix.app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.movix.app.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao

interface ProductoDao {

        @Query("SELECT * FROM productos ORDER BY codigo ASC")
        fun obtenerTodos(): Flow<List<Producto>>

        @Query("SELECT * FROM productos WHERE codigo = :codigo LIMIT 1")
        suspend fun obtenerPorCodigo(codigo: String): Producto?

        // suspend: una operación de base de datos nunca bloquea el hilo principal
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertar(producto: Producto)

        @Update
        suspend fun actualizar(producto: Producto)

        @Delete
        suspend fun eliminar(producto: Producto)
}