package com.example.prova

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prova.Service.Notificacao
import com.example.prova.View.Components.CadastroComp
import com.example.prova.View.Components.ListaComputadores
import com.example.prova.View.Components.NavigationDrawer
import com.example.prova.View.Components.TelaCliente
import com.example.prova.View.Components.TelaComputador
import com.example.prova.View.Components.clienteScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            1
        )
        val notificacao = Notificacao(this)
        notificacao.createChannel()
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
                composable("listacomp/{cpf}"){
                    val cpf = it.arguments?.getString("cpf")?:""
                    ListaComputadores(navController,cpf)
                }
                composable("computador/{id}"){
                    val id = it.arguments?.getString("id")?:""
                    TelaComputador(navController,id)
                }
            }
        }
    }
}
