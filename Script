//Script de la base de datos
//Lleva datos de ejemplo para que pueda visualizar todas las herramientas de la aplicación, y que la pueda ver funcionando de una forma más realista.

----Tabla para guardar las habitaciones----
create table tbHabitaciones(
    ID_Habitacion number primary key,
    nombre_habitacion varchar2(20) not null 
    /*Lo guardo como tipo de dato string/varchar,
    ya que en algunos lugares las habitaciones se
    identifican con claves alfanumericas (Habitación 1A, 2B, etc)*/
);

//Secuencia para tabla Habitaciones
create sequence habitacion
start with 1 
increment by 1;

//Triggers para Habitaciones
create trigger TrigHabitacion
before
insert on tbHabitaciones
for each row
begin
select habitacion.nextval into : new.ID_Habitacion from dual;
end;


----Tabla para almacenar los números de cama en cada habitación----
create table tbCamas(
    ID_Cama number primary key,
    nombre_cama varchar2(50) not null
);

//Secuencia para tabla Camas
create sequence cama
start with 1 
increment by 1;

//Triggers para Camas
create trigger TrigCama
before
insert on tbCamas
for each row
begin
select cama.nextval into : new.ID_Cama from dual;
end;


/*Interpreto que en los hospitales se utiliza una misma 'clave' para
    identificar las habitaciones y las camas. Por ejemplo:
    En las habitaciones A1, B2, C3, a pesar de ser distintas, es posible
    que todas tengan el mismo número de camas. Por lo tanto:
    Habitación A1 - Camas 1, 2, 3, 4, 5
    Habitación B2 - Camas 1, 2, 3, 4, 5
    Habitación C3 - Camas 1, 2, 3, 4
    
    Y de este modo, se genera una relación de muchos a muchos, ya que
    una misma habitación puede tener varias camas, y cada número de cama (que es
    lo que se almacena en la tabla) puede estar en varias habitaciones.*/

create table tbHabitacionesCamas(
    ID_HabitacionCama number primary key,
    ID_Habitacion number not null,
    ID_Cama number not null,
    
    constraint fk_habitacion foreign key (ID_Habitacion) references tbHabitaciones (ID_Habitacion),
    constraint fk_cama foreign key (ID_Cama) references tbCamas (ID_Cama)
);

//Secuencia para tabla HabitacionesCamas
create sequence habitacionCama
start with 1 
increment by 1;

//Triggers para HabitacionesCamas
create trigger TrigHabitacionCama
before
insert on tbHabitacionesCamas
for each row
begin
select habitacionCama.nextval into : new.ID_HabitacionCama from dual;
end;

        
----Tabla para almacenar enfermedades----
create table tbEnfermedades(
    ID_Enfermedad number primary key,
    nombre_enfermedad varchar2(100) not null
);

//Secuencia para tabla enfermedades
create sequence enfermedad
start with 1 
increment by 1;

//Triggers para enfermedades
create trigger TrigEnfermedad
before
insert on tbEnfermedades
for each row
begin
select enfermedad.nextval into : new.ID_Enfermedad from dual;
end;


----Tabla para guardar los medicamentos----
create table tbMedicamentos(
    ID_Medicamento number primary key,
    nombre_medicamento varchar2(20)
);

//Secuencia para tabla medicamentos
create sequence medicamento
start with 1 
increment by 1;

//Triggers para medicamentos
create trigger TrigMedicamento
before
insert on tbMedicamentos
for each row
begin
select medicamento.nextval into : new.ID_Medicamento from dual;
end;


----Tabla para guardar los pacientes con su información principal----
create table tbPacientes(
    ID_Paciente number primary key,
    nombres_paciente varchar2(30) not null,
    apellidos_paciente varchar2(30) not null,
    edad_paciente number constraint ck_edad_paciente check (edad_paciente >= 1),

    ID_HabitacionCama number unique,
 
   constraint fk_HabitacionesCama foreign key (ID_HabitacionCama) references tbHabitacionesCamas (ID_HabitacionCama)
);

//Secuencia para tabla medicamentos
create sequence paciente
start with 1 
increment by 1;

//Triggers para medicamentos
create trigger TrigPaciente
before
insert on tbPacientes
for each row
begin
select paciente.nextval into : new.ID_Paciente from dual;
end;


----Relaciones del paciente----
create table tbPacientesEnfermedades(
    ID_PacienteEnfermedad number primary key,
    ID_Paciente number,
    ID_Enfermedad number,
    
    constraint fk_paciente foreign key (ID_Paciente) references tbPacientes (ID_Paciente),
    constraint fk_enfermedad foreign key (ID_Enfermedad) references tbEnfermedades (ID_Enfermedad)
);

//Secuencia para tabla pacientesEnfermedades
create sequence pacienteEnfermedad
start with 1 
increment by 1;

//Triggers para pacientesEnfermedades
create trigger TrigPacienteEnfermedad
before
insert on tbPacientesEnfermedades
for each row
begin
select pacienteEnfermedad.nextval into : new.ID_PacienteEnfermedad from dual;
end;


create table tbPacientesMedicamentos(
    ID_PacienteMedicamento number primary key,
    ID_Paciente number,
    ID_Medicamento number,
    hora_aplicacion timestamp,
    
    constraint fk_paciente1 foreign key (ID_Paciente) references tbPacientes (ID_Paciente),
    constraint fk_medicamento foreign key (ID_Medicamento) references tbMedicamentos(ID_Medicamento)
);

//Secuencia para tabla PacientesMedicamentos
create sequence pacienteMedicamento
start with 1 
increment by 1;

//Triggers para PacientesMedicamentos
create trigger TrigPacienteMedicamento
before
insert on tbPacientesMedicamentos
for each row
begin
select pacienteMedicamento.nextval into : new.ID_PacienteMedicamento from dual;
end;

----Ingreso de valores ilustrativos----
insert all
into tbHabitaciones (nombre_habitacion) values ('A1')
into tbHabitaciones (nombre_habitacion) values ('A2')
into tbHabitaciones (nombre_habitacion) values ('A3')
into tbHabitaciones (nombre_habitacion) values ('A4')
into tbHabitaciones (nombre_habitacion) values ('B1')
into tbHabitaciones (nombre_habitacion) values ('B2')
into tbHabitaciones (nombre_habitacion) values ('B3')
into tbHabitaciones (nombre_habitacion) values ('B4')
into tbHabitaciones (nombre_habitacion) values ('C1')
into tbHabitaciones (nombre_habitacion) values ('C2')
into tbHabitaciones (nombre_habitacion) values ('C3')
into tbHabitaciones (nombre_habitacion) values ('C4')
select * from dual;

select * from tbHabitaciones;

insert all
into tbCamas (nombre_cama) values ('1')
into tbCamas (nombre_cama) values ('2')
into tbCamas (nombre_cama) values ('3')
into tbCamas (nombre_cama) values ('4')
into tbCamas (nombre_cama) values ('5')
into tbCamas (nombre_cama) values ('6')
select * from dual;


select * from tbCamas;


insert all
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('1','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('1','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('1','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('1','4')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('1','5')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('1','6')

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('2','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('2','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('2','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('2','4')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('2','5')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('2','6')

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('3','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('3','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('3','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('3','4')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('3','5')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('3','6')

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('4','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('4','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('4','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('4','4')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('4','5')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('4','6')

------

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('5','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('5','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('5','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('5','4')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('5','5')

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('6','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('6','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('6','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('6','4')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('6','5')

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('7','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('7','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('7','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('7','4')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('7','5')

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('8','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('8','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('8','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('8','4')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('8','5')

------

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('9','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('9','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('9','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('9','4')

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('10','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('10','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('10','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('10','4')

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('11','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('11','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('11','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('11','4')

into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('12','1')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('12','2')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('12','3')
into tbHabitacionesCamas (ID_Habitacion, ID_Cama) values ('12','4')
select * from dual;


select * from tbHabitacionesCamas;

insert all
into tbEnfermedades (nombre_enfermedad) values ('Neumonía')
into tbEnfermedades (nombre_enfermedad) values ('Epilepsia')
into tbEnfermedades (nombre_enfermedad) values ('Quemaduras Primer Grado')
into tbEnfermedades (nombre_enfermedad) values ('Quemaduras Segundo Grado')
into tbEnfermedades (nombre_enfermedad) values ('Quemaduras Tercer Grado')
into tbEnfermedades (nombre_enfermedad) values ('Cáncer')
into tbEnfermedades (nombre_enfermedad) values ('Apendicitis')
into tbEnfermedades (nombre_enfermedad) values ('Traumatismo')
into tbEnfermedades (nombre_enfermedad) values ('Diabetes Tipo 1')
into tbEnfermedades (nombre_enfermedad) values ('Diabetes Tipo 2')
into tbEnfermedades (nombre_enfermedad) values ('Asma')
select * from dual;


select * from tbEnfermedades;


insert all
into tbMedicamentos (nombre_medicamento) values ('Amoxicilina')
into tbMedicamentos (nombre_medicamento) values ('Ceftriaxona')
into tbMedicamentos (nombre_medicamento) values ('Azitromicina')
into tbMedicamentos (nombre_medicamento) values ('Paracetamol')
into tbMedicamentos (nombre_medicamento) values ('Ibuprofeno')
into tbMedicamentos (nombre_medicamento) values ('Morfina')
into tbMedicamentos (nombre_medicamento) values ('Insulina lispro')
into tbMedicamentos (nombre_medicamento) values ('Insulina aspart')
into tbMedicamentos (nombre_medicamento) values ('Insulina glargina')
into tbMedicamentos (nombre_medicamento) values ('Insulina detemir')
into tbMedicamentos (nombre_medicamento) values ('Fenitoína')
into tbMedicamentos (nombre_medicamento) values ('Fentanilo')
select * from dual;


select * from tbMedicamentos;


insert all
into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('Daniel Isaac', 'Granados Cañas', 4, 1)
into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('Bryan Exequiel', 'Miranda Rodríguez', 5, 7)
into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('Mirna', 'Espinoza Anzora', 3, 13)
into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('Emerson Alexander', 'González Rodríguez', 4, 19)

into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('Manuel Alejandro', 'Ortega Rodríguez', 6, 25)
into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('Bryan Eduardo', 'Cornejo Pérez', 7, 30)
into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('Rodrigo Andrés', 'Hurtado Quezada', 8, 35)
into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('Rodrigo Josué', 'Monterrosa Jorge', 9,40)

into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('David Enrique', 'Castillo Oliva', 12, 45)
into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('Amaris Lourdes', 'Osorio Canales', 10, 49)
into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('Edenilson Alexander', 'Amaya Benítez', 11, 53)
into tbPacientes (nombres_paciente, apellidos_paciente, edad_paciente, ID_HabitacionCama) values ('José Luis', 'Iraheta Marroquín', 12, 57)
select * from dual;


select * from tbPacientes;


insert all
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (1, 1)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (2, 10)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (3, 11)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (4, 2)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (5, 3)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (6, 3)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (7, 4)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (8, 5)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (9, 6)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (10, 7)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (11, 8)
into tbPacientesEnfermedades (ID_Paciente, ID_Enfermedad) values (12, 9)
select * from dual;


select * from tbPacientesEnfermedades;


insert all
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (1, 3, timestamp '2024-06-23 10:30:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (2, 7, timestamp '2024-06-23 09:00:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (3, 2, timestamp '2024-06-23 12:30:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (4, 6, timestamp '2024-06-23 13:30:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (5, 4, timestamp '2024-06-23 14:20:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (6, 4, timestamp '2024-06-23 15:40:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (7, 5, timestamp '2024-06-23 16:30:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (8, 6, timestamp '2024-06-23 17:00:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (9, 4, timestamp '2024-06-23 18:30:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (10, 5, timestamp '2024-06-23 19:30:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (11, 12, timestamp '2024-06-23 20:30:00')
into tbPacientesMedicamentos (ID_Paciente, ID_Medicamento, hora_aplicacion) values (12, 9, timestamp '2024-06-23 21:30:00')
select * from dual;

select * from tbPacientesEnfermedades;
