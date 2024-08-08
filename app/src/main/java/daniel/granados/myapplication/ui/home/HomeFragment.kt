package daniel.granados.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import daniel.granados.myapplication.R
import daniel.granados.myapplication.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import modelo.ClaseConexion
import modelo.DataClassHabitaciones
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var txtFecha: TextView
    private lateinit var txtHora: TextView

    private val horaFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val fechaFormat = SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", Locale("es", "ES"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.txtHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        val btnVerPacientes = root.findViewById<CardView>(R.id.btnVerPacientes)
        val txtNumeroPacientes = root.findViewById<TextView>(R.id.txtNumeroPacientes)
        txtHora = root.findViewById<TextView>(R.id.txtHora)
        txtFecha = root.findViewById<TextView>(R.id.txtFecha)

        //Hora y fecha
        mostrarFechaYHora()
        actualizarHora()

        /*btnVerPacientes.setOnClickListener {

        }*/

        //Cuenta de los pacientes
        Thread {
            var count = 0
            var connection: Connection? = null
            var statement: Statement? = null
            var resultSet: ResultSet? = null

            try {

                connection = ClaseConexion().cadenaConexion()

                statement = connection?.createStatement()

                resultSet = statement?.executeQuery("SELECT COUNT(*) FROM tbPacientes")

                if (resultSet?.next() == true) {
                    count = resultSet.getInt(1)
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                resultSet?.close()
                statement?.close()
                connection?.close()
            }


            requireActivity().runOnUiThread {
                txtNumeroPacientes.text = count.toString()
            }
        }.start()

        return root
    }

    private fun mostrarFechaYHora() {
        val horaActual = horaFormat.format(Date())

        txtFecha.text = mayusculaDia(fechaFormat.format(Date()))
        txtHora.text = horaActual
    }

    private fun mayusculaDia(fecha: String): String {
        return fecha.replaceFirstChar { it.uppercase(Locale("es", "ES")) }
    }

    private fun actualizarHora() {
        CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                // Actualizar la hora actual
                txtHora.text = horaFormat.format(Date())

                // Esperar 1 segundo antes de la siguiente actualización
                delay(1000)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}