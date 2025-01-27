package br.edu.ifsp.dmo1.pesquisaopiniao.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.dmo1.pesquisaopiniao.data.model.User
import br.edu.ifsp.dmo1.pesquisaopiniao.data.model.Voto
import br.edu.ifsp.dmo1.pesquisaopiniao.data.repository.UserRepository
import br.edu.ifsp.dmo1.pesquisaopiniao.data.repository.VotoRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var userRepository: UserRepository
    private var votoRepository: VotoRepository

    private val _existe = MutableLiveData<Boolean>()
    val existe: LiveData<Boolean> = _existe

    private val _codigo = MutableLiveData<String>()
    val codigo: LiveData<String> = _codigo

    private val _added = MutableLiveData<Boolean>()
    val added: LiveData<Boolean> = _added

    init {
        userRepository = UserRepository(application)
        votoRepository = VotoRepository(application)
    }

    fun checkExistence(prontuario: String) {
        _existe.value = userRepository.getUserByProntuario(prontuario)
    }

    fun getCodigo() {
        _codigo.value = votoRepository.getCodigo()
    }

    fun registerParticipation(prontuario: String, nome: String, opcao: String, codigo: String) {
        registerUser(prontuario, nome)
        registerVote(codigo, opcao)
        _added.value = true
    }

    private fun registerUser(prontuario: String, nome: String) {
        userRepository.inserirUser(User(prontuario, nome))
    }

    private fun registerVote(codigo: String, opcao: String) {
        votoRepository.inserirVoto(Voto(codigo, opcao))
    }
}