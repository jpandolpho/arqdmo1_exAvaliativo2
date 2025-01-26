package br.edu.ifsp.dmo1.pesquisaopiniao.data.database

import android.content.ContentValues
import br.edu.ifsp.dmo1.pesquisaopiniao.data.model.Voto

class VotoDao(private val dbHelper: DatabaseHelper) {

    fun insert(voto: Voto){
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.DATABASE_KEYS.VOTO_COL_CODIGO, voto.codigo)
            put(DatabaseHelper.DATABASE_KEYS.VOTO_COL_OPCAO, voto.opcao)
        }
        db.insert(DatabaseHelper.DATABASE_KEYS.TB_VOTO, null, values)
    }

    fun getByCodigo(codigo: String) : String?{
        val opcao : String?
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

        cursor.use{
            opcao = if(cursor.moveToNext()){
                cursor.getString(0)
            }else{
                null
            }
        }

        return opcao
    }

    fun getCountOfOpcao(opcao: String) : Int{
        val count : Int
        val db = dbHelper.readableDatabase

        val where = "${DatabaseHelper.DATABASE_KEYS.VOTO_COL_OPCAO} = ?"
        val whereArgs = arrayOf(opcao)

        val cursor = db.query(
            DatabaseHelper.DATABASE_KEYS.TB_VOTO,
            arrayOf("COUNT(*)"),
            where,
            whereArgs,
            null,
            null,
            null
        )

        cursor.use{
            cursor.moveToNext()
            count = cursor.getInt(0)
        }

        return count
    }
}