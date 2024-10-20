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
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding : ActivityPpalBinding
    private  var phoneSOS : String? = null
    //Acepta como lanzador un string que representa el permiso a solicitar.
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var permisionPhone = false



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
        initEventCall()

    }

    /*
    Cada vez que vuelve el Activity a la cabeza de la pila,
    debemos de volver a seleccionar el phone pasado mediante el intent
    desde el Activity 1.
     */
    override fun onResume() {
        super.onResume()
        permisionPhone = isPermissionCall()
        val stringPhone  = getString(R.string.string_phone)
        phoneSOS = intent.getStringExtra(stringPhone)
        mainBinding.txtPhone.setText(phoneSOS)


    }



    private fun init(){
        registerLauncher()
        if (!isPermissionCall()) //verificamos permisos en de llamada.
            requestPermissionLauncher.launch(Manifest.permission. CALL_PHONE)

        mainBinding.ivChangePhone.setOnClickListener {
            val nameSharedFich = getString(R.string.name_preferen_shared_fich)
            val nameSharedPhone = getString(R.string.name_shared_phone)
            val sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
            val edit = sharedFich.edit()
            edit.remove(nameSharedPhone)
            edit.apply()
            val intent = Intent(this, ConfActivity::class.java )
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    putExtra("back", true)//volvemos desde El ACtivity2
                }
            startActivity(intent)
        }

    }

    private fun registerLauncher(){
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
                permisionPhone = true
            }
            else {
                Toast.makeText( this, "Necesitas habilitar los permisos",
                    Toast.LENGTH_LONG).show()
                goToConfiguracionApp()  //abrimos la configuración de la aplicación.
            }
        }
    }


    //inicia el proceso de llamada, junto con la petición de permisos.
    private fun initEventCall() {
        mainBinding.button.setOnClickListener {
            permisionPhone=isPermissionCall()
            if (permisionPhone)
                call()
            else
                requestPermissionLauncher.launch(Manifest.permission. CALL_PHONE)
        }
    }
    private fun isPermissionCall():Boolean{
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true  //no hace falta pedir permisos en t. real al usuario
        else
            return isPermissionToUser() //true es que los tiene. False es que no.
    }

    private fun isPermissionToUser() = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

    //realiza la llamada una vez solicitado los permisos.
    private fun call() {
        val intent = Intent(Intent.ACTION_CALL).apply {  //creamos la intención
            //Indicamos la Uri que es la forma de indicarle a Android que es un teléfono.
            data = Uri.parse("tel:"+phoneSOS!!)
        }
        startActivity(intent)
    }


    private fun goToConfiguracionApp(){
        //Settings.ACTION_APPLICATION_DETAILS_SETTINGS  --> Intención de abrir la configuración de la App
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            //La Uri, identifica un tipo de recurso (fichero, Url, aplicación, contenidos)
            //con "package" --> queremos trabajar con un paquete, es decir una aplicación única.
            //con "packageName" --> es el nombre de nuestro paquete com.example.sosphone
            //con fragment = null.
            data = Uri.fromParts("package", packageName, null)
        }

        startActivity(intent)
    }

    /*
    De manera análoga en ConfActivity, es importantísimo actualizar el intent mandado
    con el nuevo teléfono desde ConfActivity, porque por sí sólo, el intent no es actualizado
    en la instancia MainActivity creado desde ConfActivity. Es importantísimo tener esto
    en cuenta. Todo esto es porque hemos utilizado el flag de Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
     */
    //Main
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // Actualiza el Intent con los nuevos extras
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