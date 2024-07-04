package modelo

import java.sql.Time

data class DataClassPacientesMedicamentos(
    val ID_PacienteMedicamento: Int,
    var ID_Paciente: Int,
    var ID_Medicamento: Int,
    var hora_aplicacion: Time
)
