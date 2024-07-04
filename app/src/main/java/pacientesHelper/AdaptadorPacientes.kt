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

    fun eliminarPacientes(idPaciente: Int, position: Int) {
        val listaDatos = Datos .toMutableList()
        listaDatos.removeAt(position)

        CoroutineScope(Dispatchers.IO).launch {
            val objConexion = ClaseConexion().cadenaConexion()
            val borrarPaciente = objConexion?.prepareStatement("delete from tbPacientes where ID_Paciente = ?")!!
            borrarPaciente.setInt(1, idPaciente)
            borrarPaciente.executeUpdate()

            val commit = objConexion.prepareStatement( "commit")!!
            commit.executeUpdate()
        }
        Datos=listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = Datos.size


    fun actualizarNombrePaciente(id: Int, nombre: String, apellido: String, edad: Int){
        //1-Creo una corrutina
        GlobalScope.launch(Dispatchers.IO){
            //1- Crear objeto de la clase conexión
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Variable que contenga un prepareStatement
            val updateProducto = objConexion?.prepareStatement("update tbPacientes set nombre = ? where id_paciente = ?")!!
            updateProducto.setInt(1, id)
            updateProducto.setString(2, nombre)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

        }
    }

    override fun onBindViewHolder(holder: ViewHolderPacientes, position: Int) {
        //Asigno los valores a los textView de la card
        val item = Datos[position]

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, fragment_detalles::class.java)
            intent.putExtra(
                "nombrePaciente",
                item.nombrePaciente
            )
            intent.putExtra(
                "apellidoPaciente",
                item.apellidoPaciente
            )
            intent.putExtra(
                "edadPaciente",
                item.edadPaciente
            )



            context.startActivity(intent)
        }
    }
}