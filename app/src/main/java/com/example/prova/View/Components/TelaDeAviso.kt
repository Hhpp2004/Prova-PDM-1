package com.example.prova.View.Components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.prova.R


@Composable
fun Alerta(config: AlertaMsg, onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = { onDismiss() },

        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.black),
                    contentColor = colorResource(R.color.white)
                ),
                onClick = {
                    onDismiss()
                    config.onConfirm()
                }
            ) {
                Text(config.buttonMsg)
            }
        },

        dismissButton = {
            if(config.secondButtonMsg != null && config.onSecondButton != null){
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.white),
                        contentColor = colorResource(R.color.black)
                    ),
                    onClick = {
                        onDismiss()
                        config.onSecondButton()
                    }
                ) {
                    Text(config.secondButtonMsg)
                }
            }
        },

        text = {
            Text(config.mensagem)
        }
    )
}
