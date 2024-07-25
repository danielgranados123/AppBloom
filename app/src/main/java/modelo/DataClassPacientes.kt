package modelo

import java.sql.Timestamp

data class DataClassPacientes(
    val ID_Paciente: String,
    var nombrePaciente: String,
    var apellidoPaciente: String,
    var edadPaciente: Int,
    var habitacionCama: String,
    var control: Timestamp
)
