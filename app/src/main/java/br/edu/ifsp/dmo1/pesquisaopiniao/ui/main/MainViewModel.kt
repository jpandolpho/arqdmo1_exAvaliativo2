package br.edu.ifsp.dmo1.pesquisaopiniao.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.dmo1.pesquisaopiniao.data.repository.UserRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var userRepository: UserRepository

    private val _contagem = MutableLiveData<String>()
    val contagem: LiveData<String> = _contagem

    init {
        userRepository = UserRepository(application)
    }

    fun launchResults() {
        val count: Int = userRepository.getContagem()
        if (count > 0) {
            _contagem.value = count.toString()
        } else {
            _contagem.value = "Vazio"
        }
    }
}
