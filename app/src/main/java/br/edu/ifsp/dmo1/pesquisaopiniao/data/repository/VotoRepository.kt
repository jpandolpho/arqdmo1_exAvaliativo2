package br.edu.ifsp.dmo1.pesquisaopiniao.data.repository

import android.content.Context
import br.edu.ifsp.dmo1.pesquisaopiniao.data.database.DatabaseHelper
import br.edu.ifsp.dmo1.pesquisaopiniao.data.database.VotoDao
import br.edu.ifsp.dmo1.pesquisaopiniao.data.model.Voto

class VotoRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)
    private val dao = VotoDao(dbHelper)

    fun inserirVoto(voto: Voto) = dao.insert(voto)

    fun getVotoByCodigo(codigo: String) = dao.getByCodigo(codigo)

    fun getContagemByOpcao() = dao.getCountOfOpcao()
}