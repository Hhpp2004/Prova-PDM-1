package com.example.prova.Repository

import android.content.ContentValues
import android.content.Context
import com.example.prova.Database.BancoDeDados
import com.example.prova.Models.Cliente
import com.example.prova.Models.Computador

class ComputadorRepository(context : Context) {
    private val myDatabase = BancoDeDados(context)

    fun save(computador : Computador) {
        val dbInsert = myDatabase.writableDatabase
        try{
            val values = ContentValues().apply{
                put("id",computador.id)
                put("modelo",computador.modelo)
                put("memoria_ram",computador.memoriaRam)
                put("preco",computador.preco)
                put("fk_cpf",computador.fkCpf.cpf)
            }
            dbInsert?.insert("computador",null,values)
        } finally {
            dbInsert.close()
        }
    }

    fun findAll() : ArrayList<Computador> {
        val dbRead = myDatabase.readableDatabase
        try {
            dbRead.rawQuery("SELECT * FROM computador", null).use { cursor ->
                val list: ArrayList<Computador> = ArrayList()
                with(cursor) {
                    while (moveToNext()) {
                        val id = getString(getColumnIndexOrThrow("id"))
                        val modelo = getString(getColumnIndexOrThrow("modelo"))
                        val memoriaRam = getFloat(getColumnIndexOrThrow("memoria_ram"))
                        val fkCpf = getString(getColumnIndexOrThrow("fk_cpf"))
                        val preco = getFloat(getColumnIndexOrThrow("preco"))
                        val cliente = Cliente(
                            cpf = fkCpf,
                            nome = "",
                            telefone = "",
                            email = "",
                            foto = null
                        )
                            list.add(
                                Computador(
                                    id = id,
                                    modelo = modelo,
                                    memoriaRam = memoriaRam,
                                    fkCpf = cliente,
                                    preco = preco
                                )
                            )

                    }
                }
                return list
            }
        } finally {
            dbRead.close()
        }
    }

    fun findByFkCpf(fkCpf: String?) : ArrayList<Computador> {
        val dbRead = myDatabase.readableDatabase
        try {
            dbRead.rawQuery("SELECT * FROM computador WHERE fk_cpf = ?", arrayOf(fkCpf)).use { cursor ->
                val lista: ArrayList<Computador> = ArrayList()
                with(cursor) {
                    while (moveToNext()) {
                        val id = getString(getColumnIndexOrThrow("id"))
                        val modelo = getString(getColumnIndexOrThrow("modelo"))
                        val memoriaRam = getFloat(getColumnIndexOrThrow("memoria_ram"))
                        val cpfDb = getString(getColumnIndexOrThrow("fk_cpf"))
                        val preco = getFloat(getColumnIndexOrThrow("preco"))
                        val cliente = Cliente(
                            cpf = cpfDb,
                            nome = "",
                            telefone = "",
                            email = "",
                            foto = null
                        )

                            lista.add(
                                Computador(
                                    id = id,
                                    modelo = modelo,
                                    memoriaRam = memoriaRam,
                                    fkCpf = cliente,
                                    preco = preco
                                )
                            )

                    }
                }
                return lista
            }
        } finally {
            dbRead.close()
        }
    }

    fun findById(id : String) : Computador {
        val dbRead = myDatabase.readableDatabase
        try {
            dbRead.rawQuery("SELECT * FROM computador WHERE id = ?", arrayOf(id)).use { cursor ->
                if (cursor.moveToFirst()) {
                    val modelo = cursor.getString(cursor.getColumnIndexOrThrow("modelo"))
                    val memoriaRam = cursor.getFloat(cursor.getColumnIndexOrThrow("memoria_ram"))
                    val fkCpf = cursor.getString(cursor.getColumnIndexOrThrow("fk_cpf"))
                    val preco = cursor.getFloat(cursor.getColumnIndexOrThrow("preco"))
                    val cliente = Cliente(
                        cpf = fkCpf,
                        nome = "",
                        telefone = "",
                        email = "",
                        foto = null
                    )
                    if(cliente != null){
                        return Computador(
                            id = id,
                            modelo = modelo,
                            memoriaRam = memoriaRam,
                            fkCpf = cliente,
                            preco = preco
                        )
                    }
                }
                throw IllegalStateException("Computador não encontrado para id=$id")
            }
        } finally {
            dbRead.close()
        }
    }

    fun transferirComputador(idComputador: String, novoCpf: String) {
        val db = myDatabase.writableDatabase
        try {
            val values = ContentValues().apply {
                put("fk_cpf", novoCpf)
            }
            db.update("computador", values, "id = ?", arrayOf(idComputador))
        } finally {
            db.close()
        }
    }


    fun updateById(computador : Computador, id : String){
        val dbUpdate = myDatabase.writableDatabase
        try{
            val values = ContentValues().apply {
                put("modelo", computador.modelo)
                put("memoria_ram", computador.memoriaRam)
                put("fk_cpf", computador.fkCpf.cpf)
                put("preco", computador.preco)
            }
            dbUpdate.update("computador", values, "id = ?", arrayOf(id))
        } finally {
            dbUpdate.close()
        }
    }

    fun delete(id : String){
        val dbDelete = myDatabase.writableDatabase
        try{
            dbDelete.delete("computador", "id = ?", arrayOf(id))
        } finally {
            dbDelete.close()
        }
    }
}
