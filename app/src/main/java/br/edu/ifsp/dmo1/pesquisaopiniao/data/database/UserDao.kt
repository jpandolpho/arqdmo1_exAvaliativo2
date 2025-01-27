package br.edu.ifsp.dmo1.pesquisaopiniao.data.database

import android.content.ContentValues
import br.edu.ifsp.dmo1.pesquisaopiniao.data.model.User

class UserDao(private val dbHelper: DatabaseHelper) {

    fun insert(user: User) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.DATABASE_KEYS.USER_COL_PRONTUARIO, user.prontuario)
            put(DatabaseHelper.DATABASE_KEYS.USER_COL_NOME, user.nome)
        }
        db.insert(DatabaseHelper.DATABASE_KEYS.TB_USER, null, values)
    }

    //Contagem de quantos usuários votaram e, por conseguencia
    //quantos votos a pesquisa teve.
    fun getCount(): Int {
        val count: Int
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            DatabaseHelper.DATABASE_KEYS.TB_USER,
            arrayOf("COUNT(*)"),
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            cursor.moveToNext()
            count = cursor.getInt(0)
        }
        return count
    }

    //Retorna um Boolean ao invés de um User
    //Queremos saber apenas se o usuário já votou,
    //não importando seu prontuário ou nome.
    fun getByProntuario(prontuario: String): Boolean {
        val existe: Boolean
        val db = dbHelper.readableDatabase

        val columns = arrayOf(DatabaseHelper.DATABASE_KEYS.USER_COL_PRONTUARIO)

        val where = "${DatabaseHelper.DATABASE_KEYS.USER_COL_PRONTUARIO} = ?"
        val whereArgs = arrayOf(prontuario)

        val cursor = db.query(
            DatabaseHelper.DATABASE_KEYS.TB_USER,
            columns,
            where,
            whereArgs,
            null,
            null,
            null
        )

        cursor.use {
            existe = cursor.moveToNext()
        }

        return existe
    }
}