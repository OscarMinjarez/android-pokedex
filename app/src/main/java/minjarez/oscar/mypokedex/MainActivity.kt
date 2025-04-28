package minjarez.oscar.mypokedex

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import minjarez.oscar.mypokedex.adapters.PokemonAdapter
import minjarez.oscar.mypokedex.domain.Pokemon

class MainActivity : AppCompatActivity() {

    private var cloudName: String = "dpuxxet9g"

    private lateinit var btnRegisterPokemon: FloatingActionButton
    private lateinit var listView: ListView
    private lateinit var adapter: PokemonAdapter
    private val dataList = ArrayList<Pokemon>()

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.initCloudinary()
        this.db = FirebaseFirestore.getInstance()
        this.btnRegisterPokemon = findViewById(R.id.btnRegisterPokemon)
        this.btnRegisterPokemon.setOnClickListener {
            val intent = Intent(this, RegisterPokemonActivity::class.java)
            startActivityForResult(intent, 200)
        }
        this.listView = findViewById(R.id.pokemon_lists)
        this.getPokemons()
        adapter = PokemonAdapter(this, dataList)
        listView.adapter = adapter
    }

    private fun initCloudinary() {
        val config = HashMap<String, String>()
        config["cloud_name"] = this.cloudName
        MediaManager.init(this, config)
    }

    private fun getPokemons() {
        this.db.collection("pokemons")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    this.dataList.add(Pokemon(
                            document.id,
                            document.data?.get("name") as String,
                            document.data?.get("number") as Long,
                            document.data?.get("imageUrl") as String
                        )
                    )
                }
                this.adapter.notifyDataSetChanged()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK) {
            this.dataList.clear()
            this.getPokemons()
        }
    }
}