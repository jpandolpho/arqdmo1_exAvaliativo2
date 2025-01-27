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