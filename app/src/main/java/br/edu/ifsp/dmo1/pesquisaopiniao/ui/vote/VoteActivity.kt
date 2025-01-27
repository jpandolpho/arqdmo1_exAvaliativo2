package br.edu.ifsp.dmo1.pesquisaopiniao.ui.vote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo1.pesquisaopiniao.databinding.ActivityVoteBinding

//Esta activity é utilizada tanto para a realização da Votação
//quanto para exibir qual a opção de voto de um código.
class VoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListerners()
        verifyBundle()
    }

    //Caso o bundle possua elementos, quer dizer que queremos mostrar
    //qual a opção de voto de código de validação.
    private fun verifyBundle() {
        if (intent.extras != null) {
            //Ajustando visibilidades e valores dos elementos.
            binding.radioGroup.visibility = View.GONE
            binding.buttonVotar.visibility = View.GONE

            binding.textVoto.visibility = View.VISIBLE
            val opcao = intent.getStringExtra("opcao")
            binding.textVoto.text = opcao
            binding.voteTitle.text = "SEU VOTO FOI:"
        }
    }

    private fun setupListerners() {
        binding.buttonVotar.setOnClickListener {
            //Descobrindo qual Radio está selecionado
            val checked = binding.radioGroup.checkedRadioButtonId

            //Caso haja algum Radio selecionado, precisamos descobrir qual elemento ele é
            if (checked != -1) {
                //Descobrindo qual elemento está selecionado
                val selected = findViewById<RadioButton>(checked)
                //Pegando o valor deste elemento
                val opcao = selected.text.toString()

                val mIntent = Intent()
                //Adicionando o valor do elemento na intent, para ser devolvida pelo
                //resultLauncher;
                mIntent.putExtra("opcao", opcao)
                setResult(RESULT_OK, mIntent)
                finish()
            } else {
                //Caso nenhuma opção esteja selecionada, mostramos um Toast.
                Toast.makeText(this, "Escolha uma opção.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonVoltar.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}