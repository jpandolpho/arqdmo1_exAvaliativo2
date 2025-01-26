package br.edu.ifsp.dmo1.pesquisaopiniao.data.repository

import android.content.Context
import br.edu.ifsp.dmo1.pesquisaopiniao.data.database.DatabaseHelper
import br.edu.ifsp.dmo1.pesquisaopiniao.data.database.UserDao
import br.edu.ifsp.dmo1.pesquisaopiniao.data.model.User

class UserRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)
    private val dao = UserDao(dbHelper)

    fun inserirUser(user: User) = dao.insert(user)

    fun getUserByProntuario(prontuario:String) = dao.getByProntuario(prontuario)

    fun getContagem() = dao.getCount()
}