package com.example.prova

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prova.View.Components.CadastroComp
import com.example.prova.View.Components.ListaComputadores
import com.example.prova.View.Components.NavigationDrawer
import com.example.prova.View.Components.TelaCliente
import com.example.prova.View.Components.clienteScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "mainScreen"){
                composable("mainScreen"){
                    NavigationDrawer(navController)
                }
                composable("cadastroCliente"){
                    clienteScreen(navController)
                }
                composable("cadastroComputador"){
                    CadastroComp(navController)
                }
                composable("cadastroComputador/{cpf}"){
                    val cpf = it.arguments?.getString("cpf") ?: ""
                    CadastroComp(navController, cpf)
                }
                composable("cliente/{cpf}"){
                    val cpf = it.arguments?.getString("cpf") ?: ""
                    TelaCliente(navController, cpf)
                }
                composable("computador/{cpf}"){
                    val cpf = it.arguments?.getString("cpf")?:""
                    ListaComputadores(navController,cpf)
                }
            }
        }
    }
}
