package br.edu.ifsp.dmo1.pesquisaopiniao.ui.user

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo1.pesquisaopiniao.databinding.ActivityUserBinding
import br.edu.ifsp.dmo1.pesquisaopiniao.ui.vote.VoteActivity

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var voteResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setupObservers()
        setupListeners()
        setupLauncher()
    }

    private fun setupListeners() {
        binding.buttonVotar.setOnClickListener {
            val prontuario = binding.textProntuario.text.toString()
            if (prontuario.isNotEmpty()) {
                viewModel.checkExistence(prontuario)
            } else {
                showBlankFieldsToast()
            }
        }

        binding.buttonVoltar.setOnClickListener {
            val prontuario = binding.textProntuario.text.toString()
            val nome = binding.textNome.text.toString()
            val codigo = binding.textCodigo.text
            val opcao = binding.textOpcao.text
            if (prontuario.isNotEmpty() && nome.isNotEmpty() && codigo.isNotEmpty() && opcao.isNotEmpty()) {
                val mIntent = Intent()
                mIntent.putExtra("prontuario", prontuario)
                mIntent.putExtra("nome", nome)
                mIntent.putExtra("codigo", codigo)
                mIntent.putExtra("opcao", opcao)
                setResult(RESULT_OK, mIntent)
            } else {
                setResult(RESULT_CANCELED)
            }
            finish()
        }

        //Para fins de avaliação, o aluno Gabriel Ventura me mostrou o aplicativo dele.
        //Vi ele usando essa funcionalidade de copiar o código ao apertar um botão e achei uma ótima ideia.
        //Resolvi, então, implementar esta funcionalidade.
        binding.copyCode.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("codigo", binding.textCodigo.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Código copiado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showBlankFieldsToast() {
        Toast.makeText(this, "Insira valores nos campos.", Toast.LENGTH_SHORT).show()
    }

    private fun setupLauncher() {
        voteResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if (it.resultCode == RESULT_OK) {
                    val opcao = it.data?.getStringExtra("opcao") ?: ""
                    setupCodeView(opcao)
                }
            }
        )
    }

    private fun setupCodeView(opcao: String) {
        binding.textlayoutProntuario.visibility = View.GONE
        binding.textlayoutNome.visibility = View.GONE
        binding.buttonVotar.visibility = View.GONE

        binding.textOpcao.text = opcao
        binding.textCodigo.visibility = View.VISIBLE
        binding.copyCode.visibility = View.VISIBLE
        binding.userTitle.text = "CÓDIGO DE VERIFICAÇÃO"

        viewModel.getCodigo()
    }

    private fun setupObservers() {
        viewModel.existe.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "A pessoa com este prontuario já votou.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val nome = binding.textNome.text.toString()
                if (nome.isNotEmpty()) {
                    val mIntent = Intent(this, VoteActivity::class.java)
                    voteResultLauncher.launch(mIntent)
                } else {
                    showBlankFieldsToast()
                }
            }
        })

        viewModel.codigo.observe(this, Observer {
            binding.textCodigo.text = it
            val prontuario = binding.textProntuario.text.toString()
            val nome = binding.textNome.text.toString()
            val opcao = binding.textOpcao.text.toString()
            viewModel.registerParticipation(prontuario, nome, opcao, it)
        })

        viewModel.added.observe(this, Observer {
            Toast.makeText(this, "Voto registrado com sucesso!", Toast.LENGTH_SHORT).show()
        })
    }
}