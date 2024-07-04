package daniel.granados.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_detalles.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_detalles : Fragment() {
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
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_detalles, container, false)

        val btnSalir = root.findViewById<ImageView>(R.id.imgExitDetallePacientes)
        val btnEditarHabitacion = root.findViewById<ImageView>(R.id.imgEditarHabitacionPaciente)
        val btnEditarCama = root.findViewById<ImageView>(R.id.imgEditarCamaPaciente)
        val btnEliminarPaciente = root.findViewById<Button>(R.id.btnBorrarPaciente)

        //Volver a pantalla principal
        btnSalir.setOnClickListener{
            findNavController().navigate(R.id.action_fragment_detalles_to_navigation_notifications)
        }

        //Cuando el usuario selecciona el boton de editar, se habilitan los spinners
        btnEditarHabitacion.setOnClickListener{

        }

        btnEditarCama.setOnClickListener{

        }


        //Mostrar datos
        //Llamo a los elementos que tengo
        val txtApellidoPacienteDetalle = root.findViewById<TextView>(R.id.txtApellidoPacienteDetalle)
        val txtNombrePacienteDetalle = root.findViewById<TextView>(R.id.txtNombrePacienteDetalle)
        val txtEdadPacienteDetalle = root.findViewById<TextureView>(R.id.txtEdadPacienteDetalle)
        val btnEditarNombreApellidoDetalle = root.findViewById<ImageView>(R.id.imgEditarNombreApellidoDetalle)
        val btnimgEditarEdadPaciente = root.findViewById<TextureView>(R.id.imgEditarEdadPaciente)

        val txtEnfermedadPacienteDetalle = root.findViewById<TextureView>(R.id.txtEnfermedadPacienteDetalle)
        val rcvMedicamentos = root.findViewById<RecyclerView>(R.id.rcvMedicamentos)
        val btnEditarEnfermedadPaciente = root.findViewById<ImageView>(R.id.imgEditarEnfermedadPaciente)

        val spHabitacionPacienteDetalle = root.findViewById<Spinner>(R.id.spHabitacionPacienteDetalle)
        val spCamaPacienteDetalle = root.findViewById<Spinner>(R.id.spCamaPacienteDetalle)


        //Recibo los valores
        val nombrePaciente = arguments?.getString("nombrePaciente")
        val apellidoPaciente = arguments?.getString("apellidoPaciente")
        val edadPaciente = arguments?.getInt("edadPaciente")
        val enfermedadPaciente = arguments?.getString("enfermedadPaciente")
        val habitacionPaciente = arguments?.getString("habitacionPaciente")
        val camaPaciente = arguments?.getString("camaPaciente")



        //Asigno la informacion recibida a los TextView correspondientes
        txtNombrePacienteDetalle.text = nombrePaciente
        txtApellidoPacienteDetalle.text = apellidoPaciente



        //Editar campos




        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_detalles.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_detalles().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}