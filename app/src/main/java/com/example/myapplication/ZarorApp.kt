package com.example.myapplication

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.navigation.menuPorRol
import com.example.myapplication.ui.screens.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZarorApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Rol del usuario logueado ("" = nadie)
    var rol by remember { mutableStateOf("") }

    // Ruta actual (como usePathname en Next). Se actualiza sola al navegar.
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    val menu = menuPorRol(rol)
    // El título del header sale del menú; si no está, uno genérico
    val titulo = menu.find { it.ruta == rutaActual }?.titulo ?: "Población Zaror"

    // En Login y Registro no mostramos header
    val mostrarHeader = rutaActual != Screen.Login.route && rutaActual != Screen.Signup.route
    val puedeVolver = navController.previousBackStackEntry != null

    fun cerrarSesion() {
        rol = ""
        navController.navigate(Screen.Login.route) {
            popUpTo(navController.graph.id) { inclusive = true }  // borra el historial
        }
    }

    // ---------- MENÚ LATERAL ----------
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = mostrarHeader,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menú · $rol",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                menu.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.titulo) },
                        selected = item.ruta == rutaActual,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.ruta) { launchSingleTop = true }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        // ---------- HEADER + CONTENIDO ----------
        Scaffold(
            topBar = {
                if (mostrarHeader) {
                    TopAppBar(
                        title = { Text(titulo) },
                        navigationIcon = {
                            Row {
                                if (puedeVolver) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                                    }
                                }
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menú")
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = { cerrarSesion() }) {
                                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        ) { innerPadding ->

            // ---------- {children} ----------
            NavHost(
                navController = navController,
                startDestination = Screen.Login.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(
                        onLoginSuccess = { rolElegido ->
                            rol = rolElegido
                            val inicio = menuPorRol(rolElegido).first().ruta
                            navController.navigate(inicio) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        onGoToSignup = { navController.navigate(Screen.Signup.route) }
                    )
                }
                composable(Screen.Signup.route) {
                    SignupScreen(
                        onSignupSuccess = {
                            rol = "Vecino"
                            navController.navigate(Screen.VecinoFormulario.route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        },
                        onBackToLogin = { navController.popBackStack() }
                    )
                }
                composable(Screen.VecinoFormulario.route) {
                    VecinoFormularioScreen(
                        onSubmitSuccess = { /* TODO: mostrar confirmación */ }
                    )
                }
                composable(Screen.DirectivaSecretaria.route) {
                    DirectivaSecretariaScreen()
                }
                composable(Screen.Tesoreria.route) {
                    TesoreriaScreen(onNavigate = { ruta -> navController.navigate(ruta) })
                }
                composable(Screen.TesoreriaIngresos.route) { PlaceholderScreen("Ingresos") }
                composable(Screen.TesoreriaEgresos.route) { PlaceholderScreen("Egresos") }
                composable(Screen.TesoreriaInformes.route) { PlaceholderScreen("Informes") }
                composable(Screen.Directiva.route) { PlaceholderScreen("Panel de Directiva") }
                composable(Screen.Admin.route) { PlaceholderScreen("Administración") }
            }
        }
    }
}