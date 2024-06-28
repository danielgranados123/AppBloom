package daniel.granados.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.DataClassCamas
import modelo.DataClassHabitaciones
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_agregar_paciente_prueba.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_agregar_paciente_prueba : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_agregar_paciente, container, false)

        //1-Mandar a llamar a los elementos
        val spEnfermedades = root.findViewById<Spinner>(R.id.txtEnfermedadPaciente)
        val txtNombrePaciente = root.findViewById<EditText>(R.id.txtNombrePaciente)
        val txtApellidoPaciente = root.findViewById<EditText>(R.id.txtApellidoPaciente)
        val txtEdad = root.findViewById<EditText>(R.id.txtEdadPaciente)
        val spMedicamentos = root.findViewById<Spinner>(R.id.txtMedicamentosPaciente)
        val spHabitaciones = root.findViewById<Spinner>(R.id.txtHabitacionPaciente)
        val spCamas = root.findViewById<Spinner>(R.id.txtCamaPaciente)
        val btnGuardarPaciente = root.findViewById<Button>(R.id.btnGuardarPaciente)
        val txtControlPaciente = root.findViewById<EditText>(R.id.txtControlPaciente)


        //Funcion para obtener las habitaciones
        fun obtenerHabitaciones(): List<DataClassHabitaciones>{
            //Crear un objeto de la clase conexion
            val objConexion= ClaseConexion().cadenaConexion()

            //crepo un Statement que me ejecutara el Select
            val Statement = objConexion?.createStatement()

            val resulset = Statement?.executeQuery("Select * from tbHabitaciones")!!

            val listaHabitaciones= mutableListOf<DataClassHabitaciones>()

            while (resulset.next()){
                val id = resulset.getInt("ID_Habitacion")
                val nombre = resulset.getString("nombre_habitacion")
                val habitacionCompleta = DataClassHabitaciones(id, nombre)

                listaHabitaciones.add(habitacionCompleta)
            }
            return listaHabitaciones
        }

        //Programar Spinner para que muestre datos del select

                CoroutineScope(Dispatchers.IO).launch {
                    //Obtengo los datos
                    val listaHabitaciones = obtenerHabitaciones()
                    val nombreHabitaciones = listaHabitaciones.map { it.nombre_habitacion }

                    withContext(Dispatchers.Main){
                        //2-Crear y modificar el adaptador
                        val miAdaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombreHabitaciones)

                        spHabitaciones.adapter = miAdaptador
                    }
                }

        //Funcion para obtener las camas (dependiendo la habitacion seleccionada)
        fun obtenerCamas(): List<DataClassCamas>{
            //Crear un objeto de la clase conexion
            val objConexion= ClaseConexion().cadenaConexion()

            //crepo un Statement que me ejecutara el Select
            val statement = objConexion?.createStatement()

            val resulSet = statement?.executeQuery("select nombre_cama\n" +
                    "from tbHabitacionesCamas hc \n" +
                    "inner join tbCamas c on hc.id_Cama = c.ID_Cama\n" +
                    "where ID_Habitacion = ?")!!

            val habitacionCama = resulSet.setString()

            val listaCamas= mutableListOf<DataClassCamas>()

            while (resulSet.next()){
                val id = resulSet.getInt("ID_Cama")
                val nombre = resulSet.getString("nombre_cama")
                val camaCompleta = DataClassCamas(id, nombre)

                listaCamas.add(camaCompleta)
            }
            return listaCamas
        }

        //Programar Spinner para que muestre datos del select

        CoroutineScope(Dispatchers.IO).launch {
            //Obtengo los datos
            val listaCamas = obtenerCamas()
            val nombreCamas = listaCamas.map { it.nombre_cama }

            withContext(Dispatchers.Main){
                //2-Crear y modificar el adaptador
                val miAdaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombreCamas)

                spHabitaciones.adapter = miAdaptador
            }
        }

        //Función para seleccionar la hora
        fun showTimePickerDialog(textView: EditText) {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _: TimePicker, hourOfDay: Int, minute: Int ->
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cal.set(Calendar.MINUTE, minute)
                    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val formattedTime = format.format(cal.time)
                    textView.setText(formattedTime)
                },
                hour,
                minute,
                false
            )

            timePickerDialog.show()
        }

        txtControlPaciente.setOnClickListener {
            showTimePickerDialog(txtControlPaciente)
        }

        btnGuardarPaciente.setOnClickListener {
            if (txtNombrePaciente.text.toString().isEmpty() ||
                txtApellidoPaciente.text.toString().isEmpty() ||
                txtEdad.text.toString().isEmpty() ||
                txtControlPaciente.text.toString().isEmpty() ||
                spEnfermedades.selectedItemPosition == 0 ||
                spMedicamentos.selectedItemPosition == 0 ||
                spHabitaciones.selectedItemPosition == 0 ||
                spCamas.selectedItemPosition == 0

            ) {
                Toast.makeText(
                    requireContext(),
                    "Verifica que todos los campos estén completos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val objConexion = ClaseConexion().cadenaConexion()

                val agregarPaciente = objConexion?.prepareStatement("insert into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values (?, ?, ?, ?)")!!

                val habitacionCama =
                    agregarPaciente.setString(1, txtNombrePaciente.text.toString())
                    agregarPaciente.setString(2, txtApellidoPaciente.text.toString())
                    agregarPaciente.setString(3, txtEdad.text.toString())
                    agregarPaciente.setString(4, habitacionCama.text.toString())

                val agregarPacienteEmfermedad = objConexion?.prepareStatement("insert into tbPacientesEnfermedades (id_paciente, id_enfermedad) values (?, ?)")!!

                val pacienteEnfermedad =
                    agregarPacienteEmfermedad.setInt(1, )
                    agregarPacienteEmfermedad.setInt(2, )
            }
        }

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_agregar_paciente_prueba.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_agregar_paciente_prueba().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}