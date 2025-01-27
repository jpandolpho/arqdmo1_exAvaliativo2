package br.edu.ifsp.dmo1.pesquisaopiniao.ui.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo1.pesquisaopiniao.R
import br.edu.ifsp.dmo1.pesquisaopiniao.databinding.ItemListVoteBinding

class VoteCountAdapter(private var dataset: List<Pair<String, Int>>) :
    RecyclerView.Adapter<VoteCountAdapter.ViewHolder>() {

    fun loadData(data: List<Pair<String, Int>>) {
        dataset = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_vote, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (opcao, count) = dataset[position]
        holder.binding.itemOption.setText(opcao)
        holder.binding.itemCount.setText("${count}")
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemListVoteBinding = ItemListVoteBinding.bind(view)
    }
}