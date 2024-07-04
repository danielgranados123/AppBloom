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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = Datos[position]
        holder..text = ticket.titulo
        holder.txtAutor.text = ticket.nombreAutor


        val item = Datos[position]

            holder.imgBorrar.setOnClickListener {
                //Creamos una alerta
                //1-Invocamos el contexto

                val context = holder.itemView.context

                //Creo la alerta
                val builder = AlertDialog.Builder(context)

                //Le ponemos un titulo a la alerta

                builder.setTitle("¡Espera!")

                //Ponemos el mensaje
                builder.setMessage("¿Estás seguro de que deseas eliminar este paciente?")

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

            holder.imgEditar.setOnClickListener {

                val context = holder.itemView.context

                //Crear alerta

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Editar estado del ticket:")

                //Agregamos cuadro de texto para que el usuario escriba el nuevo nombre
                val cuadritoNuevoNombre = EditText(context)
                cuadritoNuevoNombre.setHint(item.estado)
                builder.setView(cuadritoNuevoNombre)

                //Paso final, agregamos los botones
                builder.setPositiveButton("Actualizar"){
                        dialog, wich ->
                    actualizarTickets(cuadritoNuevoNombre.text.toString(), item.uuid)
                }

                builder.setNegativeButton("Cancelar"){
                        dialog, wich ->
                    dialog.dismiss()
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }

        //Darle clic a la card
        holder.itemView.setOnClickListener {
            //Invoco el contexto
            val context = holder.itemView.context

            //Cambiamos la pantalla
            //Abrimos la pantalla de detalle de pacientes

            val pantallaDetalles = Intent(context, fragment_detalles::class.java)

            //Aqui, antes de abrir la nueva pantalla le mando los parametros
            pantallaDetalles.putExtra("nombresPaciente", item.nombrePaciente)
            pantallaDetalles.putExtra("apellidosPaciente", item.apellidoPaciente)


            context.startActivity(pantallaDetalles)


        }
    }
}