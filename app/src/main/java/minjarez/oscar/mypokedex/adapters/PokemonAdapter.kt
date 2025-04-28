package minjarez.oscar.mypokedex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import minjarez.oscar.mypokedex.R
import minjarez.oscar.mypokedex.domain.Pokemon

class PokemonAdapter(context: Context, pokemons: ArrayList<Pokemon>) : ArrayAdapter<Pokemon>(context, 0, pokemons) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater
            .from(context)
            .inflate(R.layout.item_pokemon, parent, false)
        val pokemon = getItem(position)
        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtNumber = view.findViewById<TextView>(R.id.txtNumber)
        val imgPokemon = view.findViewById<ImageView>(R.id.imgPokemon)
        pokemon?.let {
            txtName.text = it.name
            txtNumber.text = it.number.toString()
            Glide.with(context)
                .load(it.imageUrl)
                .into(imgPokemon)
        }
        return view
    }
}