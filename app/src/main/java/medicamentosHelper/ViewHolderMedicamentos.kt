package medicamentosHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import daniel.granados.myapplication.R

class ViewHolderMedicamentos(view: View) : RecyclerView.ViewHolder(view){
    val txtMedicina: TextView = view.findViewById(R.id.txtMedicina)
    val txtHora: TextView = view.findViewById(R.id.txtHora)

    val btnEditar: ImageView = view.findViewById(R.id.btnEditar)
    val btnEliminar: ImageView = view.findViewById(R.id.btnEliminar)
}