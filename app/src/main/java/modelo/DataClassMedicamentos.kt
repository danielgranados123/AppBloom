package modelo

data class DataClassMedicamentos(
    val ID_Medicamento: Int,
    var nombre_medicamento: String,
    var ID_Paciente: String,
    var control: String
)
