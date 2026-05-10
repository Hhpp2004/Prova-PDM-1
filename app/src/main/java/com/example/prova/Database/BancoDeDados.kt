package com.example.prova.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BancoDeDados (context : Context) : SQLiteOpenHelper(context,"Prova",null,3){
    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val entity1 : String = "CREATE TABLE cliente" +
                " (cpf TEXT PRIMARY KEY, " +
                "nome TEXT, " +
                "email TEXT," +
                "foto TEXT," +
                " telefone TEXT)"

        val entity2 : String = "CREATE TABLE computador" +
                " (id TEXT PRIMARY KEY, " +
                "modelo TEXT, " +
                "memoria_ram REAL," +
                " preco REAL," +
                " fk_cpf TEXT," +
                " FOREIGN KEY(fk_cpf) REFERENCES cliente (cpf) ON UPDATE CASCADE)"

        db?.execSQL(entity1)
        db?.execSQL(entity2)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS computador")
        db?.execSQL("DROP TABLE IF EXISTS cliente")
        onCreate(db)
    }
}
