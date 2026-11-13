package com.example.myapplication

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.ZarorRepository
import com.example.myapplication.ui.components.LocalMensajes
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.navigation.menuPorRol
import com.example.myapplication.ui.screens.* // no borrar, así se importan todas las screens
import com.example.myapplication.ui.viewmodel.AuthViewModel
import com.example.myapplication.ui.viewmodel.TesoreriaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZarorApp() {

    // Mientras Room y DataStore cargan, mostramos un indicador de carga
    if (!ZarorRepository.cargado) {
        PantallaCarga()
        return
    }

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Snackbar único para toda la app
    val snackbarHostState = remember { SnackbarHostState() }

    fun avisar(mensaje: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()   // reemplaza el mensaje anterior
            snackbarHostState.showSnackbar(mensaje)
        }
    }

    // ViewModels compartidos por toda la app
    val tesoreriaViewModel: TesoreriaViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    // Usuario con sesión iniciada y su rol (null = nadie)
    val usuario = authViewModel.usuarioActual
    val rol = usuario?.rol

    // Si había una sesión guardada, la app parte en la pantalla de inicio de ese rol
    val pantallaInicial = remember {
        usuario?.let { menuPorRol(it.rol).first().ruta } ?: Screen.Login.route
    }

    // Ruta actual. Se actualiza sola al navegar.
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    val menu = menuPorRol(rol)
    // El título del header sale del menú; si no está, uno genérico
    val titulo = menu.find { it.ruta == rutaActual }?.titulo ?: "Población Zaror"

    // En Login y Registro no mostramos header ni menú lateral
    val mostrarHeader = rutaActual != Screen.Login.route && rutaActual != Screen.Signup.route
    val puedeVolver = navController.previousBackStackEntry != null

    fun cerrarSesion() {
        authViewModel.cerrarSesion()
        navController.navigate(Screen.Login.route) {
            popUpTo(navController.graph.id) { inclusive = true }  // borra el historial
        }
        avisar("Sesión cerrada")
    }

    // ---------- MENÚ LATERAL ----------
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = mostrarHeader,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "${usuario?.nombre ?: ""} · ${rol?.nombre ?: ""}",
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
            snackbarHost = { SnackbarHost(snackbarHostState) },
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

            // Todas las pantallas de adentro pueden usar avisar(...)
            CompositionLocalProvider(LocalMensajes provides { mensaje -> avisar(mensaje) }) {

                // ---------- NAVHOST / RUTAS ----------
                NavHost(
                    navController = navController,
                    startDestination = pantallaInicial,
                    modifier = Modifier.padding(innerPadding),
                    // Al entrar a una pantalla: se desliza desde la derecha y aparece
                    enterTransition = { slideInHorizontally(tween(300)) { it / 3 } + fadeIn(tween(300)) },
                    exitTransition = { fadeOut(tween(200)) },
                    // Al volver atrás: se desliza desde la izquierda
                    popEnterTransition = { slideInHorizontally(tween(300)) { -it / 3 } + fadeIn(tween(300)) },
                    popExitTransition = { fadeOut(tween(200)) }
                ) {
                    // 1. Login
                    composable(Screen.Login.route) {
                        LoginScreen(
                            authViewModel = authViewModel,
                            onLoginSuccess = { rolElegido ->
                                val inicio = menuPorRol(rolElegido).first().ruta
                                navController.navigate(inicio) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                                avisar("Hola, ${authViewModel.usuarioActual?.nombre ?: ""} 👋")
                            },
                            onGoToSignup = { navController.navigate(Screen.Signup.route) }
                        )
                    }

                    // 2. Registro
                    composable(Screen.Signup.route) {
                        SignupScreen(
                            authViewModel = authViewModel,
                            onSignupSuccess = {
                                navController.navigate(Screen.VecinoFormulario.route) {
                                    popUpTo(navController.graph.id) { inclusive = true }
                                }
                                avisar("¡Cuenta creada! Bienvenida/o")
                            },
                            onBackToLogin = { navController.popBackStack() }
                        )
                    }

                    // 3. Vecino: formulario y mis reservas
                    composable(Screen.VecinoFormulario.route) {
                        VecinoFormularioScreen(
                            usuario = usuario,
                            onSubmitSuccess = {
                                navController.navigate(Screen.MisReservas.route) { launchSingleTop = true }
                                avisar("Solicitud enviada ✔")
                            }
                        )
                    }
                    composable(Screen.MisReservas.route) {
                        MisReservasScreen(usuario = usuario)
                    }

                    // 4. Secretaría
                    composable(Screen.DirectivaSecretaria.route) {
                        DirectivaSecretariaScreen()
                    }

                    // 5. Tesorería y sus submódulos (comparten el mismo ViewModel)
                    composable(Screen.Tesoreria.route) {
                        TesoreriaScreen(onNavigate = { ruta -> navController.navigate(ruta) })
                    }
                    composable(Screen.TesoreriaMovimientos.route) {
                        MovimientosScreen(viewModel = tesoreriaViewModel)
                    }
                    composable(Screen.TesoreriaInformes.route) {
                        TesoreriaInformesScreen(viewModel = tesoreriaViewModel)
                    }

                    // 6. Directiva
                    composable(Screen.Directiva.route) {
                        DirectivaSecretariaScreen()
                    }

                    // 7. Panel de Administrador
                    composable(Screen.Admin.route) {
                        AdminScreen()
                    }
                }   // cierra NavHost
            }       // cierra CompositionLocalProvider
        }
    }
}

// Indicador de carga mientras Room y DataStore leen los datos guardados
@Composable
private fun PantallaCarga() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}