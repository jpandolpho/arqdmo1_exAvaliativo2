package br.edu.ifsp.dmo1.pesquisaopiniao.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.dmo1.pesquisaopiniao.data.model.User
import br.edu.ifsp.dmo1.pesquisaopiniao.data.model.Voto
import br.edu.ifsp.dmo1.pesquisaopiniao.data.repository.UserRepository
import br.edu.ifsp.dmo1.pesquisaopiniao.data.repository.VotoRepository

class MainViewModel(application: Application): AndroidViewModel(application) {
    private var userRepository: UserRepository
    private var votoRepository: VotoRepository

    private val _added = MutableLiveData<Boolean>()
    val added: LiveData<Boolean> = _added

    private val _contagem = MutableLiveData<String>()
    val contagem: LiveData<String> = _contagem

    init{
        userRepository = UserRepository(application)
        votoRepository = VotoRepository(application)
    }

    fun registerNewVote(prontuario: String, nome: String, codigo: String, opcao: String) {
        registerParticipant(prontuario,nome)
        registerVote(codigo,opcao)
        _added.value = true
    }

    fun launchResults() {
        var count:Int?
        count = userRepository.getContagem()
        if(count != null){
            _contagem.value = count.toString()
        }else{
            _contagem.value = "Vazio"
        }
    }

    private fun registerVote(codigo: String, opcao: String) {
        votoRepository.inserirVoto(Voto(codigo,opcao))
    }

    private fun registerParticipant(prontuario: String, nome: String) {
        userRepository.inserirUser(User(prontuario,nome))
    }
}
