package com.example.prova.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BancoDeDados (context : Context) : SQLiteOpenHelper(context,"Prova",null,2){
    override fun onCreate(db: SQLiteDatabase?) {
        val entity1 : String = "CREATE TABLE cliente" +
                " (cpf TEXT PRIMARY KEY, " +
                "nome TEXT, " +
                "email TEXT," +
                " telefone TEXT)"

        val entity2 : String = "CREATE TABLE computador" +
                " (id TEXT PRIMARY KEY, " +
                "modelo TEXT, " +
                "memoria_ram REAL," +
                " preco REAL," +
                " fk_cpf TEXT," +
                " FOREIGN KEY(fk_cpf) REFERENCES cliente (cpf) CASCADE ON UPDATE)"

        db?.execSQL(entity1)
        db?.execSQL(entity2)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS cliente")
        db?.execSQL("DROP TABLE IF EXISTS computador")
        onCreate(db)
    }
}
