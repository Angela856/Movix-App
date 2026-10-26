package com.movix.app.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.movix.app.model.Producto
import com.movix.app.ui.utils.NotificacionHelper

@Composable
fun PantallaAlertas(
    productos: List<Producto>,
    modifier: Modifier = Modifier
) {
    val productosEnAlerta = productos.filter { it.enAlerta }

    val context = LocalContext.current
    var permisoNotificacionConcedido by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val launcherNotificacion = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoNotificacionConcedido = concedido
        if (concedido) {
            NotificacionHelper.mostrarNotificacion(
                context,
                "¡Notificaciones activadas!",
                "Ahora recibirás alertas de stock bajo."
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
            .padding(16.dp)
    ) {
        Text(
            text = "Alertas de inventario",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF172554)
        )

        Text(
            text = "Productos con stock bajo o en el mínimo",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
        )

        AnimatedVisibility(
            visible = productosEnAlerta.isNotEmpty(),
            enter = fadeIn() + expandVertically() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + shrinkVertically() + scaleOut(targetScale = 0.8f)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE4E6)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Productos que necesitan atención",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF9F1239)
                    )

                    Text(
                        text = "${productosEnAlerta.size} productos en alerta",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFBE123C),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !permisoNotificacionConcedido) {
                    launcherNotificacion.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    NotificacionHelper.mostrarNotificacion(
                        context,
                        "Alerta de Stock Crítico",
                        "Hay ${productosEnAlerta.size} productos por debajo del mínimo permitido."
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text(if (permisoNotificacionConcedido) "Probar Notificación de Stock" else "Activar Notificaciones")
        }

        Text(
            text = "Listado de productos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
        )

        if (productosEnAlerta.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = "¡Todo en orden! No hay productos en alerta.",
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF166534)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = productosEnAlerta,
                    key = { it.codigo }
                ) { producto ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = producto.nombre,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = "Código: ${producto.codigo}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }

                                Surface(
                                    color = Color(0xFFFFE4E6),
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = "STOCK BAJO",
                                        color = Color(0xFFBE123C),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 6.dp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Categoría: ${producto.categoria}",
                                color = Color.DarkGray
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Stock actual",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "${producto.stockActual} unidades",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFBE123C)
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "Stock mínimo",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "${producto.stockMinimo} unidades",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}