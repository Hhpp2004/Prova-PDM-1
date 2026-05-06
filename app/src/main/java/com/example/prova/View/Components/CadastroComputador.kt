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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prova.Models.Cliente
import com.example.prova.Models.Computador
import com.example.prova.R
import com.example.prova.Repository.ClienteRepository
import com.example.prova.Repository.ComputadorRepository
import java.util.UUID.randomUUID

@Composable
fun CadastroComp(navController : NavController, cpfCliente : String = ""){
    var modelo by rememberSaveable{mutableStateOf("")}
    var memoriaRam by rememberSaveable{mutableStateOf("")}
    var fkcpf by rememberSaveable{mutableStateOf(cpfCliente)}
    var preco by rememberSaveable{mutableStateOf("")}
    var alertaState by remember{mutableStateOf<AlertaMsg?>(null)}
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val controller = remember(context) {
        ComputadorRepository(context)
    }

    val clienteRepository = remember(context){
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
            text = "Cadastro de Computador",
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
            value = modelo,
            onValueChange = {modelo = it},
            placeholder = {Text("Digite o modelo")},
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
            placeholder = {Text("Digite o tamanho da memoria RAM")},
            modifier = Modifier.width(380.dp).padding(start = 13.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
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
            placeholder = {Text("Digite o preco da maquina")},
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
            value = fkcpf,
            onValueChange = {fkcpf = it},
            placeholder = {Text("Digite o CPF do dono")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
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
                if(modelo.isNotEmpty() && memoriaRam.isNotEmpty() && fkcpf.isNotEmpty() && preco.isNotEmpty()){
                    val memoriaRamNumero = memoriaRam.toFloatOrNull()
                    val precoNumero = preco.toFloatOrNull()
                    if(memoriaRamNumero == null || precoNumero == null){
                        alertaState = AlertaMsg(
                            "Digite valores numericos validos para memoria RAM e preco",
                            "OK",
                            onConfirm = {}
                        )
                        return@Button
                    }

                    val clienteEncontrado : Cliente? = clienteRepository.findByCpf(fkcpf)
                    if(clienteEncontrado == null){
                        alertaState = AlertaMsg(
                            "Usuario não existe, crie uma conta para ele",
                            "Cadastrar um novo cliente",
                            {
                                navController.navigate("cadastroCliente")
                            }
                        )
                    } else {
                        val computador = Computador(
                            id = randomUUID().toString(),
                            modelo = modelo,
                            memoriaRam = memoriaRamNumero,
                            fkCpf = clienteEncontrado,
                            preco = precoNumero
                        )
                        controller.save(computador)
                        navController.popBackStack()
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
