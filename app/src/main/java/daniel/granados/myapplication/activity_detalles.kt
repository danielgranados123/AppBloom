package daniel.granados.myapplication

import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_detalles : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Oculto el encabezado
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Llamo los elementos de mi interfaz
        val lblNombrePacienteDetalle = findViewById<TextView>(R.id.txtNombrePacienteDetalle)
        val lblApellidoPacienteDetalle = findViewById<TextView>(R.id.txtApellidoPacienteDetalle)
        val lblEdadPacienteDetalle = findViewById<TextView>(R.id.txtEdadPacienteDetalle)
        val lblEnfermedadPacienteDetalle = findViewById<TextView>(R.id.txtEnfermedadPacienteDetalle)



        //Recibo los datos
        lblNombrePacienteDetalle.text = intent.getStringExtra("nombre")
        lblApellidoPacienteDetalle.text = intent.getStringExtra("apellido")
        lblEdadPacienteDetalle.text = intent.getStringExtra("edad")
        lblEnfermedadPacienteDetalle.text = intent.getStringExtra("enfermedad")



    }
}