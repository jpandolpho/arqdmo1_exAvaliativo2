package br.edu.ifsp.dmo1.pesquisaopiniao.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_KEYS.DB_NAME, null, DATABASE_KEYS.DB_VERSION) {

    object DATABASE_KEYS {
        const val DB_NAME = "votes_db"
        const val DB_VERSION = 1
        const val TB_USER = "tb_user"
        const val TB_VOTO = "tb_voto"
        const val USER_COL_PRONTUARIO = "prontuario"
        const val USER_COL_NOME = "nome"
        const val VOTO_COL_CODIGO = "codigo"
        const val VOTO_COL_OPCAO = "opcao"
    }

    private companion object {
        const val CREATE_TABLE_USER_V1 =
            "CREATE TABLE ${DATABASE_KEYS.TB_USER} (${DATABASE_KEYS.USER_COL_PRONTUARIO} TEXT PRIMARY KEY, ${DATABASE_KEYS.USER_COL_NOME} TEXT)"
        const val CREATE_TABLE_VOTO_V1 =
            "CREATE TABLE ${DATABASE_KEYS.TB_VOTO} (${DATABASE_KEYS.VOTO_COL_CODIGO} TEXT PRIMARY KEY, ${DATABASE_KEYS.VOTO_COL_OPCAO} TEXT)"
        const val DROP_TABLE_USER = "DROP TABLE IF EXISTS ${DATABASE_KEYS.TB_USER}"
        const val DROP_TABLE_VOTO = "DROP TABLE IF EXISTS ${DATABASE_KEYS.TB_VOTO}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_USER_V1)
        db.execSQL(CREATE_TABLE_VOTO_V1)
    }

    //onUpgrade genérico, pois não será executado.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE_USER)
        db.execSQL(DROP_TABLE_VOTO)
        onCreate(db)
    }
}