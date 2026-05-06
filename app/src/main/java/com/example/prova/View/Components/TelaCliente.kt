package com.example.prova.View.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prova.Models.Cliente
import com.example.prova.R
import com.example.prova.Repository.ClienteRepository


@Composable
fun TelaCliente(navController : NavController, cpfCliente : String){
    var telefone by rememberSaveable{mutableStateOf("")}
    var email by rememberSaveable{mutableStateOf("")}
    var nome by rememberSaveable{mutableStateOf("")}
    var alertaState by remember{mutableStateOf<AlertaMsg?>(null)}
    var cpf by rememberSaveable{mutableStateOf("")}
    var clienteBanco by remember{mutableStateOf<Cliente?>(null)}

    val context = LocalContext.current
    val controller = remember(context) {
        ClienteRepository(context)
    }

    LaunchedEffect(cpfCliente) {
        clienteBanco = controller.findByCpf(cpfCliente)
    }

    Column(modifier = Modifier.fillMaxSize().background(colorResource(R.color.white))) {
        Column(
            modifier = Modifier.fillMaxWidth().height(100.dp)
                .background(colorResource(R.color.black))
        ) {
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
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "voltar"
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            "Dados do ${clienteBanco?.nome}",
            fontSize = 22.sp,
            color = colorResource(R.color.black),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            color = colorResource(R.color.black),
            thickness = 2.dp
        )
        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            color = colorResource(R.color.black),
            thickness = 2.dp
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            colors = colors(
                focusedBorderColor = colorResource(R.color.black),
                unfocusedBorderColor = colorResource(R.color.black),
                focusedLabelColor = colorResource(R.color.black),
                cursorColor = colorResource(R.color.black)
            ),
            shape = RoundedCornerShape(10.dp),
            value = nome,
            onValueChange = { nome = it },
            placeholder = { Text(clienteBanco?.nome ?: "Nome") },
            modifier = Modifier.width(380.dp).padding(start = 13.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            colors = colors(
                focusedBorderColor = colorResource(R.color.black),
                unfocusedBorderColor = colorResource(R.color.black),
                focusedLabelColor = colorResource(R.color.black),
                cursorColor = colorResource(R.color.black)
            ),
            shape = RoundedCornerShape(10.dp),
            value = cpf,
            onValueChange = { cpf = it },
            placeholder = { Text(clienteBanco?.cpf ?: "CPF") },
            modifier = Modifier.width(380.dp).padding(start = 13.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            colors = colors(
                focusedBorderColor = colorResource(R.color.black),
                unfocusedBorderColor = colorResource(R.color.black),
                focusedLabelColor = colorResource(R.color.black),
                cursorColor = colorResource(R.color.black)
            ),
            shape = RoundedCornerShape(10.dp),
            value = email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            onValueChange = { email = it },
            placeholder = { Text(clienteBanco?.email ?: "Email") },
            modifier = Modifier.width(380.dp).padding(start = 13.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            colors = colors(
                focusedBorderColor = colorResource(R.color.black),
                unfocusedBorderColor = colorResource(R.color.black),
                focusedLabelColor = colorResource(R.color.black),
                cursorColor = colorResource(R.color.black)
            ),
            shape = RoundedCornerShape(10.dp),
            value = telefone,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            onValueChange = { telefone = it },
            placeholder = { Text(clienteBanco?.telefone ?: "Telefone") },
            modifier = Modifier.width(380.dp).padding(start = 13.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.padding(start = 12.dp)
                    .width(120.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.white),
                    contentColor = colorResource(R.color.black)
                ),
                onClick = {
                    val clienteAtual = clienteBanco ?: return@Button
                    val cliente = Cliente(
                        nome = if(nome.isNotBlank()) nome else clienteAtual.nome,
                        telefone = if(telefone.isNotBlank()) telefone else clienteAtual.telefone,
                        email = if(email.isNotBlank()) email else clienteAtual.email,
                        cpf = if(cpf.isNotBlank()) cpf else clienteAtual.cpf
                    )
                    controller.updateByCpf(cliente, clienteAtual.cpf)
                    clienteBanco = cliente
                    nome = ""
                    telefone = ""
                    email = ""
                    cpf = ""
                    alertaState = AlertaMsg(
                        "Dados atualizados com sucesso",
                        "OK",
                        {}
                    )
                }
            ) {
                Text(
                    "Atualizar",
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.width(80.dp))
            Button(
                modifier = Modifier.padding(start = 12.dp).height(40.dp)
                    .width(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.vermelho),
                    contentColor = colorResource(R.color.white)
                ),
                onClick = {
                    val cpfParaApagar = clienteBanco?.cpf ?: cpfCliente
                    controller.delete(cpfParaApagar)
                    navController.popBackStack()
                }
            ){
                Text(
                    "Apagar",
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.black),
                contentColor = colorResource(R.color.white)
            ),
            onClick = {
                navController.navigate("computador/${clienteBanco?.cpf ?: cpfCliente}")
            },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 12.dp),
            shape = RoundedCornerShape(50)
        ){
            Text(
                "Lista de computadores",
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
    alertaState?.let {
        Alerta(
            config = it,
            onDismiss = { alertaState = null }
        )
    }
}
