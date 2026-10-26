package com.movix.app.repository

import com.movix.app.model.Movimiento
import com.movix.app.model.Producto
import com.movix.app.model.Usuario.Rol
import com.movix.app.model.Movimiento.TipoMovimiento
import com.movix.app.model.Usuario

object DatosDePrueba {
    val usuarios = listOf(
        Usuario(
            id = "U01",
            nombre = "Angela Anhuaman (Admin)",
            correo = "admin@kaiju.com",
            contrasenia = "admin123",
            rol = Rol.ADMINISTRADOR
        ),
        Usuario(
            id = "U02",
            nombre = "Charlie Gonzalez (Ventas)",
            correo = "ventas@kaiju.com",
            contrasenia = "ventas123",
            rol = Rol.VENDEDOR
        ),
        Usuario(
            id = "U03",
            nombre = "Marta Torres (Bodega)",
            correo = "bodega@kaiju.com",
            contrasenia = "bodega123",
            rol = Rol.ENCARGADO_INVENTARIO
        )
    )

    val productosIniciales = mutableListOf(
        Producto(
            codigo = "KJ-POL-01",
            nombre = "Polerón Canguro S-XXL",
            descripcion = "Polerón diseño Frieren serigrafiado",
            categoria = "Anime",
            tipo = "Polerón Canguro",
            precio = 24990,
            stockActual = 15,
            stockMinimo = 5,
            detalle = "Tallas: S a XXL • Algodón 100%"
        ),
        Producto(
            codigo = "KJ-POL-02",
            nombre = "Polerón con Cierre Goku",
            descripcion = "Polerón con cierre Dragon Ball Z",
            categoria = "Anime",
            tipo = "Polerón con Cierre",
            precio = 26990,
            stockActual = 3,
            stockMinimo = 5,
            detalle = "Tallas: M, L, XL"
        ),
        Producto(
            codigo = "KJ-POL-03",
            nombre = "Polerón Polo Uzumaki",
            descripcion = "Polerón estilo polo Naruto Shippuden",
            categoria = "Anime",
            tipo = "Polerón Polo",
            precio = 22990,
            stockActual = 5,
            stockMinimo = 5,
            detalle = "Color Negro • Talla L"
        ),
        Producto(
            codigo = "KJ-PLE-01",
            nombre = "Polera Manga Corta Slam Dunk",
            descripcion = "Polera Akira Sendoh serigrafía premium",
            categoria = "Deportes Anime",
            tipo = "Polera Manga Corta",
            precio = 13990,
            stockActual = 28,
            stockMinimo = 10,
            detalle = "Tallas: S, M, L, XL"
        ),
        Producto(
            codigo = "KJ-PLE-02",
            nombre = "Polera Manga Larga Spy x Family",
            descripcion = "Polera Anya Forger manga larga",
            categoria = "Anime",
            tipo = "Polera Manga Larga",
            precio = 15990,
            stockActual = 2,
            stockMinimo = 5,
            detalle = "Color Blanco • Talla M"
        ),
        Producto(
            codigo = "KJ-PLE-03",
            nombre = "Polera Sin Mangas Boji",
            descripcion = "Polera corte muscular Osama Ranking",
            categoria = "Cultura Pop",
            tipo = "Polera Sin Mangas",
            precio = 11990,
            stockActual = 22,
            stockMinimo = 8,
            detalle = "Formato Deportivo • Talla L"
        ),
        Producto(
            codigo = "KJ-BAG-01",
            nombre = "Totebag One Piece Luffy",
            descripcion = "Bolsa de lienzo Luffy Gear 5",
            categoria = "Accesorios",
            tipo = "Totebag",
            precio = 8990,
            stockActual = 45,
            stockMinimo = 10,
            detalle = "Lienzo de algodón 38x42 cm"
        ),
        Producto(
            codigo = "KJ-BAG-02",
            nombre = "Totebag Chainsaw Man",
            descripcion = "Totebag Pochita tela reforzada",
            categoria = "Accesorios",
            tipo = "Totebag",
            precio = 8990,
            stockActual = 1,
            stockMinimo = 5,
            detalle = "Color Negro con estampado Naranjo"
        ),
        Producto(
            codigo = "KJ-PLE-04",
            nombre = "Polera Manga Corta Kaiju",
            descripcion = "Polera vintage Godzilla logo Kaiju",
            categoria = "Exclusivos",
            tipo = "Polera Manga Corta",
            precio = 16990,
            stockActual = 18,
            stockMinimo = 5,
            detalle = "Formato Oversize • Talla XL"
        ),
        Producto(
            codigo = "KJ-POL-04",
            nombre = "Polerón Canguro Básico",
            descripcion = "Polerón neutro listo para estampado",
            categoria = "Insumos",
            tipo = "Polerón Canguro",
            precio = 19990,
            stockActual = 8,
            stockMinimo = 10,
            detalle = "Algodón Perchado • Color Gris"
        )
    )


    val movimientosIniciales = mutableListOf(
        Movimiento(
            id = "M001",
            codigoProducto = "KJ-POL-01",
            tipo = TipoMovimiento.ENTRADA,
            cantidad = 20,
            motivo = "Recepción de lote inicial bodega",
            fecha = "07/10/2026 09:30",
            usuario = "María López (Bodega)"
        ),
        Movimiento(
            id = "M002",
            codigoProducto = "KJ-POL-01",
            tipo = TipoMovimiento.SALIDA_VENTA,
            cantidad = 5,
            motivo = "Venta boleta #4512",
            fecha = "07/10/2026 11:15",
            usuario = "Carlos Gómez (Ventas)"
        ),
        Movimiento(
            id = "M003",
            codigoProducto = "KJ-POL-02",
            tipo = TipoMovimiento.AJUSTE,
            cantidad = 3,
            motivo = "Ajuste por prendas defectuosas en exhibición",
            fecha = "07/10/2026 14:00",
            usuario = "Ana Pérez (Admin)"
        )
    )
}