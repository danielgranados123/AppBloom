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
import kotlinx.coroutines.launch
import modelo.ClaseConexion
import modelo.DataClassHabitaciones
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


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


        btnVerPacientes.setOnClickListener {

        }

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

            // Actualizar el UI en el hilo principal
            requireActivity().runOnUiThread {
                txtNumeroPacientes.text = count.toString()
            }
        }.start()

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}