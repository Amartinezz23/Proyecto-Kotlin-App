package com.example.sosphone

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sosphone.databinding.ActivityPpalBinding
import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding : ActivityPpalBinding
    private  var phoneSOS : String? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityPpalBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(mainBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        initCall()
    }




    private fun init(){

        mainBinding.ivChangePhone.setOnClickListener {
            val nameSharedFich = getString(R.string.name_preferen_shared_fich)
            val nameSharedPhone = getString(R.string.name_shared_phone)
            val sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
            val edit = sharedFich.edit()
            edit.remove(nameSharedPhone)
            edit.apply()
            startActivity(
                Intent(this, ConfActivity::class.java )
                .apply { addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
        }

        val stringPhone  = getString(R.string.string_phone)
        phoneSOS = intent.getStringExtra(stringPhone)
        phoneSOS?.let {
            initCall()
        }?:run { Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show() }



        /*
        Para registrar una petición de permisos que aún no se ha lanzado....

        - Con ActivityResultContracts.RequestPermission(), indicamos a Android
        que solicitamos un único permiso peligroso.
        - la lambda,es la lógica que queremos hacer, cuando el usuario haya pulsado
        aceptar/denegar el permiso solicitado.
         */
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { //lambda
            /*
            Se ejecuta esta lambda, cuando el usuario pulsa aceptar/denegar
             */
                isGranted->
                    if (isGranted) {
                        call()
                    }
                    else {
                        Toast.makeText( this, "Necesitas habilitar los permisos",
                            Toast.LENGTH_LONG).show()
                    }
        }

    }



    //inicia el proceso de llamada, junto con la petición de permisos.
    private fun initCall() {
        mainBinding.button.setOnClickListener {
            verifyPermissionsCall()
        }
    }

    private fun verifyPermissionsCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Para Android 6.0 (API 23) y superior, se solicita el permiso en tiempo de ejecución
            if (!permisionCall()) { //si no tiene los permisos, se solicitan
                /*
                llamada launch con el permiso de LLAMADA TELÉFONICA
                - Iniciamos un diálogo con el permiso que queremos solicitar.
                - El diálogo, ejecutará un método con el resultado de Aceptar/Denegar
                mediante una llamada de orden superior, definida en lambda que hemos utilizado
                en el registro de petición de permiso.
                 */
                requestPermissionLauncher.launch(Manifest.permission. CALL_PHONE)  //pedimos permisos en t. ejecución.
            }else{
                //ya se solicitaron los permisos en t. ejecución
                call()
            }
        }else
            call()  //No es necesario pedir en t. ejecución por ser < api 23

    }

    /*
    devuelve false, si no tiene los permisos.
    PackageManager.PERMISSION_GRANTED es una cte que vale 0
    Si el chequeo da un valor en el permiso igual a 0, es que tiene los permisos.
     */
    private fun permisionCall() = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED


    //método que pide los permisos en t. ejecución.


    //realiza la llamada una vez solicitado los permisos.
    private fun call() {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:"+phoneSOS!!)
        }
        startActivity(intent)
    }
}

/*
1. Registro del permiso:
Cuando llamamos a registerForActivityResult(), registramos el contrato (en este caso, ActivityResultContracts.RequestPermission()) y, al mismo tiempo, proporcionamos una función lambda. Esta lambda define lo que queremos que suceda cuando el usuario acepte o deniegue el permiso.

En este momento, todavía no estamos solicitando el permiso, simplemente estamos "preparando" la acción que ocurrirá después de que el usuario interactúe con la solicitud de permiso.

2. Lanzamiento del permiso:
Cuando llamas a launch(), es cuando realmente se solicita el permiso al usuario. Esto hará que aparezca el diálogo de solicitud de permiso en la pantalla del dispositivo (el diálogo en el que el usuario puede aceptar o denegar el permiso).

3. Respuesta del usuario:
Si el usuario acepta o deniega el permiso, esa respuesta activa la lambda que registraste previamente en registerForActivityResult().
La lambda es una función de orden superior que recibe como argumento el resultado de la acción del usuario (es decir, si el permiso fue concedido o no).
 */