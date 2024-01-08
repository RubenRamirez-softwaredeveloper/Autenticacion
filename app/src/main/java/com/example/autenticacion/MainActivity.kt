package com.example.autenticacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.autenticacion.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bLogin.setOnClickListener{
            login()
        }

        binding.bRegistrar.setOnClickListener{
            registro()
        }
    }

    private fun registro() {
        startActivity(Intent(this, RegistroActivity::class.java))
    }

    private fun login() {
        //Comprobamos que lso campos de correo y password no estén vacíos:
        if(binding.correo.text.isNotEmpty() && binding.contrasenia.text.isNotEmpty()){
            //Iniciamos sesión con el método signIn y enviamos a Firebase el correo y password introducidos:
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.correo.text.toString(),
                binding.contrasenia.text.toString()
            )
                .addOnCompleteListener{ // En caso de que el usuario y password existan en Firebase:
                    if(it.isSuccessful){
                        val intent = Intent(this, Bienvenida::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, "Correo o contraseña incorrecto", Toast.LENGTH_LONG).show()
                    }
                }
        }
        else{
            Toast.makeText(this, "Algún campo está vacio", Toast.LENGTH_LONG).show()
        }
    }
}