package com.example.prova.View.Components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.prova.Models.Cliente
import com.example.prova.R
import com.example.prova.Repository.ClienteRepository
import com.example.prova.Service.Notificacao
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.roundToInt

private data class CropPosition(
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float
)

private fun cropPosition(
    bitmapWidth: Int,
    bitmapHeight: Int,
    frameSize: Float,
    zoom: Float,
    offsetX: Float,
    offsetY: Float
) : CropPosition {
    val scale = max(frameSize / bitmapWidth, frameSize / bitmapHeight) * zoom
    val width = bitmapWidth * scale
    val height = bitmapHeight * scale

    return CropPosition(
        left = (frameSize - width) / 2f + offsetX * frameSize / 2f,
        top = (frameSize - height) / 2f + offsetY * frameSize / 2f,
        width = width,
        height = height
    )
}

private fun cropPhoto(bitmap: Bitmap, zoom: Float, offsetX: Float, offsetY: Float) : Bitmap {
    val outputSize = 800
    val output = Bitmap.createBitmap(outputSize, outputSize, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(output)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    val position = cropPosition(
        bitmap.width,
        bitmap.height,
        outputSize.toFloat(),
        zoom,
        offsetX,
        offsetY
    )

    canvas.drawBitmap(
        bitmap,
        null,
        RectF(
            position.left,
            position.top,
            position.left + position.width,
            position.top + position.height
        ),
        paint
    )

    return output
}

private fun loadBitmapFromUri(context : Context, uri : Uri) : Bitmap {
    val source = ImageDecoder.createSource(context.contentResolver, uri)
    return ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
    }
}

fun savePhoto(context : Context, bitmap : Bitmap, cpf : String) : String {
    val pasta = File(context.filesDir,"Fotos")
    if(!pasta.exists()){
        pasta.mkdirs()
    }
    val arquivo = File(pasta, "$cpf.jpg")
    val output = FileOutputStream(arquivo)
    bitmap.compress(
        Bitmap.CompressFormat.JPEG,
        100,
        output
    )
    output.flush()
    output.close()

    return arquivo.absolutePath
}

fun copyPhotoFromGalery(context : Context, uri : Uri, nomeFile : String) : String{
    val input = context.contentResolver.openInputStream(uri)
    val pasta = File(context.filesDir,"Fotos")
    if(!pasta.exists()){
        pasta.mkdirs()
    }

    val arquivo = File(pasta, "$nomeFile.jpg")
    val output = FileOutputStream(arquivo)
    input?.copyTo(output)
    input?.close()
    output.close()
    return arquivo.absolutePath
}

@Composable
fun PhotoCropDialog(
    bitmap : Bitmap,
    onDismiss : () -> Unit,
    onConfirm : (Bitmap) -> Unit
){
    var zoom by rememberSaveable(bitmap){ mutableStateOf(1f) }
    var offsetX by rememberSaveable(bitmap){ mutableStateOf(0f) }
    var offsetY by rememberSaveable(bitmap){ mutableStateOf(0f) }
    val imageBitmap = remember(bitmap) { bitmap.asImageBitmap() }
    val sliderColors = SliderDefaults.colors(
        thumbColor = colorResource(R.color.black),
        activeTrackColor = colorResource(R.color.black),
        inactiveTrackColor = colorResource(R.color.cinza)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Enquadrar foto",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Canvas(
                    modifier = Modifier
                        .size(260.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.black))
                ) {
                    val position = cropPosition(
                        bitmap.width,
                        bitmap.height,
                        size.width,
                        zoom,
                        offsetX,
                        offsetY
                    )

                    drawImage(
                        image = imageBitmap,
                        dstOffset = androidx.compose.ui.unit.IntOffset(
                            position.left.roundToInt(),
                            position.top.roundToInt()
                        ),
                        dstSize = androidx.compose.ui.unit.IntSize(
                            position.width.roundToInt(),
                            position.height.roundToInt()
                        )
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))
                Text("Zoom", fontWeight = FontWeight.Bold)
                Slider(
                    value = zoom,
                    onValueChange = { zoom = it },
                    valueRange = 1f..3f,
                    colors = sliderColors
                )
                Text("Horizontal", fontWeight = FontWeight.Bold)
                Slider(
                    value = offsetX,
                    onValueChange = { offsetX = it },
                    valueRange = -1f..1f,
                    colors = sliderColors
                )
                Text("Vertical", fontWeight = FontWeight.Bold)
                Slider(
                    value = offsetY,
                    onValueChange = { offsetY = it },
                    valueRange = -1f..1f,
                    colors = sliderColors
                )
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.black),
                    contentColor = colorResource(R.color.white)
                ),
                onClick = {
                    onConfirm(cropPhoto(bitmap, zoom, offsetX, offsetY))
                }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.white),
                    contentColor = colorResource(R.color.black)
                ),
                border = BorderStroke(
                    2.dp,
                    colorResource(R.color.black)
                ),
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}


@Composable
fun fotoPerfil(navController : NavController, cliente : Cliente, image : Uri?){


    Box(modifier = Modifier
        .fillMaxWidth()
        .height(140.dp)
        .background(colorResource(R.color.black))
    ){
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
        Surface(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.TopEnd),
            shape = CircleShape,
            color = colorResource(R.color.black)
        ){
            if(image != null || cliente.foto != null){
                AsyncImage(
                    model =  image ?: cliente.foto,
                    contentDescription  = "Foto perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = colorResource(R.color.white),
                        modifier = Modifier
                            .size(60.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun TelaCliente(navController : NavController, cpfCliente : String){
    var telefone by rememberSaveable{mutableStateOf("")}
    var email by rememberSaveable{mutableStateOf("")}
    var nome by rememberSaveable{mutableStateOf("")}
    var alertaState by remember{mutableStateOf<AlertaMsg?>(null)}
    var cpf by rememberSaveable{mutableStateOf("")}
    var clienteBanco by remember{mutableStateOf<Cliente?>(null)}
    var openDialog by remember{
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val notification = Notificacao(context)
    val controller = remember(context) {
        ClienteRepository(context)
    }
    LaunchedEffect(cpfCliente) {
        clienteBanco = controller.findByCpf(cpfCliente)
    }
    var image by remember{
        mutableStateOf<Uri?>(null)
    }
    var fotoParaEditar by remember{
        mutableStateOf<Bitmap?>(null)
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        if(uri != null){
            fotoParaEditar = loadBitmapFromUri(context, uri)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->

        if(bitmap != null){
            fotoParaEditar = bitmap
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { permissaoLiberada ->
        if(permissaoLiberada){
            cameraLauncher.launch(null)
        } else {
            alertaState = AlertaMsg(
                "Permissão da câmera negada",
                "OK",
                onConfirm = {}
            )
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.white))) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(colorResource(R.color.black))
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            if(clienteBanco != null){
                fotoPerfil(navController, clienteBanco!!, image)
            } else {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .align(Alignment.CenterVertically),
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
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.black)),
                        contentAlignment = Alignment.Center){
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = colorResource(R.color.white),
                            modifier = Modifier
                                .size(60.dp)
                        )
                    }
                }
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
            modifier = Modifier
                .width(380.dp)
                .padding(start = 13.dp)
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
            modifier = Modifier
                .width(380.dp)
                .padding(start = 13.dp)
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
            modifier = Modifier
                .width(380.dp)
                .padding(start = 13.dp)
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
            modifier = Modifier
                .width(380.dp)
                .padding(start = 13.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .width(150.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.white),
                    contentColor = colorResource(R.color.black)
                ),
                border = BorderStroke(
                    2.dp,
                    colorResource(R.color.black)
                ),
                onClick = {
                    val clienteAtual = clienteBanco ?: return@Button
                    val cliente = Cliente(
                        nome = if(nome.isNotBlank()) nome else clienteAtual.nome,
                        telefone = if(telefone.isNotBlank()) telefone else clienteAtual.telefone,
                        email = if(email.isNotBlank()) email else clienteAtual.email,
                        cpf = if(cpf.isNotBlank()) cpf else clienteAtual.cpf,
                        foto = clienteAtual.foto
                    )
                    controller.updateByCpf(cliente, clienteAtual.cpf)
                    notification.showNotification("Dados atualizados","Os seus dados ${nome} foram atualizados com sucesso")
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
            Spacer(modifier = Modifier.width(50.dp))
            Button(
                modifier = Modifier
                    .padding(start = 12.dp, end = 10.dp)
                    .height(40.dp)
                    .width(230.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.vermelho),
                    contentColor = colorResource(R.color.white)
                ),
                onClick = {
                    val cpfParaApagar = clienteBanco?.cpf ?: cpfCliente
                    controller.delete(cpfParaApagar)
                    notification.showNotification("Dados atualizados", "Os seus dados foram apagados com sucesso")
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
                navController.navigate("listacomp/${clienteBanco?.cpf ?: cpfCliente}")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 12.dp),
            shape = RoundedCornerShape(50)
        ){
            Text(
                "Lista de computadores",
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.white),
            contentColor = colorResource(R.color.black)
            ),
            onClick = {
                openDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 12.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(
                2.dp,
                colorResource(R.color.black)
            )
        ){
            Text("Adicionar uma foto de perfil",
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
    if(openDialog){
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },

            title = {
                Text(
                    "Foto de perfil",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Text(
                    "Escolha uma opção"
                )
            },

            confirmButton = {

                Column {

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.black),
                            contentColor = colorResource(R.color.white)
                        ),
                        onClick = {
                            if(
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ){
                                cameraLauncher.launch(null)
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }

                            openDialog = false
                        }
                    ) {

                        Text("Tirar foto")

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.white),
                            contentColor = colorResource(R.color.black)
                        ),
                        border = BorderStroke(
                            2.dp,
                            colorResource(R.color.black)
                        ),
                        onClick = {

                            galeriaLauncher.launch("image/*")

                            openDialog = false
                        }
                    ) {

                        Text("Escolher da galeria")

                    }
                }
            }
        )
    }
    fotoParaEditar?.let { bitmap ->
        PhotoCropDialog(
            bitmap = bitmap,
            onDismiss = {
                fotoParaEditar = null
            },
            onConfirm = { bitmapEditado ->
                val cpfFoto = clienteBanco?.cpf ?: cpfCliente
                val caminho = savePhoto(
                    context,
                    bitmapEditado,
                    cpfFoto
                )

                controller.updateByPhoto(cpfFoto, caminho)
                image = Uri.fromFile(File(caminho))
                clienteBanco = clienteBanco?.let { clienteAtual ->
                    Cliente(
                        email = clienteAtual.email,
                        telefone = clienteAtual.telefone,
                        nome = clienteAtual.nome,
                        cpf = clienteAtual.cpf,
                        foto = caminho
                    )
                }
                fotoParaEditar = null
            }
        )
    }
    alertaState?.let {
        Alerta(
            config = it,
            onDismiss = { alertaState = null }
        )
    }
}
