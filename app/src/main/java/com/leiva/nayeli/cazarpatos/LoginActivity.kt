package com.leiva.nayeli.cazarpatos

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    lateinit var manejadorArchivo: FileHandler
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser: Button
    lateinit var checkBoxRecordarme: CheckBox
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        LeerDatosDePreferencias();
        manejadorArchivo = SharedPreferencesManager(this)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser) //Eventos clic
        checkBoxRecordarme = findViewById(R.id.checkBoxRecordarme)
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val clave =
                editTextPassword.text.toString() //Validaciones de datos requeridos y formatos
            if (!validateRequiredData())
                return@setOnClickListener
            //Guardar datos en preferencias.
            GuardarDatosEnPreferencias()
            //Si pasa validación de datos requeridos, ir a pantalla principal
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_LOGIN, email)
            startActivity(intent)
            finish ()
        }
        buttonNewUser . setOnClickListener {

        }
        mediaPlayer = MediaPlayer . create (this, R.raw.title_screen)
        mediaPlayer.start()
    }

    private fun validateRequiredData(): Boolean {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.error_email_required))
            editTextEmail . requestFocus ()
            return false
        }
        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.error_password_required))
            editTextPassword . requestFocus ()
            return false
        }
        if (password.length < 3) {
            editTextPassword.setError(getString(R.string.error_password_min_length))
            editTextPassword . requestFocus ()
            return false
        }
        return true
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    private fun LeerDatosDePreferencias(){
        var datoLeido : Pair<String, String>
        manejadorArchivo = SharedPreferencesManager(this)
        datoLeido = manejadorArchivo.ReadInformation()
        Log.d("TAG", "SharedPreferencesManager " + datoLeido.toList().toString())

        manejadorArchivo = EncriptedSharedPreferencesManager(this)
        datoLeido = manejadorArchivo.ReadInformation()
        Log.d("TAG", "EncriptedSharedPreferencesManager " + datoLeido.toList().toString())

        manejadorArchivo = FileInternalManager(this)
        datoLeido = manejadorArchivo.ReadInformation()
        Log.d("TAG", "FileInternalManager " + datoLeido.toList().toString())

        manejadorArchivo = FileExternalManager(this)
        datoLeido = manejadorArchivo.ReadInformation()
        Log.d("TAG", "FileExternalManager " + datoLeido.toList().toString())

    }
    private fun GuardarDatosEnPreferencias(){
        val email = editTextEmail.text.toString()
        val clave = editTextPassword.text.toString()
        val listadoAGrabar:Pair<String,String>
        if(checkBoxRecordarme.isChecked){
            listadoAGrabar = email to clave
        }
        else{
            listadoAGrabar ="" to ""
        }
        manejadorArchivo.SaveInformation(listadoAGrabar)
        //Guarda la información encriptada
        manejadorArchivo = EncriptedSharedPreferencesManager(this)
        manejadorArchivo.SaveInformation(listadoAGrabar)

        //Guarda la información en un archivo interno
        manejadorArchivo = FileInternalManager(this)
        manejadorArchivo.SaveInformation(listadoAGrabar)

        //Guarda la información en un archivo externo
        manejadorArchivo = FileExternalManager(this)
        manejadorArchivo.SaveInformation(listadoAGrabar)


    }



}