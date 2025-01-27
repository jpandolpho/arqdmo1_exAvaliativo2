package br.edu.ifsp.dmo1.pesquisaopiniao.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo1.pesquisaopiniao.databinding.ActivityMainBinding
import br.edu.ifsp.dmo1.pesquisaopiniao.ui.result.ResultActivity
import br.edu.ifsp.dmo1.pesquisaopiniao.ui.user.UserActivity

//Não foi utilizado nenhum resultLauncher na MainActivity.
/*Inicialmente, era utilizado um resultLauncher para a UserActivity,
pois os votos(tanto usuário, como o voto em si) eram salvos pela MainActivity, necessitando assim
que ela recebesse resultados.
Porém, foi identificado que, ao votar e chegar na tela onde o código de validação do voto era exibido,
caso o usuário utilizasse o botão de retornar do sistema operacional, as informações não eram salvas.
Assim, o app foi ajustado para que o salvamento seja feito quando o código de validação é exibido na tela.*/
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.contagem.observe(this, Observer {
            //Caso it venha como "Vazio", quer dizer que não foram encontrados votos no banco.
            if (it.equals("Vazio")) {
                Toast.makeText(
                    this,
                    "Registre ao menos um voto para ver os resultados.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val mIntent = Intent(this, ResultActivity::class.java)
                mIntent.putExtra("contagem", it)
                startActivity(mIntent)
            }
        })
    }

    private fun setupListeners() {
        binding.buttonParticipar.setOnClickListener {
            val mIntent = Intent(this, UserActivity::class.java)
            startActivity(mIntent)
        }
        binding.buttonChecar.setOnClickListener {
            val mIntent = Intent(this, ResultActivity::class.java)
            startActivity(mIntent)
        }
        binding.buttonFinalizar.setOnClickListener {
            viewModel.launchResults()
        }
    }
}