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
import com.example.prova.Models.Computador
import com.example.prova.R
import com.example.prova.Repository.ClienteRepository
import com.example.prova.Repository.ComputadorRepository
import com.example.prova.Service.Notificacao

@Composable
fun TelaComputador(navController : NavController, id : String){
    var modelo by rememberSaveable{mutableStateOf("")}
    var memoriaRam by rememberSaveable{mutableStateOf("")}
    var preco by rememberSaveable{mutableStateOf("")}
    var fkCpf by rememberSaveable{mutableStateOf("")}
    var alertaState by remember{mutableStateOf<AlertaMsg?>(null)}
    val context = LocalContext.current
    val notificacao = Notificacao(context)
    val computadorController = remember(context){
        ComputadorRepository(context)
    }
    val clienteController = remember(context){
        ClienteRepository(context)
    }

    val computadorBanco = computadorController.findById(id)
    val clienteBanco = computadorBanco.fkCpf

    Column(modifier = Modifier.fillMaxSize().background(colorResource(R.color.white))){
        Column(modifier = Modifier.fillMaxWidth().height(100.dp).background(colorResource(R.color.black))){
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
        Text("Computador do ${clienteBanco.nome}",
            fontSize = 22.sp,
            color = colorResource(R.color.black),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text("ID: ${computadorBanco.id}",
            fontSize = 22.sp,
            color = colorResource(R.color.black),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp))
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
            value = modelo,
            onValueChange = {modelo = it},
            placeholder = {Text(computadorBanco.modelo)},
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
            value = memoriaRam,
            onValueChange = {
                if(it.all {char -> char.isDigit()}){
                    memoriaRam = it
                }
            },
            placeholder = {Text("${computadorBanco.memoriaRam} gb")},
            modifier = Modifier.width(380.dp).padding(start = 13.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
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
            value = preco,
            onValueChange = {
                if(it.all { char -> char.isDigit() || char == '.' }){
                    preco = it
                }
            },
            placeholder = {Text("R$ ${computadorBanco.preco}")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
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
            value = fkCpf,
            onValueChange = {fkCpf = it},
            placeholder = {Text("Dono: ${computadorBanco.fkCpf.cpf}")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
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
                    val cpfDono = fkCpf.ifEmpty { computadorBanco.fkCpf.cpf }
                    val donoComputador = clienteController.findByCpf(cpfDono)

                    if(donoComputador == null){
                        alertaState = AlertaMsg(
                            "Cliente não encontrado na base de dados",
                            "Cadastrar um novo cliente",
                            {navController.navigate("cadastroCliente")},
                            "Voltar",
                            {}
                        )
                    } else {
                        val computadorNovo = Computador(
                            id = computadorBanco.id,
                            modelo = modelo.ifEmpty { computadorBanco.modelo },
                            preco = if(preco.isNotEmpty()) preco.toFloat() else computadorBanco.preco,
                            memoriaRam = if(memoriaRam.isNotEmpty()) memoriaRam.toFloat() else computadorBanco.memoriaRam,
                            fkCpf = donoComputador
                        )
                        computadorController.updateById(computadorNovo,id)
                        notificacao.showNotification("Dados Atualizados","Os dados do computador atualizados com sucesso")
                        alertaState = AlertaMsg("Dados atualizados com sucesso","OK",{navController.popBackStack()})
                    }
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
                    computadorController.delete(id)
                    notificacao.showNotification("Dados atualizados", "Os seus dados foram apagados com sucesso")
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
    }
    alertaState?.let {
        Alerta(
            config = it,
            onDismiss = { alertaState = null }
        )
    }
}
