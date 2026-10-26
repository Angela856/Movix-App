# Kaiju - Sistema de Gestión de Inventario y Control de Stock

| Nombre completo | Correo |
| --- | --- |
| [Angela Anhuaman Solon] | [an.anhuaman@duocuc.cl] |
| [Almendra González Aedo] | [alm.gonzaleza@duocuc.cl ] |

---

## Descripción del Proyecto
**Kaiju** es una aplicación móvil desarrollada para Android utilizando **Jetpack Compose** y **Kotlin**, diseñada para optimizar la gestión de inventarios, el control de stock y el registro de movimientos en pequeños negocios o almacenes. Cuenta con un sistema seguro de inicio de sesión, control de productos en tiempo real, alertas de stock bajo y una interfaz moderna y dinámica.

---

## Funcionalidades Implementadas
* **Autenticación de Usuarios:** Pantalla de inicio de sesión conectada a ViewModel para validar credenciales y mantener la sesión activa del usuario.
* **Pantalla de Bienvenida:** Panel principal con navegación directa hacia el catálogo, alertas y registro de productos, acompañado de un logotipo animado nativo.
* **Catálogo de Productos:** Visualización en lista de los artículos registrados en el inventario con persistencia local mediante **Room Database**.
* **Detalle de Producto:** Consulta detallada de la información de cada artículo seleccionado a través de rutas dinámicas.
* **Gestión de Stock y Alertas:** Sistema de monitoreo automático que identifica y lista los productos cuyo stock se encuentra por debajo del mínimo permitido.
* **Formulario de Registro de Productos:** Interfaz completa para dar de alta nuevos artículos incluyendo código, nombre, categoría, stock actual, stock mínimo y **precio** con teclado numérico optimizado.
* **Registro de Movimientos:** Control y trazabilidad de las entradas y salidas de mercancía en el inventario asociadas al usuario activo.

---

## Diseño y planificación
Sketch, wireframe, mockup y demás material de diseño del equipo:
[Carpeta de Drive del equipo](https://drive.google.com/drive/folders/1xR_uXHBmCtapYyAKesBxrgiL0XQPg-tE?usp=drive_link)


[Trello](https://trello.com/b/wayHibfA/movixapp)

---

## Pasos para Ejecutar el Proyecto

### Requisitos Previos
* Tener instalado **Android Studio** (versión recomendada: Koala o superior).
* Contar con **JDK 17** o compatible configurado en el entorno.
* Un dispositivo físico Android con la depuración USB habilitada o un emulador configurado (API 24 o superior).

### Instrucciones de Instalación y Ejecución
1. **Clonar o descargar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
