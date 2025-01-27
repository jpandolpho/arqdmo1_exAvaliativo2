package br.edu.ifsp.dmo1.pesquisaopiniao.ui.result

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.dmo1.pesquisaopiniao.R
import br.edu.ifsp.dmo1.pesquisaopiniao.databinding.ActivityResultBinding
import br.edu.ifsp.dmo1.pesquisaopiniao.ui.vote.VoteActivity

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel
    private lateinit var adapter: VoteCountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ResultViewModel::class.java)

        setupListeners()
        setupObservers()
        verifyBundle()
    }

    private fun setupListeners() {
        //Fazendo a verificação se um código inserido existe no sistema.
        binding.buttonCheck.setOnClickListener {
            val codigo = binding.textCodigo.text.toString()
            viewModel.checkCodigo(codigo)
        }

        binding.buttonVoltar.setOnClickListener {
            finish()
        }
    }

    //Esta view é reutilizada para tanto mostrar resultados gerais, quanto para fazer a
    // verificação do código de validação.
    private fun verifyBundle() {
        //Caso o bundle chegue com dados, queremos exibir a lista de resultados.
        if (intent.extras != null) {
            setupRecyclerView()
            //Desempacotando a quantidade de votos(já em String)
            val count = intent.getStringExtra("contagem")
            //Exibindo contagem de votos
            binding.voteCount.text = "${R.string.vote_count} $count"
            //Carregando lista com os votos existentes no sistema.
            viewModel.load()
        } else {
            //Caso o bundle venha vazio, significa que queremos fazer a verificação
            //de um código de validação.

            //Mudamos título da página e alteramos a visibilidade de elementos.
            binding.resultsTitle.text = "INSIRA SEU CÓDIGO"
            binding.voteCount.visibility = View.GONE
            binding.listVotes.visibility = View.GONE

            binding.textlayoutCodigo.visibility = View.VISIBLE
            binding.buttonCheck.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        adapter = VoteCountAdapter(mutableListOf())
        binding.listVotes.adapter = adapter
        binding.listVotes.layoutManager = LinearLayoutManager(this)
    }

    private fun setupObservers() {
        viewModel.results.observe(this, Observer {
            //Mandando para o adapter a lista de dados vindo do banco.
            adapter.loadData(it)
        })

        viewModel.opcao.observe(this, Observer {
            //Caso o usuário deixe o campo em branco ou coloque um código inválido,
            //mostramos um Toast.
            if (it.isEmpty()) {
                Toast.makeText(this, "Código inválido.", Toast.LENGTH_SHORT).show()
            } else {
                //Senão, colocamos na intent a opção referente ao código digitado e lançamos a atividade.
                val mIntent = Intent(this, VoteActivity::class.java)
                mIntent.putExtra("opcao", it)
                startActivity(mIntent)
            }
        })
    }
}