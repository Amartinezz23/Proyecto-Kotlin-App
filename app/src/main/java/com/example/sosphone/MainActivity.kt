package com.example.sosphone

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
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
        //Si no hay nada ponemos que abra google
        val url = prefs.getString("url_guardada", "https://www.google.com")

        Toast.makeText(this, "Abriendo: $url", Toast.LENGTH_SHORT).show()

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }



    // Llamar a un número de teléfono
    fun abrirTelefono(view: android.view.View) {
        // Abrimos las SharedPreferences donde se guardó el número
        val nameSharedFich = getString(R.string.name_preferen_shared_fich)
        val nameSharedPhone = getString(R.string.name_shared_phone)
        val prefs = getSharedPreferences(nameSharedFich, MODE_PRIVATE)

        // Obtenemos el número guardado
        val phone = prefs.getString(nameSharedPhone, null)

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
        val prefs = getSharedPreferences("configApp", MODE_PRIVATE)
        val minutos = prefs.getInt("minutos_alarma", 1) // Valor por defecto 1 minuto

        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)

        // Sumar los minutos configurados
        minute += minutos
        while (minute >= 60) {
            minute -= 60
            hour = (hour + 1) % 24
        }

        val intent_alarm = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Alarma en $minutos minutos")
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minute)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true) // para no abrir la app del reloj (opcional)
        }

        startActivity(intent_alarm)

        Toast.makeText(this, "Alarma programada en $minutos minutos", Toast.LENGTH_SHORT).show()
    }
}
