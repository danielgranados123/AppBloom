package daniel.granados.myapplication.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import daniel.granados.myapplication.R
import daniel.granados.myapplication.databinding.FragmentNotificationsBinding
import daniel.granados.myapplication.fragment_agregar_paciente_prueba
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.DataClassPacientes
import pacientesHelper.AdaptadorPacientes
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class NotificationsFragment : Fragment() {

    companion object variablesUsuarios {
        lateinit var idUsuario: String
    }

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.lblMisPacientes
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        idUsuario = UUID.randomUUID().toString()

        val btnNuevo = root.findViewById<FloatingActionButton>(R.id.btnAgregarPaciente)

        btnNuevo.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_notifications_to_fragment_agregar_paciente_prueba)
        }

        val uuidUsuario = idUsuario


        ///////////////////MOSTRAR PACIENTES EN EL RECYCLERVIEW///////////////
        val rcvPacientes = root.findViewById<RecyclerView>(R.id.rcvPacientes)


        //Asignar layout al RecyclerView
        rcvPacientes.layoutManager = LinearLayoutManager(requireContext())

        //Función para obtener datos
        fun obtenerPacientes(): List<DataClassPacientes> {

            val objConexion = ClaseConexion().cadenaConexion()
            val Statement = objConexion?.createStatement()

            val resultset = Statement?.executeQuery("SELECT p.id_paciente, p.nombres_paciente, p.edad_paciente, p.apellidos_paciente, h.nombre_habitacion, pm.hora_aplicacion FROM tbPacientes p INNER JOIN tbHabitacionesCamas hc ON p.ID_HabitacionCama = hc.ID_HabitacionCama INNER JOIN tbHabitaciones h ON hc.ID_Habitacion = h.ID_Habitacion INNER JOIN tbPacientesMedicamentos pm ON p.id_Paciente = pm.ID_Paciente ORDER BY CASE WHEN pm.hora_aplicacion >= SYSTIMESTAMP THEN (EXTRACT(DAY FROM (pm.hora_aplicacion - SYSTIMESTAMP)) * 86400 + EXTRACT(HOUR FROM (pm.hora_aplicacion - SYSTIMESTAMP)) * 3600 + EXTRACT(MINUTE FROM (pm.hora_aplicacion - SYSTIMESTAMP)) * 60 + EXTRACT(SECOND FROM (pm.hora_aplicacion - SYSTIMESTAMP))) ELSE NULL END DESC, CASE WHEN pm.hora_aplicacion < SYSTIMESTAMP THEN (EXTRACT(DAY FROM (SYSTIMESTAMP - pm.hora_aplicacion)) * 86400 + EXTRACT(HOUR FROM (SYSTIMESTAMP - pm.hora_aplicacion)) * 3600 + EXTRACT(MINUTE FROM (SYSTIMESTAMP - pm.hora_aplicacion)) * 60 + EXTRACT(SECOND FROM (SYSTIMESTAMP - pm.hora_aplicacion))) ELSE NULL END DESC")!!

            val pacientes = mutableListOf<DataClassPacientes>()

            val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            while (resultset.next()) {
                val id_paciente = resultset.getString("id_paciente")
                val nombre_paciente = resultset.getString("nombres_paciente")
                val apellido_paciente = resultset.getString("apellidos_paciente")
                val edad_paciente = resultset.getInt("edad_paciente")
                val habitacionCama = resultset.getString("nombre_habitacion")
                val control = resultset.getTimestamp("hora_aplicacion")

                val horaFormateada = hourFormat.format(control)

                val paciente = DataClassPacientes(id_paciente, nombre_paciente, apellido_paciente, edad_paciente, habitacionCama, horaFormateada)
                pacientes.add(paciente)
            }

            return pacientes
        }

        //Asignar adaptador
        CoroutineScope(Dispatchers.IO).launch {
            val pacientes = obtenerPacientes()
            withContext(Dispatchers.Main){
                val miAdapter = AdaptadorPacientes(pacientes)
                rcvPacientes.adapter = miAdapter
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}