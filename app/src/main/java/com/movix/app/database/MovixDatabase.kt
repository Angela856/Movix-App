package com.movix.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.movix.app.model.MovimientoEntity
import com.movix.app.model.Producto
import com.movix.app.repository.DatosDePrueba
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Producto::class, MovimientoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class MovixDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun movimientoDao(): MovimientoDao

    companion object {
        @Volatile
        private var INSTANCE: MovixDatabase? = null

        fun obtenerBaseDatos(context: Context): MovixDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    MovixDatabase::class.java,
                    "movix_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    precargarDatos(database.productoDao(), database.movimientoDao())
                                }
                            }
                        }
                    })
                    .build()

                INSTANCE = instancia
                instancia
            }
        }

        private suspend fun precargarDatos(productoDao: ProductoDao, movimientoDao: MovimientoDao) {
            DatosDePrueba.productosIniciales.forEach { producto ->
                productoDao.insertar(producto)
            }

            DatosDePrueba.movimientosIniciales.forEach { movimiento ->
                val entity = MovimientoEntity(
                    id = movimiento.id,
                    codigoProducto = movimiento.codigoProducto,
                    tipo = movimiento.tipo.name,
                    cantidad = movimiento.cantidad,
                    motivo = movimiento.motivo,
                    fecha = movimiento.fecha,
                    usuario = movimiento.usuario
                )
                movimientoDao.insertarMovimiento(entity)
            }
        }
    }
}