package minjarez.oscar.mypokedex

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.firestore.FirebaseFirestore
import minjarez.oscar.mypokedex.databinding.ActivityRegisterPokemonBinding
import minjarez.oscar.mypokedex.domain.Pokemon

class RegisterPokemonActivity : AppCompatActivity() {

    private lateinit var btnUploadImage: Button
    private lateinit var btnRegisterPokemon: Button

    private lateinit var txtPokemonName: TextView
    private lateinit var txtPokemonNumber: TextView

    private val uploadPreset: String = "pokedex_upload_preset"

    private lateinit var imageUri: Uri
    private var imagePublicUrl: String = ""

    private lateinit var binding: ActivityRegisterPokemonBinding

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_pokemon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.db = FirebaseFirestore.getInstance()
        this.binding = ActivityRegisterPokemonBinding.inflate(layoutInflater)
        this.btnUploadImage = findViewById(R.id.btn_upload_image)
        this.btnRegisterPokemon = findViewById(R.id.btn_register_pokemon)
        this.txtPokemonName = findViewById(R.id.txt_add_pokemon_name)
        this.txtPokemonNumber = findViewById(R.id.txt_add_pokemon_number)
        this.btnUploadImage.setOnClickListener {
            this.openGallery()
        }
        this.btnRegisterPokemon.setOnClickListener {
            this.savePokemon()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            this.imageUri = data.data as Uri
            this.changeImage(imageUri)
        }
    }

    private fun changeImage(uri: Uri) {
        val thumbnail: ImageView = findViewById(R.id.imageView)
        try {
            thumbnail.setImageURI(uri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun savePokemon(): String {
        val requestId: String = MediaManager.get()
            .upload(this.imageUri)
            .unsigned(this.uploadPreset)
            .callback(object : UploadCallback {
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    Log.d("Cloudinary Quickstart", "Upload success");
                    imagePublicUrl = resultData["secure_url"] as String
                    db.collection("pokemons")
                        .add(Pokemon(
                            name = txtPokemonName.text.toString(),
                            number = txtPokemonNumber.text.toString().toLong(),
                            imageUrl = imagePublicUrl
                        ))
                        .addOnSuccessListener { reference ->
                            Log.d("Pokemon saved", reference.toString())
                            setResult(RESULT_OK)
                            finish()
                        }
                }

                override fun onStart(requestId: String?) {
                    Log.d("Cloudinary Quickstart", "Cloudinary initialized :)")
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    Log.d("Cloudinary Quickstart", "Upload progress");
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    println("Error uploading: ${error.description}")
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {

                }
            })
            .dispatch()
        return requestId
    }
}