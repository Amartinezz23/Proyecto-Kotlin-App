package com.example.sosphone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sosphone.R

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
        val prefs = getSharedPreferences("configApp", MODE_PRIVATE)
        val url = prefs.getString("url_configurada", "https://www.google.com")

        Toast.makeText(this, "Abriendo: $url", Toast.LENGTH_SHORT).show()

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }



    // Llamar a un número de teléfono
    fun abrirTelefono(view: android.view.View) {
        val intent = Intent(this, TlfnActivity::class.java)
        startActivity(intent)


    }

    // Poner alarma
    fun PonerAlarma(view: android.view.View) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Alarma SOS")
            putExtra(AlarmClock.EXTRA_HOUR, 8)
            putExtra(AlarmClock.EXTRA_MINUTES, 0)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No se pudo configurar la alarma", Toast.LENGTH_SHORT).show()
        }
    }

    // Abrir el confActivity
    fun abrirAjustes(view: android.view.View) {
        val intent = Intent(this, ConfActivity::class.java)
        intent.putExtra("from_settings_icon", true)
        startActivity(intent)
    }
}
