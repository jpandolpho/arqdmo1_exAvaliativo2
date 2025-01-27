package br.edu.ifsp.dmo1.pesquisaopiniao.data.model

class Voto(val codigo: String, val opcao: String) {
    companion object {
        //solução para geração aleatoria do código.
        //encontrada em https://www.baeldung.com/kotlin/random-alphanumeric-string
        fun generateCode(): String {
            val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            val randomCode = List(10) { charPool.random() }.joinToString("")
            return randomCode
        }
    }
}