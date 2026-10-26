package com.movix.app.repository

import com.movix.app.database.MovimientoDao
import com.movix.app.model.MovimientoEntity
import com.movix.app.database.ProductoDao
import com.movix.app.model.Movimiento
import com.movix.app.model.Producto
import com.movix.app.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class InventarioRepository(
    private val productoDao: ProductoDao,
    private val movimientoDao: MovimientoDao
) {

    val productos: Flow<List<Producto>> = productoDao.obtenerTodos()

    val movimientos: Flow<List<Movimiento>> = movimientoDao.obtenerMovimientos().map { listaEntity ->
        listaEntity.map { entity ->
            Movimiento(
                id = entity.id,
                codigoProducto = entity.codigoProducto,
                tipo = Movimiento.TipoMovimiento.valueOf(entity.tipo),
                cantidad = entity.cantidad,
                motivo = entity.motivo,
                fecha = entity.fecha,
                usuario = entity.usuario
            )
        }
    }

    suspend fun registrarMovimiento(movimiento: Movimiento) {
        withContext(Dispatchers.IO) {
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

    fun obtenerUsuarios(): List<Usuario> {
        return DatosDePrueba.usuarios
    }

    suspend fun obtenerProductoPorCodigo(codigo: String): Producto? {
        return productoDao.obtenerPorCodigo(codigo)
    }

    suspend fun agregarProducto(nuevoProducto: Producto) {
        withContext(Dispatchers.IO) {
            productoDao.insertar(nuevoProducto)
        }
    }

    suspend fun actualizarProducto(producto: Producto) {
        withContext(Dispatchers.IO) {
            productoDao.actualizar(producto)
        }
    }

    suspend fun eliminarProducto(producto: Producto) {
        withContext(Dispatchers.IO) {
            productoDao.eliminar(producto)
        }
    }
}