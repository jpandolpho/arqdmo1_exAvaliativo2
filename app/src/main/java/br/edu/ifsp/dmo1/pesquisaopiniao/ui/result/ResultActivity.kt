package br.edu.ifsp.dmo1.pesquisaopiniao.ui.result

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.buttonCheck.setOnClickListener {
            val codigo = binding.textCodigo.text.toString()
            viewModel.checkCodigo(codigo)
        }

        binding.buttonVoltar.setOnClickListener {
            finish()
        }
    }

    private fun verifyBundle() {
        if (intent.extras != null) {
            setupRecyclerView()
            val count = intent.getStringExtra("contagem")
            val text = binding.voteCount.text.toString()
            binding.voteCount.text = "$text $count"
            viewModel.load()
        } else {
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
            adapter.loadData(it)
        })

        viewModel.opcao.observe(this, Observer {
            if (it.isEmpty()) {
                Toast.makeText(this, "Código inválido.", Toast.LENGTH_SHORT).show()
            } else {
                val mIntent = Intent(this, VoteActivity::class.java)
                mIntent.putExtra("opcao", it)
                startActivity(mIntent)
            }
        })
    }
}