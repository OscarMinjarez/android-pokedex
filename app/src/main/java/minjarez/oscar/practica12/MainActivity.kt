package minjarez.oscar.practica12

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import minjarez.oscar.practica12.adapters.PokemonAdapter
import minjarez.oscar.practica12.domain.Pokemon

import com.cloudinary.android.MediaManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var cloudName: String = "dpuxxet9g"

    private lateinit var btnRegisterPokemon: FloatingActionButton
    private lateinit var listView: ListView
    private lateinit var adapter: PokemonAdapter
    private val dataList = ArrayList<Pokemon>()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.initCloudinary()
        this.btnRegisterPokemon = findViewById(R.id.btnRegisterPokemon)
        this.btnRegisterPokemon.setOnClickListener {
            val intent: Intent = Intent(this, RegisterPokemonActivity::class.java)
            startActivity(intent)
        }
        this.listView = findViewById<ListView>(R.id.pokemon_lists)
        dataList.add(Pokemon("1234", "Pikachu", 12))
        dataList.add(Pokemon("4321", "Charmander", 13))
        adapter = PokemonAdapter(this, dataList)
        listView.adapter = adapter
    }

    private fun initCloudinary() {
        val config = HashMap<String, String>()
        config["cloud_name"] = this.cloudName
        MediaManager.init(this, config)
    }
}