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

//Esta view é utilizada tanto para o preenchimento de dados do usuário,
//quanto para mostrar o código de validão referente ao voto dele.
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
            //Verificamos se o prontuário digitado já está no banco
            if (prontuario.isNotEmpty()) {
                viewModel.checkExistence(prontuario)
            } else {
                //Caso o campo esteja vazio, mostramos um Toast.
                showBlankFieldsToast()
            }
        }

        binding.buttonVoltar.setOnClickListener {
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
                    //Quando retornamos pelo resultLauncher para esta activity, trazemos
                    //a opção de voto do usuário.
                    val opcao = it.data?.getStringExtra("opcao") ?: ""
                    setupCodeView(opcao)
                }
            }
        )
    }

    //Quando retornamos da VoteActivity com um resultado positivo, precisamos ajustar a View.
    //Ajustamos a visibilidade dos elementos e alteramos o título.
    private fun setupCodeView(opcao: String) {
        binding.textlayoutProntuario.visibility = View.GONE
        binding.textlayoutNome.visibility = View.GONE
        binding.buttonVotar.visibility = View.GONE

        binding.textOpcao.text = opcao
        binding.textCodigo.visibility = View.VISIBLE
        binding.copyCode.visibility = View.VISIBLE
        binding.userTitle.text = "CÓDIGO DE VERIFICAÇÃO"

        //Após ajustarmos as visibilidades, geramos o código.
        viewModel.getCodigo()
    }

    private fun setupObservers() {
        //Caso o prontuário já esteja presente no banco, lançamos um toast.
        //Caso contrário, verificamos se os campos são válidos e então lançamos
        //por resultLauncher um intenção, que nos trará a opção de voto.
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

        //Após gerarmos o código, adicionamos ele a view e então registramos o voto e o usuário.
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