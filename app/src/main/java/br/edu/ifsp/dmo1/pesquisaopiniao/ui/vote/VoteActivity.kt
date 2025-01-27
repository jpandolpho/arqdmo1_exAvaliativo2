package br.edu.ifsp.dmo1.pesquisaopiniao.ui.vote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.dmo1.pesquisaopiniao.R
import br.edu.ifsp.dmo1.pesquisaopiniao.databinding.ActivityVoteBinding

class VoteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListerners()
        verifyBundle()
    }

    private fun verifyBundle() {
        if(intent.extras != null){
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
            val checked = binding.radioGroup.checkedRadioButtonId

            if(checked!=-1){
                val selected = findViewById<RadioButton>(checked)
                val opcao = selected.text.toString()

                val mIntent = Intent()
                mIntent.putExtra("opcao",opcao)
                setResult(RESULT_OK,mIntent)
                finish()
            }else{
                Toast.makeText(this,"Escolha uma opção.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonVoltar.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}