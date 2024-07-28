package medicamentosHelper

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import daniel.granados.myapplication.R
import daniel.granados.myapplication.activity_detalles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modelo.ClaseConexion
import modelo.DataClassMedicamentos
import pacientesHelper.ViewHolderPacientes

class AdaptadorMedicamentos(private var Datos: List<DataClassMedicamentos>) : RecyclerView.Adapter<ViewHolderMedicamentos>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMedicamentos {
        val vista =            LayoutInflater.from(parent.context).inflate(R.layout.item_card_medicamentos, parent, false)
        return ViewHolderMedicamentos(vista)
    }


    fun eliminarMedicina(idMedicamento: Int, position: Int) {
        val listaDatos = Datos .toMutableList()
        listaDatos.removeAt(position)

        CoroutineScope(Dispatchers.IO).launch {
            val objConexion = ClaseConexion().cadenaConexion()
            val borrarPacienteMedicamento = objConexion?.prepareStatement("delete from tbPacientesMedicamentos where ID_Medicamento = ?")!!
            borrarPacienteMedicamento.setInt(1, idMedicamento)
            borrarPacienteMedicamento.executeUpdate()

            val commit = objConexion.prepareStatement( "commit")!!
            commit.executeUpdate()
        }
        Datos=listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderMedicamentos, position: Int) {
        val item = Datos[position]
        holder.txtMedicina.text = item.nombre_medicamento
        holder.txtHora.text = item.control

        holder.btnEliminar.setOnClickListener{
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
                eliminarMedicina(item.ID_Medicamento, position)
            }

            builder.setNegativeButton("No"){
                    dialog, wich ->
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }
        
    }
}