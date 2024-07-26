package pacientesHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import daniel.granados.myapplication.R

class ViewHolderPacientes(view: View) : RecyclerView.ViewHolder(view){
    val lblApellidoPaciente: TextView = view.findViewById(R.id.lblApellidosPaciente)
    val lblHabitacion: TextView = view.findViewById(R.id.lblHabitacionPaciente)
    val lblHoraControl: TextView = view.findViewById(R.id.lblHorarioPaciente)

    val imgEliminar: ImageView = view.findViewById(R.id.imgBorrar)
}