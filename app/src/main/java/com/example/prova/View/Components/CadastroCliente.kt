package com.example.prova.View.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.example.prova.Models.Cliente
import com.example.prova.R
import com.example.prova.Repository.ClienteRepository

@Composable
fun clienteScreen(navController : NavController){
    var telefone by rememberSaveable{mutableStateOf("")}
    var email by rememberSaveable{mutableStateOf("")}
    var nome by rememberSaveable{mutableStateOf("")}
    var cpf by rememberSaveable{mutableStateOf("")}
    var alertaState by remember{mutableStateOf<AlertaMsg?>(null)}

    val context = LocalContext.current
    val controller = remember(context) {
        ClienteRepository(context)
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
        Text(
            text = "Cadastro de Cliente",
            fontSize = 22.sp,
            color = colorResource(R.color.black),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider(modifier = Modifier.padding(start = 10.dp, end = 10.dp),
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
            onValueChange = {nome = it},
            placeholder = {Text("Digite o seu nome")},
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChange = {cpf = it},
            placeholder = {Text("Digite o seu CPF")},
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
            onValueChange = {email = it},
            placeholder = {Text("Digite o seu email")},
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
            onValueChange = {telefone = it},
            placeholder = {Text("Digite o seu telefone")},
            modifier = Modifier.width(380.dp).padding(start = 13.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            modifier = Modifier.padding(start = 12.dp,end = 12.dp).width(380.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.black),
                contentColor = colorResource(R.color.white)
            ),
            onClick = {
                if(nome.isNotEmpty() && telefone.isNotEmpty() && cpf.isNotEmpty() && email.isNotEmpty()){
                    val aux = controller.findByCpf(cpf)
                    if(aux == null){
                        var cliente : Cliente = Cliente(nome = nome,
                            telefone = telefone,
                            cpf = cpf,
                            email = email
                        )
                        controller.save(cliente)
                        navController.popBackStack()
                    } else {
                        alertaState = AlertaMsg(
                            "Esse Cliente já existe",
                            "OK",
                            {
                                navController.popBackStack()
                            }
                        )
                    }
                } else {

                    alertaState = AlertaMsg(
                        "Preencha todos os campos",
                        "OK",
                        onConfirm = {}
                    )
                }
            }
        ){
            Text("Registrar",
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