package daniel.granados.myapplication

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.util.Calendar

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
        val txtControlPaciente = root.findViewById<TextView>(R.id.txtControlPaciente)

        txtControlPaciente.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtControlPaciente.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }

        btnGuardarPaciente.setOnClickListener {
            if (txtNombrePaciente.text.toString().isEmpty() ||
                txtNombrePaciente.text.toString().isEmpty() ||
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