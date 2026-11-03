package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.Screens.SignupScreen
import com.example.myapplication.ui.Screens.VecinoFormularioScreen
import com.example.myapplication.ui.Screens.DirectivaSecretariaScreen
import com.example.myapplication.ui.Screens.TesoreriaScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Signup.route
                    ) {
                        // 1. Pantalla de Registro
                        composable(Screen.Signup.route) {
                            SignupScreen(
                                onSignupSuccess = {
                                    // Al registrarse con éxito, navega al formulario de arriendo
                                    navController.navigate(Screen.VecinoFormulario.route)
                                },
                                onBackToLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 2. Pantalla de Formulario de Arriendo (Vecino)
                        composable(Screen.VecinoFormulario.route) {
                            VecinoFormularioScreen(
                                onSubmitSuccess = {
                                    navController.popBackStack()
                                },
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 3. Pantalla de Secretaría / Directiva
                        composable(Screen.DirectivaSecretaria.route) {
                            DirectivaSecretariaScreen(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }


                        // 4. Pantalla de Tesorería (Ingresos y Egresos)
                        composable(Screen.Tesoreria.route) {
                            TesoreriaScreen(
                                onNavigate = { routeName ->
                                    navController.navigate(routeName)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
