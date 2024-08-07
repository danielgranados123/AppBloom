package daniel.granados.myapplication

import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import daniel.granados.myapplication.ui.notifications.NotificationsFragment.variablesUsuarios.idUsuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import medicamentosHelper.AdaptadorMedicamentos
import modelo.ClaseConexion
import modelo.DataClassCamas
import modelo.DataClassEnfermedades
import modelo.DataClassHabitaciones
import modelo.DataClassHabitacionesCamas
import modelo.DataClassMedicamentos
import modelo.DataClassPacientes
import pacientesHelper.AdaptadorPacientes
import java.sql.Timestamp
import java.sql.Types
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import java.sql.*
import java.time.*
import java.time.format.DateTimeFormatter

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

    // Variables globales para almacenar datos de habitaciones y camas
    private lateinit var listaHabitaciones: List<DataClassHabitaciones>
    private lateinit var listaCamas: List<DataClassCamas>

    //Para el recyclerView de medicamentos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_agregar_paciente_prueba, container, false)

        val idUsuario = idUsuario

        //1-Mandar a llamar a los elementos
        val spEnfermedades = root.findViewById<Spinner>(R.id.txtEnfermedadPaciente)
        val txtNombrePaciente = root.findViewById<EditText>(R.id.txtNombrePaciente)
        val txtApellidoPaciente = root.findViewById<EditText>(R.id.txtApellidoPaciente)
        val txtEdad = root.findViewById<EditText>(R.id.txtEdadPaciente)
        val spMedicamentos = root.findViewById<Spinner>(R.id.txtMedicamentosPaciente)
        val spHabitaciones = root.findViewById<Spinner>(R.id.spHabitacionPaciente)
        val spCamas = root.findViewById<Spinner>(R.id.spCamasPaciente)
        val btnGuardarPaciente = root.findViewById<Button>(R.id.btnGuardarPaciente)
        val txtControlPaciente = root.findViewById<EditText>(R.id.txtControlPaciente)
        val btnAgregarMedicamento = root.findViewById<ImageView>(R.id.btnAgregarMedicamento)

        val btnExit = root.findViewById<ImageView>(R.id.btnExit)

        btnExit.setOnClickListener {
            requireActivity().onBackPressed()
        }

        //Función para obtener las habitaciones
        fun obtenerHabitaciones(): List<DataClassHabitaciones> {
            // Crear un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //Crear un Statement que ejecutará el Select
            val statement = objConexion?.createStatement()

            val resulset = statement?.executeQuery("Select * from tbHabitaciones")!!

            val listaHabitaciones = mutableListOf<DataClassHabitaciones>()

            while (resulset.next()) {
                val id = resulset.getInt("ID_Habitacion")
                val nombre = resulset.getString("nombre_habitacion")
                val habitacionCompleta = DataClassHabitaciones(id, nombre)

                listaHabitaciones.add(habitacionCompleta)
            }

            return listaHabitaciones
        }

        //Función para obtener las camas de una habitación específica
        fun obtenerCamas(idHabitacion: Int): List<DataClassCamas> {
            // Crear un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //Preparar la consulta SQL para obtener las camas de la habitación seleccionada
            val selectCamas = objConexion?.prepareStatement("SELECT c.id_cama, c.nombre_cama FROM tbHabitacionesCamas hc INNER JOIN tbCamas c ON hc.id_Cama = c.ID_Cama WHERE ID_Habitacion = ?")!!

            selectCamas.setInt(1, idHabitacion)
            val resultSet = selectCamas.executeQuery()

            val listaCamas = mutableListOf<DataClassCamas>()

            while (resultSet.next()) {
                val id = resultSet.getInt("id_cama")
                val nombre = resultSet.getString("nombre_cama")
                val camaCompleta = DataClassCamas(id, nombre)

                listaCamas.add(camaCompleta)
            }

            return listaCamas
        }

        //Función para obtener la relación entre habitaciones y camas
        fun obtenerHabitacionesCamas(): List<DataClassHabitacionesCamas> {
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbHabitacionesCamas")!!

            val listaHabitacionesCamas = mutableListOf<DataClassHabitacionesCamas>()

            while (resultSet.next()) {
                val idHabitacionCama = resultSet.getInt("ID_HabitacionCama")
                val idHabitacion = resultSet.getInt("ID_Habitacion")
                val idCama = resultSet.getInt("ID_Cama")
                val habitacionCama =
                    DataClassHabitacionesCamas(idHabitacionCama, idHabitacion, idCama)
                listaHabitacionesCamas.add(habitacionCama)
            }

            return listaHabitacionesCamas
        }

        //En el método donde inicializas los spinners y sus listeners
        CoroutineScope(Dispatchers.IO).launch {
            //Obtener los datos de las habitaciones
            listaHabitaciones = obtenerHabitaciones()

            withContext(Dispatchers.Main) {
                //Adaptador para el spinner de habitaciones
                val adaptadorHabitaciones = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    listaHabitaciones.map { it.nombre_habitacion }
                )
                spHabitaciones.adapter = adaptadorHabitaciones
            }
        }

        //Listener para el spinner de habitaciones
        spHabitaciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != AdapterView.INVALID_POSITION) {
                    //Obtener el ID de la habitación seleccionada
                    val idHabitacionSeleccionada = listaHabitaciones[position].ID_Habitacion

                    //Llamar a la función para obtener las camas correspondientes
                    CoroutineScope(Dispatchers.IO).launch {
                        listaCamas = obtenerCamas(idHabitacionSeleccionada)

                        withContext(Dispatchers.Main) {
                            //Adaptador para el spinner de camas
                            val adaptadorCamas = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                listaCamas.map { it.nombre_cama }
                            )
                            spCamas.adapter = adaptadorCamas

                            //Habilitar el spinner de camas solo si hay camas disponibles
                            spCamas.isEnabled = listaCamas.isNotEmpty()
                        }
                    }
                } else {
                    //Si no se ha seleccionado nada en el spinner de habitaciones
                    spCamas.isEnabled = false
                    spCamas.adapter = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Si no se ha seleccionado nada en el spinner de habitaciones
                spCamas.isEnabled = false
                spCamas.adapter = null
            }
        }

        fun asignarIdHabitacionCama(idHabitacionCama: Int) {
            println("$idHabitacionCama")
        }

        //Spinner de camas
        spCamas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != AdapterView.INVALID_POSITION) {
                    // Obtener el ID de la cama seleccionada
                    val idCamaSeleccionada = listaCamas[position].ID_Cama

                    // Buscar el ID_HabitacionCama que coincida con la habitación y la cama seleccionadas
                    CoroutineScope(Dispatchers.IO).launch {
                        val listaHabitacionesCamas = obtenerHabitacionesCamas()
                        val idHabitacionCama = listaHabitacionesCamas.find {
                            it.ID_Habitacion == listaHabitaciones[spHabitaciones.selectedItemPosition].ID_Habitacion &&
                                    it.ID_Cama == idCamaSeleccionada
                        }

                        withContext(Dispatchers.Main) {
                            if (idHabitacionCama != null) {
                                asignarIdHabitacionCama(idHabitacionCama.ID_HabitacionCama)
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

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

                    //Como en la base de datos el tipo de dato es timeStamp:
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val formattedDateTime = dateFormat.format(cal.time)

                    textView.setText(formattedDateTime)
                },
                hour,
                minute,
                true
            )

            timePickerDialog.show()
        }

        fun stringToTimestamp(dateTimeString: String): Timestamp {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val parsedDate = dateFormat.parse(dateTimeString)
            return Timestamp(parsedDate.time)
        }

        //El txt pasa a ser la hora que el usuario selecciona
        txtControlPaciente.setOnClickListener {
            showTimePickerDialog(txtControlPaciente)
        }

        //Sp de enfermedades
        fun obtenerEnfermedades(): List<DataClassEnfermedades> {
            //Crear un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //crepo un Statement que me ejecutara el Select
            val Statement = objConexion?.createStatement()


            val resulset = Statement?.executeQuery("Select * from tbEnfermedades")!!

            val listadoEnfermedades = mutableListOf<DataClassEnfermedades>()

            while (resulset.next()) {
                val id = resulset.getInt("id_enfermedad")
                val nombre = resulset.getString("nombre_enfermedad")
                val enfermedad = DataClassEnfermedades(id, nombre)

                listadoEnfermedades.add(enfermedad)
            }

            return listadoEnfermedades
        }

        //Programar Spinner para que muestre datos del select
        CoroutineScope(Dispatchers.IO).launch {
            //Obtengo los datos
            val listadoDeEnfermedades = obtenerEnfermedades()
            val nombreEnfermedad = listadoDeEnfermedades.map { it.nombre_enfermedad }

            withContext(Dispatchers.Main) {
                //2-Crear y modificar el adaptador
                val miAdaptador = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    nombreEnfermedad
                )

                spEnfermedades.adapter = miAdaptador
            }
        }


        //Sp de medicamentos
        fun obtenerMedicamentos(): List<DataClassMedicamentos> {
            //Crear un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //crepo un Statement que me ejecutara el Select
            val Statement = objConexion?.createStatement()

            val resulset = Statement?.executeQuery("Select m.id_medicamento, m.nombre_medicamento, p.ID_Paciente, pm.hora_aplicacion from tbMedicamentos m inner join tbPacientesMedicamentos pm on m.ID_Medicamento = pm.ID_Medicamento inner join tbPacientes p on pm.ID_Paciente = p.ID_Paciente")!!

            val listadoMedicamentos = mutableListOf<DataClassMedicamentos>()

            while (resulset.next()) {
                val id = resulset.getInt("id_medicamento")
                val nombre = resulset.getString("nombre_medicamento")
                val idPaciente = resulset.getString("ID_Paciente")
                val control = resulset.getString("hora_aplicacion")
                val medicamento = DataClassMedicamentos(id, nombre, idPaciente, control)

                listadoMedicamentos.add(medicamento)
            }

            return listadoMedicamentos
        }

        //Programar Spinner para que muestre datos del select
        CoroutineScope(Dispatchers.IO).launch {
            //Obtengo los datos
            val listadoDeMedicamentos = obtenerMedicamentos()
            val nombreMedicamento = listadoDeMedicamentos.map { it.nombre_medicamento }

            withContext(Dispatchers.Main) {
                //2-Crear y modificar el adaptador
                val miAdaptador = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    nombreMedicamento
                )

                spMedicamentos.adapter = miAdaptador
            }
        }


        //Mostrar medicamentos agregados hasta el momento
        val rcvMedicina = root.findViewById<RecyclerView>(R.id.rcvMedicamentos)
        val idTemporal = UUID.randomUUID().toString() //Id temporal para el paciente


        fun obtenerNombreMedicamento(idMedicamento: Int): String {
            val objConexion = ClaseConexion().cadenaConexion()
            var nombre = ""

            try {
                val statement = objConexion?.prepareStatement("select nombre_medicamento from tbMedicamentos where id_medicamento = ?")
                statement?.setInt(1, idMedicamento)
                val resultSet = statement?.executeQuery()

                if (resultSet?.next() == true) {
                    nombre = resultSet.getString("nombre_medicamento")
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }

            return nombre
        }

        //Función para obtener datos
        fun obtenerMedicamentosPendientes(idTemporal: String): List<DataClassMedicamentos> {
            val objConexion = ClaseConexion().cadenaConexion()
            val medicamentos = mutableListOf<DataClassMedicamentos>()

                val statement = objConexion?.prepareStatement("select ID_Medicamento, hora_aplicacion from tbMedicamentosTemporales where ID_PacienteTemporal = ?")
                statement?.setString(1, idTemporal)

                val resultSet = statement?.executeQuery()
                val hourFormat = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())

                while (resultSet?.next() == true) {
                    val idMedicamento = resultSet.getInt("ID_Medicamento")
                    val horaAplicacion = resultSet.getTimestamp("hora_aplicacion")
                    val horaFormateada = hourFormat.format(horaAplicacion)

                    val nombre = obtenerNombreMedicamento(idMedicamento)

                    val medicamento = DataClassMedicamentos(idMedicamento, nombre, idTemporal, horaFormateada)
                    medicamentos.add(medicamento)
                }

            return medicamentos
        }

        //Asignar adaptador
        CoroutineScope(Dispatchers.IO).launch {
            rcvMedicina.layoutManager = LinearLayoutManager(requireContext())

            val medicamento = obtenerMedicamentosPendientes(idTemporal)

            withContext(Dispatchers.Main){
                val miAdapter = AdaptadorMedicamentos(medicamento)
                rcvMedicina.adapter = miAdapter
            }
        }

        //Guardar medicamentos para mostrarlos en el recyclerView
        btnAgregarMedicamento.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexion()

                //Medicamentos
                val medicamento = obtenerMedicamentos()

                val pacienteMedicamentoTemporal = objConexion?.prepareStatement("insert into tbMedicamentosTemporales (ID_PacienteTemporal, ID_Medicamento, hora_aplicacion) values (?, ?, ?)")!!
                pacienteMedicamentoTemporal.setString(1, idTemporal)
                pacienteMedicamentoTemporal.setInt(2,medicamento[spMedicamentos.selectedItemPosition].ID_Medicamento)
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(txtControlPaciente.text.toString())
                val stamp = Timestamp(date.time)
                pacienteMedicamentoTemporal.setTimestamp(3, stamp)

                pacienteMedicamentoTemporal.executeUpdate()

                val nuevosMedicamentos = obtenerMedicamentosPendientes(idTemporal)
                withContext(Dispatchers.Main){
                    (rcvMedicina.adapter as? AdaptadorMedicamentos)?.actualizarLista(nuevosMedicamentos)
                }

            }
        }

        btnGuardarPaciente.setOnClickListener {
            //Validaciones
            if (txtNombrePaciente.text.isEmpty() || txtApellidoPaciente.text.isEmpty() || txtEdad.text.isEmpty() || txtControlPaciente.text.isEmpty()) {
                Toast.makeText(requireContext(), "Verifica que todos los campos estén completados.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Verificar que el nombre y el apellido contengan solo letras
            if (!txtNombrePaciente.text.matches(Regex("^[a-zA-Z]+$")) || !txtApellidoPaciente.text.matches(Regex("^[a-zA-Z]+$"))) {
                Toast.makeText(requireContext(), "Nombre o apellido no válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Verificar que la edad sea un número dentro del rango de 1 a 12 (Edades de los niños en el hospital Bloom)
            val edad = txtEdad.text.toString().toIntOrNull()
            if (edad == null || edad < 1 || edad > 12) {
                Toast.makeText(requireContext(), "La edad ingresada no es válida.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Verificar que la hora contenga solo números
            if (!txtControlPaciente.text.matches(Regex("^[0-9]+$"))) {
                Toast.makeText(requireContext(), "La hora ingresada no es válida.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idCamaSeleccionada = listaCamas[spCamas.selectedItemPosition].ID_Cama

            //Buscar el ID_HabitacionCama que coincida con la habitación y la cama seleccionadas
            CoroutineScope(Dispatchers.IO).launch {
                val listaHabitacionesCamas = obtenerHabitacionesCamas()
                val idHabitacionSeleccionada =
                    listaHabitaciones[spHabitaciones.selectedItemPosition].ID_Habitacion
                val idHabitacionCama = listaHabitacionesCamas.find {
                    it.ID_Habitacion == idHabitacionSeleccionada && it.ID_Cama == idCamaSeleccionada
                }?.ID_HabitacionCama

                if (idHabitacionCama != null) {

                    //Enfermedades
                    val enfermedad = obtenerEnfermedades()

                    //Medicamentos
                    val medicamento = obtenerMedicamentos()

                    //Habitaciones y Camas
                    val habitacionCama = idHabitacionCama.toString()

                    //Insertar el paciente
                    val objConexion = ClaseConexion().cadenaConexion()

                    val agregarPaciente =
                        objConexion?.prepareStatement("insert into tbPacientes (id_Paciente,nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values (?, ?, ?, ?, ?)")!!


                    agregarPaciente.setString(1, idUsuario)
                    agregarPaciente.setString(2, txtNombrePaciente.text.toString())
                    agregarPaciente.setString(3, txtApellidoPaciente.text.toString())
                    agregarPaciente.setString(4, txtEdad.text.toString())
                    agregarPaciente.setString(5, habitacionCama)
                    agregarPaciente.executeUpdate()

                    val pacienteEnfermedad = objConexion?.prepareStatement("insert into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (?, ?)")!!
                    pacienteEnfermedad.setString(1, idUsuario)
                    pacienteEnfermedad.setInt(2, enfermedad[spEnfermedades.selectedItemPosition].ID_Enfermedad)
                    pacienteEnfermedad.executeUpdate()

                    //Mover datos desde tbMedicamentosTemporales a tbPacientesMedicamentos
                    val moverMedicamento = objConexion?.prepareStatement("insert into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) select ?, ID_Medicamento, hora_aplicacion from tbMedicamentosTemporales where ID_PacienteTemporal = ?")
                    moverMedicamento?.setString(1, idUsuario)
                    moverMedicamento?.setString(2, idTemporal)
                    moverMedicamento?.executeUpdate()

                    // Eliminar registros de la tabla temporal
                    val eliminarMedicamentoTablaTemporal = objConexion?.prepareStatement("delete from tbMedicamentosTemporales where ID_PacienteTemporal = ?")
                    eliminarMedicamentoTablaTemporal?.setString(1, idTemporal)
                    eliminarMedicamentoTablaTemporal?.executeUpdate()
                }

                withContext(Dispatchers.Main) {
                    txtNombrePaciente.text.clear()
                    txtApellidoPaciente.text.clear()
                    txtEdad.text.clear()
                    txtControlPaciente.text.clear()

                    val nuevosMedicamentos = obtenerMedicamentosPendientes(idTemporal)
                    (rcvMedicina.adapter as? AdaptadorMedicamentos)?.actualizarLista(nuevosMedicamentos)

                    Toast.makeText(
                        requireContext(),
                        "Paciente agregado correctamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return root
    }

}