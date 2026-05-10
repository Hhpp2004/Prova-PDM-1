package com.example.prova.Models

class Cliente (var email: String, var telefone: String, var nome: String, var cpf: String, var foto : String?){
    override fun toString(): String {
        return "Cliente(" +
                "email='$email', " +
                "telefone='$telefone', " +
                "nome='$nome', " +
                "cpf='$cpf'" +
                ")"
    }
}