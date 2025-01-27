package br.edu.ifsp.dmo1.pesquisaopiniao.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo1.pesquisaopiniao.R
import br.edu.ifsp.dmo1.pesquisaopiniao.databinding.ActivityMainBinding
import br.edu.ifsp.dmo1.pesquisaopiniao.ui.result.ResultActivity
import br.edu.ifsp.dmo1.pesquisaopiniao.ui.user.UserActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var userVoteResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupObservers()
        setupListeners()
        setupLauncher()
    }

    private fun setupObservers() {
        viewModel.added.observe(this, Observer {
            Toast.makeText(this,"Voto registrado com sucesso!", Toast.LENGTH_SHORT).show()
        })

        viewModel.contagem.observe(this, Observer {
            if(it.equals("Vazio")){
                Toast.makeText(this,"Registre ao menos um voto para ver os resultados.", Toast.LENGTH_LONG).show()
            }else{
                val mIntent = Intent(this, ResultActivity::class.java)
                mIntent.putExtra("contagem",it)
                startActivity(mIntent)
            }
        })
    }

    private fun setupListeners() {
        binding.buttonParticipar.setOnClickListener {
            val mIntent = Intent(this,UserActivity::class.java)
            userVoteResultLauncher.launch(mIntent)
        }
        binding.buttonChecar.setOnClickListener{
            val mIntent = Intent(this, ResultActivity::class.java)
            startActivity(mIntent)
        }
        binding.buttonFinalizar.setOnClickListener {
            viewModel.launchResults()
        }
    }

    private fun setupLauncher() {
        userVoteResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if(it.resultCode == RESULT_OK){
                    val prontuario = it.data?.getStringExtra("prontuario")?:""
                    val nome = it.data?.getStringExtra("nome")?:""
                    val codigo = it.data?.getStringExtra("codigo")?:""
                    val opcao = it.data?.getStringExtra("opcao")?:""
                    viewModel.registerNewVote(prontuario,nome,codigo,opcao)
                }
            }
        )
    }
}