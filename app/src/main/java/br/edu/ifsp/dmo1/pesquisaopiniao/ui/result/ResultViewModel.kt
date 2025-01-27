package br.edu.ifsp.dmo1.pesquisaopiniao.ui.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.dmo1.pesquisaopiniao.data.repository.VotoRepository

class ResultViewModel(application: Application) : AndroidViewModel(application) {
    private var votoRepository: VotoRepository

    private val _results = MutableLiveData<List<Pair<String, Int>>>()
    val results: LiveData<List<Pair<String, Int>>> = _results

    private val _opcao = MutableLiveData<String>()
    val opcao: LiveData<String> = _opcao

    init {
        votoRepository = VotoRepository(application)
    }

    fun load() {
        _results.value = votoRepository.getContagemByOpcao()
    }

    fun checkCodigo(codigo: String) {
        _opcao.value = votoRepository.getVotoByCodigo(codigo) ?: ""
    }
}