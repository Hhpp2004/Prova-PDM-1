package com.example.prova.View.Components

data class AlertaMsg (
    val mensagem : String,
    val buttonMsg : String,
    val onConfirm : () -> Unit,
    val secondButtonMsg : String? = null,
    val onSecondButton : (() -> Unit)? = null
)
