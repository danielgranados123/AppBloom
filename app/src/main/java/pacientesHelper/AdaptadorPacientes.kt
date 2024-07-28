package pacientesHelper

import android.app.AlertDialog
import android.content.Intent
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import daniel.granados.myapplication.R
import daniel.granados.myapplication.activity_detalles
import daniel.granados.myapplication.fragment_detalles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.DataClassPacientes

class AdaptadorPacientes(private var Datos: List<DataClassPacientes>) : RecyclerView.Adapter<ViewHolderPacientes>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPacientes {
        val vista =            LayoutInflater.from(parent.context).inflate(R.layout.item_card_pacientes, parent, false)
        return ViewHolderPacientes(vista)
    }

    fun actualizarListaPacientes(nuevaLista:List<DataClassPacientes>){
        Datos=nuevaLista
        notifyDataSetChanged()
    }

    fun eliminarPacientes(idPaciente: String, position: Int) {
        val listaDatos = Datos .toMutableList()
        listaDatos.removeAt(position)

        CoroutineScope(Dispatchers.IO).launch {
            val objConexion = ClaseConexion().cadenaConexion()
            val borrarPacienteMedicamento = objConexion?.prepareStatement("delete from tbPacientesMedicamentos where ID_Paciente = ?")!!
            borrarPacienteMedicamento.setString(1, idPaciente)
            borrarPacienteMedicamento.executeUpdate()

            val borrarPacienteEnfermedad = objConexion?.prepareStatement("delete from tbPacientesEnfermedades where ID_Paciente = ?")!!
            borrarPacienteEnfermedad.setString(1, idPaciente)
            borrarPacienteEnfermedad.executeUpdate()

            val borrarPaciente = objConexion?.prepareStatement("delete from tbPacientes where ID_Paciente = ?")!!
            borrarPaciente.setString(1, idPaciente)
            borrarPaciente.executeUpdate()

            val commit = objConexion.prepareStatement( "commit")!!
            commit.executeUpdate()
        }
        Datos=listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolderPacientes, position: Int) {
        val item = Datos[position]
        holder.lblApellidoPaciente.text = item.apellidoPaciente
        holder.lblHabitacion.text = item.habitacionCama
        holder.lblHoraControl.text = item.control

            holder.imgEliminar.setOnClickListener{
                //Creamos una alerta
                //1-Invocamos el contexto

                val context = holder.itemView.context

                //Creo la alerta
                val builder = AlertDialog.Builder(context)

                //Le ponemos un titulo a la alerta

                builder.setTitle("¡Espera!")

                //Ponemos el mensaje
                builder.setMessage("¿Estás seguro de que deseas eliminar el registro?")

                //Paso final, agregamos los botones
                builder.setPositiveButton("Si"){
                        dialog, wich ->
                    eliminarPacientes(item.ID_Paciente, position)
                }

                builder.setNegativeButton("No"){
                        dialog, wich ->
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }
        

        //Asigno los valores a los textView de la card
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val pantallaDetalles = Intent(context, activity_detalles::class.java)
            pantallaDetalles.putExtra("id", item.ID_Paciente)
            pantallaDetalles.putExtra("nombre", item.nombrePaciente)
            pantallaDetalles.putExtra("apellido", item.apellidoPaciente)
            pantallaDetalles.putExtra("edad", item.edadPaciente)
            pantallaDetalles.putExtra("enfermedad", item.enfermedad)
            pantallaDetalles.putExtra("control", item.control)
            pantallaDetalles.putExtra("habitacion", item.habitacionCama)
            context.startActivity(pantallaDetalles)
        }
    }
}