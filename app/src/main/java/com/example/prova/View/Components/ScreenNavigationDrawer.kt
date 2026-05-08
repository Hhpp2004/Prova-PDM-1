package com.example.prova.View.Components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prova.Models.Cliente
import com.example.prova.Models.Computador
import com.example.prova.R
import com.example.prova.Repository.ClienteRepository
import com.example.prova.Repository.ComputadorRepository
import com.example.prova.Service.Backup
import com.example.prova.Service.Notificacao
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(navController : NavController){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var nome : String by rememberSaveable{mutableStateOf("")}
    var alertaState by remember{mutableStateOf<AlertaMsg?>(null)}
    val backup : Backup = Backup()
    val stateList = remember{
        mutableStateListOf<Cliente>()
    }
    val context : Context = LocalContext.current
    val notificacao = Notificacao(context)
    val isPreview : Boolean = LocalInspectionMode.current
    val clienteController: ClienteRepository = remember(context) {
        ClienteRepository(context)
    }

    val computadorController : ComputadorRepository = remember(context){
        ComputadorRepository(context)
    }
    val listaComp : ArrayList<Computador> = ArrayList<Computador>()
    LaunchedEffect(isPreview,computadorController){
        if(!isPreview){
            listaComp.clear()
            listaComp.addAll(computadorController.findAll())
        }
    }

    val listaCliente : ArrayList<Cliente> = ArrayList<Cliente>()
    LaunchedEffect(isPreview, clienteController) {
        if (!isPreview) {
            stateList.clear()
            listaCliente.clear()
            stateList.addAll(clienteController.findAll())
            listaCliente.addAll(clienteController.findAll())
        }
    }

    ModalNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {

                        Text("Menu", modifier = Modifier.padding(start = 16.dp),fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(15.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(15.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.black),
                                contentColor = colorResource(R.color.white)
                            ),
                            onClick = {
                                navController.navigate("cadastroCliente")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Cadastrar um novo cliente")
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.black),
                                contentColor = colorResource(R.color.white)
                            ),
                            onClick = {
                                navController.navigate("cadastroComputador")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Cadastrar um novo computador")
                        }
                    }

                    Column(verticalArrangement = Arrangement.SpaceEvenly){
                        Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.black),
                            contentColor = colorResource(R.color.white)
                        ),
                        onClick = {
                            if(!listaComp.isEmpty()){
                                backup.saveFile(context,
                                    "todos computadores",
                                    listaComp
                                )
                                notificacao.showNotification("Backup concluido",
                                    "Backup dos computadores concluido com sucesso"
                                )
                            } else {
                                alertaState = AlertaMsg(
                                    "Não foi possivel realizar backup",
                                    "OK",
                                    {
                                    }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Backup dos dados dos computadores")
                    }
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.black),
                                contentColor = colorResource(R.color.white)
                            ),
                            onClick = {
                                if(!listaCliente.isEmpty()){
                                    backup.saveFile2(context,
                                        "todos clientes",
                                        listaCliente
                                    )
                                    notificacao.showNotification("Backup concluido",
                                        "Backup dos cliente concluido com sucesso"
                                    )
                                } else {
                                    alertaState = AlertaMsg(
                                        "Não foi possivel realizar backup",
                                        "OK",
                                        {
                                        }
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Backup dos dados dos clientes")
                        }
                    }
                }
            }
        }
    ){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch{drawerState.open()}
                            }
                        ){
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = colorResource(R.color.white)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.black)
                    )
                )
            }
        ){
            innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(17.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                if(stateList.isEmpty()){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.black),
                                contentColor = colorResource(R.color.white)
                            ),
                            onClick = {
                                navController.navigate("cadastroCliente")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Cadastrar um novo cliente")
                        }
                    }
                } else {
                    Text("Lista de Clientes",
                        fontSize = 22.sp,
                        color = colorResource(R.color.black),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        color = colorResource(R.color.black),
                        thickness = 2.dp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    LazyColumn(modifier = Modifier.fillMaxSize()){
                        items(stateList){ cliente ->
                            Text(
                                color = colorResource(R.color.black),
                                text = cliente.nome,
                                fontSize = 18.sp,
                                modifier = Modifier.fillMaxWidth()
                                    .clickable{
                                        nome = cliente.nome
                                        navController.navigate("cliente/${cliente.cpf}")
                                    }.padding(10.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                                color = colorResource(R.color.black),
                                thickness = 2.dp
                            )
                        }
                    }
                }
            }
        }
    }
    alertaState?.let {
        Alerta(
            config = it,
            onDismiss = { alertaState = null }
        )
    }
}
