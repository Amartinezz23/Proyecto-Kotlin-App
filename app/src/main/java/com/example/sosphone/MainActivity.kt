package com.example.sosphone

import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.media.MediaPlayer
import android.provider.MediaStore

private lateinit var mediaPlayer: MediaPlayer
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.audio)



    }


    // Abrir YouTube (App o Web)
    fun abrirWeb(view: android.view.View) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://"))
            startActivity(intent)
        } catch (e: Exception) {
            // Si no está instalada la app, abrir web
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
            startActivity(webIntent)
        }
    }

    // Abrir URL
    fun abrirUrl(view: android.view.View) {
        val preferences = getSharedPreferences("configApp", MODE_PRIVATE)
        //Si no hay nada ponemos que abra google
        val url = preferences.getString("url_guardada", "https://www.google.com")

        Toast.makeText(this, "Abriendo: $url", Toast.LENGTH_SHORT).show()

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }



    // Llamar a un número de teléfono
    fun abrirTelefono(view: android.view.View) {
        // Abrimos las SharedPreferences donde se guardó el número
        val nameSharedFichero = getString(R.string.name_preferen_shared_fich)
        val nameSharedPhone = getString(R.string.name_shared_phone)
        val preferences = getSharedPreferences(nameSharedFichero, MODE_PRIVATE)

        // Obtenemos el número guardado
        val phone = preferences.getString(nameSharedPhone, null)

        if (phone.isNullOrEmpty()) {
            // Si no hay número guardado, mandamos al usuario a la configuración
            Toast.makeText(this, "No hay número guardado. Configúralo primero.", Toast.LENGTH_SHORT).show()
            val intentConf = Intent(this, ConfActivity::class.java)
            startActivity(intentConf)
        } else {
            // Si sí hay número, abrimos TlfnActivity y se lo pasamos
            val intent = Intent(this, TlfnActivity::class.java)
            intent.putExtra(getString(R.string.string_phone), phone)
            startActivity(intent)
        }
    }


    // Poner alarma


    fun PonerAlarma(view: android.view.View) {
        //Ponemos que la alarma sea de por ejemplo 1 mintu
        val minutos = 2;
        val calendario = Calendar.getInstance()
        var hora = calendario.get(Calendar.HOUR_OF_DAY)
        var minuto = calendario.get(Calendar.MINUTE)

        // Sumar los minutos configurados
        minuto += minutos
        while (minuto >= 60) {
            minuto -= 60
            hora = (hora + 1) % 24
        }

        val intent_alarm = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Alarma en $minutos minutos")
            putExtra(AlarmClock.EXTRA_HOUR, hora)
            putExtra(AlarmClock.EXTRA_MINUTES, minuto)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true) // para no abrir la app del reloj (opcional)
        }

        startActivity(intent_alarm)

        Toast.makeText(this, "Alarma programada en $minutos minutos", Toast.LENGTH_SHORT).show()
    }

    fun abrirAjustes(view : android.view.View){
         val intent = Intent(this, ConfActivity::class.java)
        startActivity(intent)
    }

    fun abrirACDC(view : android.view.View){
        if (mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(0)
        }
        mediaPlayer.start()
    }

    fun abrirCamara(view: View){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(intent)
    }
}
