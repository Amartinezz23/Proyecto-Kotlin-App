package com.example.sosphone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sosphone.databinding.ActivityConfBinding
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

class ConfActivity : AppCompatActivity() {

    private lateinit var confBinding: ActivityConfBinding
    private lateinit var sharedFich: SharedPreferences
    private lateinit var nameSharedPhone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //layoutInflater, es el objeto que se encarga de inflar las vistas de la interfaz.
        confBinding = ActivityConfBinding.inflate(layoutInflater)

        //activityMaiinBinding, ya es el objeto con las views infladas.
        //insertamos la IU del activity, a partir de la raiz del árbol generado de views,
        setContentView(confBinding.root)

        // Llamamos a la función que habilita el modo de borde a borde
        enableEdgeToEdge()

        //Ajustamos los margenes con binding.
        ViewCompat.setOnApplyWindowInsetsListener(confBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initPreferentShared()
        start()
    }

    private fun initPreferentShared() {
        val nameSharedFich = getString(R.string.name_preferen_shared_fich)
        this.nameSharedPhone = getString(R.string.name_shared_phone)

        //Abrimos el fichero de preferencias compartidas privada.
        this.sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        confBinding.editPhone.setText("")
        Toast.makeText(this, R.string.msg_new_phone, Toast.LENGTH_LONG).show()

    }

    private fun start(){
        //buscamos la preferencia del phone guardada. En caso de que no esté, será null
        val sharedPhone : String?  = sharedFich.getString(nameSharedPhone, null)

        sharedPhone?.let {
            startMainActivity(it)
        }

        confBinding.btnConf.setOnClickListener {
            val numberPhone = confBinding.editPhone.text.toString()
            if (numberPhone.isEmpty())
                Toast.makeText(this, R.string.msg_empty_phone, Toast.LENGTH_LONG).show()
            else
                if (!isValidPhoneNumber2(numberPhone, "ES"))
                    Toast.makeText(this, R.string.msg_not_valid_phone, Toast.LENGTH_LONG).show()
                else{
                    //registramos el teléfono, ya que es válido.
                    val edit = sharedFich.edit()
                    edit.putString(nameSharedPhone, numberPhone)
                    edit.apply()
                    startMainActivity(numberPhone)
                }
        }
    }


    private fun startMainActivity(phone: String) {
        val intent = Intent(this@ConfActivity, MainActivity::class.java)
        intent.apply {
            putExtra(getString(R.string.string_phone), phone)
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
        startActivity(intent ) //lanzamos el Activity
    }


    //clase de Android que valida un teléfono.
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)
    }

    fun isValidPhoneNumber2(phoneNumber: String, countryCode: String): Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance()
        return try {
            // Parseamos el número de teléfono en base al código de país proporcionado
            val number = phoneUtil.parse(phoneNumber, countryCode)
            // Verificamos si es un número válido para el país especificado
            phoneUtil.isValidNumber(number)
        } catch (e: NumberParseException) {
            e.printStackTrace()
            false
        }
    }

}