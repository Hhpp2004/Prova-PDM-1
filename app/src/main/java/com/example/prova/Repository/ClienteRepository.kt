package com.example.prova.Repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.prova.Database.BancoDeDados
import com.example.prova.Models.Cliente

class ClienteRepository(context : Context) {
    private val myDatabase = BancoDeDados(context)

    fun save(cliente : Cliente) {
        val dbInsert = myDatabase.writableDatabase
        try{
            val values = ContentValues().apply{
                put("cpf",cliente.cpf)
                put("nome",cliente.nome)
                put("email",cliente.email)
                put("telefone",cliente.telefone)
            }
            dbInsert?.insert("cliente",null,values)
        } finally {
            dbInsert.close()
        }
    }

    fun findAll() : ArrayList<Cliente> {
        val dbRead = myDatabase.readableDatabase
        lateinit var read : Cursor
        try{
            read = dbRead.rawQuery("SELECT * FROM cliente",null)
            val list : ArrayList<Cliente> = ArrayList()
            var cliente : Cliente
            with(read){
                while(moveToNext()){
                    val cpf = getString(getColumnIndexOrThrow("cpf"))
                    val nome = getString(getColumnIndexOrThrow("nome"))
                    val email = getString(getColumnIndexOrThrow("email"))
                    val telefone = getString(getColumnIndexOrThrow("telefone"))
                    val foto = getString(getColumnIndexOrThrow("foto"))
                    cliente = Cliente(email,telefone,nome,cpf,foto)
                    list.add(cliente)
                }
            }
            return list
        } finally {
            read.close()
            dbRead.close()
        }
    }

    fun findByCpf(cpf: String) : Cliente? {
        val dbRead = myDatabase.readableDatabase
        try{
            dbRead.rawQuery(
                "SELECT * FROM cliente WHERE cpf = ?",
                arrayOf(cpf)
            ).use { cursor ->
                if(cursor.moveToFirst()){
                    return Cliente(
                        cpf = cursor.getString(cursor.getColumnIndexOrThrow("cpf")),
                        nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                        telefone = cursor.getString(cursor.getColumnIndexOrThrow("telefone")),
                        email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        foto = cursor.getString(cursor.getColumnIndexOrThrow("foto"))
                    )
                }
                return null
            }
        } finally {
            dbRead.close()
        }
    }

    fun updateByPhoto(cpf : String, pathPhoto : String){
        val dbUpdate = myDatabase.writableDatabase
        try{
            val values = ContentValues().apply{
                put("foto",pathPhoto)
            }
            dbUpdate.update("cliente",values,"cpf = ?",arrayOf(cpf))
        } finally {
            dbUpdate.close()
        }
    }

    fun updateByCpf(cliente: Cliente, cpf: String?) {
        val dbUpdate = myDatabase.writableDatabase
        try{
            val values = ContentValues().apply{
                put("cpf",cliente.cpf)
                put("email",cliente.email)
                put("nome",cliente.nome)
                put("telefone",cliente.telefone)
            }

            cpf?.let{ oldCpf ->
                dbUpdate.update("cliente", values, "cpf = ?", arrayOf(oldCpf))
                val computadorValues = ContentValues().apply {
                    put("fk_cpf", cliente.cpf)
                }
                dbUpdate.update("computador", computadorValues, "fk_cpf = ?", arrayOf(oldCpf))
            }
        }  finally {
            dbUpdate.close()
        }
    }
    
    fun delete(cpf : String) {
        val dbDelete = myDatabase.writableDatabase
        try{
            dbDelete.delete("computador", "fk_cpf = ?", arrayOf(cpf))
            dbDelete.delete("cliente", "cpf = ?", arrayOf(cpf))
        } finally {
            dbDelete.close()
        }
    }
}
