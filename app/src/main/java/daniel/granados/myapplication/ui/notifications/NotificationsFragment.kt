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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.DataClassPacientes
import pacientesHelper.AdaptadorPacientes

class NotificationsFragment : Fragment() {

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

        val btnNuevo = root.findViewById<FloatingActionButton>(R.id.btnAgregarPaciente)

        btnNuevo.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_notifications_to_fragment_agregar_paciente_prueba)
        }


        ///////////////////MOSTRAR PACIENTES EN EL RECYCLERVIEW///////////////
        val rcvPacientes = root.findViewById<RecyclerView>(R.id.rcvPacientes)

        //Asignar layout al RecyclerView
        rcvPacientes.layoutManager = LinearLayoutManager(requireContext())

        //Función para obtener datos
        /*fun obtenerDatosGastos(): List<DataClassPacientes> {

            val objConexion = ClaseConexion().cadenaConexion()

            val Statement = objConexion?.createStatement()

            val resultset = Statement?.executeQuery("Select * from tbEnfermedades")!!

            val pacientes = mutableListOf<DataClassPacientes>()

            while (resultset.next()) {
                val uuid = resultset.getString("UUID_Gasto")

                val tipoGastoIngresoUUID = resultset.getInt("ID_TipoGastoIngreso")
                val clasificacion = resultset.getString("nombreClasificacion")
                val fuenteGasto = resultset.getString("UUID_fuenteGasto")
                val monto = resultset.getDouble("montoGasto")
                val fecha = resultset.getString("fechaGasto")

                val paciente = DataClassPacientes(uuid, uuidUsuario, tipoGastoIngresoUUID, clasificacion, fuenteGasto, monto, fecha)
                pacientes.add(paciente)
            }

            return pacientes
        }

        //Asignar adaptador
        CoroutineScope(Dispatchers.IO).launch {
            val gastosDB = obtenerDatosGastos()
            withContext(Dispatchers.Main){
                val miAdapter = AdaptadorPacientes(gastosDB)
                rcvPacientes.adapter = miAdapter
            }
        }*/


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}