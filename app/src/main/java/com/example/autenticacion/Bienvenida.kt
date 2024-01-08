package com.example.autenticacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.autenticacion.databinding.ActivityBienvenidaBinding
import com.example.autenticacion.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Bienvenida : AppCompatActivity() {
    lateinit var binding: ActivityBienvenidaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBienvenidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()
        binding.bGuardarCoche.setOnClickListener {
            if (binding.editTextMatricula.text.isNotEmpty() &&
                binding.editTextMarca.text.isNotEmpty() &&
                binding.editTextMarca.text.isNotEmpty() &&
                binding.editTextColor.text.isNotEmpty()
            ) {
                db.collection("coches").add(
                    mapOf(
                        "color" to binding.editTextColor.text.toString(),
                        "marca" to binding.editTextMarca.text.toString(),
                        "matricula" to binding.editTextMatricula.text.toString(),
                        "modelo" to binding.editTextModelo.text.toString()

                    )
                )
                    .addOnSuccessListener {documento ->
                        Toast.makeText(this, "Nuevo coche añadido con el id: ${documento.id}", Toast.LENGTH_LONG).show()

                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "Error en la inserción del nuevo registro", Toast.LENGTH_LONG).show()

                    }

            } else {
                Toast.makeText(this, "Algún campo está vacío", Toast.LENGTH_LONG).show()
            }

            binding.bCerrarSesion.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                // Volvemos a nuestra MainActivity:
                startActivity(Intent(this, MainActivity::class.java))
            }

            binding.bEditarCoche.setOnClickListener{
                db.collection("coches")
                    .whereEqualTo("matricula", binding.editTextMatricula.text.toString())
                    .get().addOnSuccessListener {
                        it.forEach  {
                            binding.editTextMarca.setText(it.get("marca") as String?)
                            binding.editTextModelo.setText(it.get("modelo") as String?)
                            binding.editTextColor.setText(it.get("color") as String?)
                        }
                    }
            }

            binding.bEliminarCoche.setOnClickListener{
                db.collection("coches")
                    .get()
                    .addOnSuccessListener { 
                        it.forEach{
                            it.reference.delete()
                        }
                    }
            }
        }
    }
}