package daniel.granados.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import medicamentosHelper.AdaptadorMedicamentos
import modelo.ClaseConexion
import modelo.DataClassMedicamentos
import modelo.DataClassPacientes
import java.util.Locale

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

        val btnSalir = findViewById<ImageView>(R.id.imgExitDetallePacientes)
        val btnEditarNombre = findViewById<ImageView>(R.id.imgEditarNombreApellidoDetalle)
        val btnEditarEdad = findViewById<ImageView>(R.id.imgEditarEdadPaciente)

        val edad = intent.getIntExtra("edad", 0)

        //Recibo los datos
        lblNombrePacienteDetalle.text = intent.getStringExtra("nombre")
        lblApellidoPacienteDetalle.text = intent.getStringExtra("apellido")
        lblEdadPacienteDetalle.text = "$edad años"
        lblEnfermedadPacienteDetalle.text = intent.getStringExtra("enfermedad")


        val idPaciente = intent.getStringExtra("id")?:""


        btnSalir.setOnClickListener {
            finish()
        }

        //Editar nombre y apellido
        fun actualizarNombrePaciente(nombre: String, apellido: String, id: String){
            //1-Creo una corrutina
            GlobalScope.launch(Dispatchers.IO){
                //1- Crear objeto de la clase conexión
                val objConexion = ClaseConexion().cadenaConexion()

                //2- Variable que contenga un prepareStatement
                val updateProducto = objConexion?.prepareStatement("update tbPacientes set nombres_paciente = ?, apellidos_paciente = ? where id_paciente = ?")!!
                updateProducto.setString(1, nombre)
                updateProducto.setString(2, apellido)
                updateProducto.setString(3, id)
                updateProducto.executeUpdate()

                val commit = objConexion.prepareStatement("commit")!!
                commit.executeUpdate()

                withContext(Dispatchers.Main) {
                    lblNombrePacienteDetalle.text = nombre
                    lblApellidoPacienteDetalle.text = apellido
                    Toast.makeText(this@activity_detalles, "Nombre actualizado con éxito.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnEditarNombre.setOnClickListener {
            //Creo la alerta
            val builder = AlertDialog.Builder(this@activity_detalles)
            builder.setTitle("Editar nombre completo:")

            //Campos de texto
            val layout = LinearLayout(this@activity_detalles)
            layout.orientation = LinearLayout.VERTICAL

            //Editar nombre:
            val editTextNombre = EditText(this@activity_detalles)
            editTextNombre.hint = "Nombre"
            val nombreActual = lblNombrePacienteDetalle.text.toString()
            editTextNombre.setText(nombreActual)

            //Editar apellido:
            val editTextApellido = EditText(this@activity_detalles)
            editTextApellido.hint = "Apellido"
            val apellidoActual = lblApellidoPacienteDetalle.text.toString()
            editTextApellido.setText(apellidoActual)

            layout.addView(editTextNombre)
            layout.addView(editTextApellido)

            builder.setView(layout)

            //Botones:
            builder.setPositiveButton("Actualizar") { dialog, which ->
                val nuevoNombre = editTextNombre.text.toString()
                val nuevoApellido = editTextApellido.text.toString()
                actualizarNombrePaciente(nuevoNombre, nuevoApellido, idPaciente)
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        //Editar edad
        fun actualizarEdadPaciente(edad: Int, id: String){
            //1-Creo una corrutina
            GlobalScope.launch(Dispatchers.IO){
                //1- Crear objeto de la clase conexión
                val objConexion = ClaseConexion().cadenaConexion()

                //2- Variable que contenga un prepareStatement
                val updateProducto = objConexion?.prepareStatement("update tbPacientes set edad_paciente = ? where id_paciente = ?")!!
                updateProducto.setInt(1, edad)
                updateProducto.setString(2, id)
                updateProducto.executeUpdate()

                val commit = objConexion.prepareStatement("commit")!!
                commit.executeUpdate()

                withContext(Dispatchers.Main) {
                    lblEdadPacienteDetalle.text = edad.toString() + " años"
                    Toast.makeText(this@activity_detalles, "Edad actualizada con éxito.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnEditarEdad.setOnClickListener {
            //Creo la alerta
            val builder = AlertDialog.Builder(this@activity_detalles)
            builder.setTitle("Editar edad del paciente:")

            //Campos de texto
            val layout = LinearLayout(this@activity_detalles)
            layout.orientation = LinearLayout.VERTICAL

            //Editar edad:
            val editTextEdad = EditText(this@activity_detalles)
            editTextEdad.hint = "Edad"
            val edadActual = edad.toString()
            editTextEdad.setText(edadActual)


            layout.addView(editTextEdad)

            builder.setView(layout)

            //Botones:
            builder.setPositiveButton("Actualizar") { dialog, which ->
                val nuevaEdad = editTextEdad.text.toString().toInt()
                actualizarEdadPaciente(nuevaEdad, idPaciente)
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        //Medicamentos RecyclerView
        val rcvMedicina = findViewById<RecyclerView>(R.id.rcvMedicamentos)

        fun obtenerMedicamentos(): List<DataClassMedicamentos> {
            //Crear un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //crepo un Statement que me ejecutara el Select
            val statement = objConexion?.prepareStatement("Select m.id_medicamento, m.nombre_medicamento, pm.hora_aplicacion from tbMedicamentos m inner join tbPacientesMedicamentos pm on m.ID_Medicamento = pm.ID_Medicamento where pm.ID_Paciente = ?")
            statement?.setString(1, idPaciente)

            val resultSet = statement?.executeQuery()
            val hourFormat = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())

            val listadoMedicamentos = mutableListOf<DataClassMedicamentos>()
            while (resultSet?.next() == true) {
                val idMedicamento = resultSet.getInt("ID_Medicamento")
                val nombre = resultSet.getString("nombre_medicamento")
                val horaAplicacion = resultSet.getTimestamp("hora_aplicacion")
                val horaFormateada = hourFormat.format(horaAplicacion)


                val medicamento = DataClassMedicamentos(idMedicamento, nombre, idPaciente, horaFormateada)
                listadoMedicamentos.add(medicamento)
            }

            return listadoMedicamentos
        }

        //Asignar adaptador
        CoroutineScope(Dispatchers.IO).launch {
            rcvMedicina.layoutManager = LinearLayoutManager(this@activity_detalles)

            val medicamento = obtenerMedicamentos()

            withContext(Dispatchers.Main){
                val miAdapter = AdaptadorMedicamentos(medicamento)
                rcvMedicina.adapter = miAdapter
            }
        }

    }
}