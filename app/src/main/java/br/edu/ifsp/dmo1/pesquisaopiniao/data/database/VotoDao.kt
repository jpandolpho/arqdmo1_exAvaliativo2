package br.edu.ifsp.dmo1.pesquisaopiniao.data.database

import android.content.ContentValues
import br.edu.ifsp.dmo1.pesquisaopiniao.data.model.Voto

class VotoDao(private val dbHelper: DatabaseHelper) {

    fun insert(voto: Voto) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.DATABASE_KEYS.VOTO_COL_CODIGO, voto.codigo)
            put(DatabaseHelper.DATABASE_KEYS.VOTO_COL_OPCAO, voto.opcao)
        }
        db.insert(DatabaseHelper.DATABASE_KEYS.TB_VOTO, null, values)
    }

    //Retorna a opção que aquele código votou ou nulo, caso
    //aquele código não seja encontrado no banco de dados.
    fun getByCodigo(codigo: String): String? {
        val opcao: String?
        val db = dbHelper.readableDatabase

        val columns = arrayOf(DatabaseHelper.DATABASE_KEYS.VOTO_COL_OPCAO)

        val where = "${DatabaseHelper.DATABASE_KEYS.VOTO_COL_CODIGO} = ?"
        val whereArgs = arrayOf(codigo)

        val cursor = db.query(
            DatabaseHelper.DATABASE_KEYS.TB_VOTO,
            columns,
            where,
            whereArgs,
            null,
            null,
            null
        )

        cursor.use {
            opcao = if (cursor.moveToNext()) {
                cursor.getString(0)
            } else {
                null
            }
        }

        return opcao
    }

    //Retorna uma lista de pares de String e Int
    //Com este retorno, teremos uma lista do tipo
    //{[Ótimo, 2],[Ruim,1]}
    fun getCountOfOpcao(): List<Pair<String, Int>> {
        val db = dbHelper.readableDatabase

        val columns = arrayOf(
            DatabaseHelper.DATABASE_KEYS.VOTO_COL_OPCAO,
            "COUNT(${DatabaseHelper.DATABASE_KEYS.VOTO_COL_OPCAO}) AS CONTAGEM"
        )

        val cursor = db.query(
            DatabaseHelper.DATABASE_KEYS.TB_VOTO,
            columns,
            null,
            null,
            "${DatabaseHelper.DATABASE_KEYS.VOTO_COL_OPCAO}",
            null,
            "CONTAGEM DESC"
        )
        val dados = mutableListOf<Pair<String, Int>>()

        cursor.use {
            while (it.moveToNext()) {
                val column = it.getString(0)
                val count = it.getInt(1)
                dados.add(Pair(column, count))
            }
        }

        return dados
    }

    //gera um código aletório que representa o voto.
    fun generateCode() = Voto.generateCode()
}