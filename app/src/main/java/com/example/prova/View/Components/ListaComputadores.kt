package com.example.prova.View.Components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun ListaComputadores(navController : NavController, cpfCliente : String){
    val stateList = remember{
        mutableStateListOf<Computador>()
    }
    var modelo by rememberSaveable{mutableStateOf("")}
    var clienteBanco by remember{mutableStateOf<Cliente?>(null)}
    val context : Context = LocalContext.current
    val isPreview : Boolean = LocalInspectionMode.current

    val clienteRepository : ClienteRepository = remember(context){
        ClienteRepository(context)
    }

    LaunchedEffect(cpfCliente) {
        clienteBanco = clienteRepository.findByCpf(cpfCliente)
    }

    val computadorRepository : ComputadorRepository = remember(context){
        ComputadorRepository(context)
    }

    LaunchedEffect(isPreview, computadorRepository){
        if(!isPreview){
            stateList.clear()
            stateList.addAll(computadorRepository.findByFkCpf(cpfCliente))
        }
    }

    Column(modifier = Modifier.fillMaxSize()
        .background(colorResource(R.color.white))){
        Column(modifier = Modifier.fillMaxWidth().height(100.dp)
            .background(colorResource(R.color.black))){
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                modifier = Modifier.padding(start = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.black),
                    contentColor = colorResource(R.color.white)
                ),
                onClick = {
                    navController.popBackStack()
                }
            ){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "voltar"
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text("Computadores do cliente ${clienteBanco?.nome}",
                modifier = Modifier.padding(start = 20.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            color = colorResource(R.color.black),
            thickness = 2.dp
        )
        Spacer(modifier = Modifier.height(15.dp))
        if(stateList.isEmpty()){
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.black),
                        contentColor = colorResource(R.color.white)
                    ),
                    onClick = {
                        navController.navigate("cadastroComputador/${cpfCliente}")
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 13.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Cadastrar um novo computador")
                }
            }
        } else {
            LazyColumn{
                items(stateList){computador ->
                    Text(
                        color = colorResource(R.color.black),
                        text = computador.modelo,
                        fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth()
                            .clickable{
                                modelo = computador.modelo
                                navController.navigate("computador/${computador.id}")
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
