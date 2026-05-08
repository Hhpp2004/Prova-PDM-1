package com.example.prova.Models

class Computador (var id: String, var modelo: String, var memoriaRam: Float, var fkCpf: Cliente, var preco: Float){
    override fun toString(): String {
        return "Computador(" +
                "id='$id', " +
                "modelo='$modelo', " +
                "memoriaRam=$memoriaRam, " +
                "fkCpf=$fkCpf, " +
                "preco=$preco" +
                ")"
    }
}
