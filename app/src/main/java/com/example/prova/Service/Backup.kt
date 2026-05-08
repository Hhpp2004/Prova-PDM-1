package com.example.prova.Service

import android.content.Context
import android.os.Environment
import com.example.prova.Models.Cliente
import com.example.prova.Models.Computador
import java.io.File

class Backup {
    fun saveFile(context: Context, nomeArquivo: String, obj: ArrayList<Computador>){
        val pasta =  Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        if(!pasta.exists()){
            pasta.mkdirs()
        }
        val arquivo = File(pasta,nomeArquivo+".txt")
        if(!arquivo.exists()){
            arquivo.createNewFile()
        }
        val conteudo = obj.joinToString("\n")
        arquivo.writeText(conteudo)
    }

    fun saveFile2(context: Context, nomeArquivo: String, obj: ArrayList<Cliente>){
        val pasta =  Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        if(!pasta.exists()){
            pasta.mkdirs()
        }
        val arquivo = File(pasta,nomeArquivo+".txt")
        if(!arquivo.exists()){
            arquivo.createNewFile()
        }
        val conteudo = obj.joinToString("\n")
        arquivo.writeText(conteudo)
    }
}