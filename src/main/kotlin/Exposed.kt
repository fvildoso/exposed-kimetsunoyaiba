import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

//objetos de las tablas que tenemos

object Cazadores : Table() {
    val id_cazador = integer("id_cazador")
    val nombre = varchar("nombre", 50)
    val birthday = datetime("birthday").defaultExpression(CurrentDateTime())
    val rango = varchar("rango", 50)
    val pais_origen = varchar("pais_origen", 50)
    override val primaryKey = PrimaryKey(id_cazador)
}

object Ataques : Table() {
    val id_ataque = integer("id_ataque")
    val nombre_ataque = varchar("nombre_ataque", 50)
    val arma = varchar("arma", 50)
    override val primaryKey = PrimaryKey(id_ataque)
}

object CazadoresAtaques : Table("cazadores_ataques") {
    val id_cazador_ataque = integer("id_cazador_ataque")
    val id_cazador = integer("id_cazador") references Cazadores.id_cazador
    val id_ataque = integer("id_ataque") references Ataques.id_ataque
    override val primaryKey = PrimaryKey(id_cazador_ataque)
}

object Demonios : Table() {
    val id_demonio = integer("id_demonio")
    val nombre = varchar("nombre", 50)
    val rango = varchar("rango", 50)
    override val primaryKey = PrimaryKey(id_demonio)
}

object DetalleEnfrentamientos : Table("detalle_enfrentamientos") {
    val id_detalle_enfrentamiento = integer("id_detalle_enfrentamiento")
    val id_cazador = integer("id_cazador") references Cazadores.id_cazador
    val id_demonio = integer("id_demonio") references Demonios.id_demonio
    val id_enfrentamiento = integer("id_enfrentamiento") references Enfrentamientos.id_enfrentamiento
    val estado_cazador = varchar("estado_cazador", 50)
    val estado_demonio = varchar("estado_demonio", 50)
    override val primaryKey = PrimaryKey(id_detalle_enfrentamiento)
}

object Enfrentamientos : Table() {
    val id_enfrentamiento = integer("id_enfrentamiento")
    val fecha = datetime("fecha").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(id_enfrentamiento)
}

object LogsName : Table("logs_name") {
    val id_log = integer("id_log")
    val id_cazador = integer("id_cazador")
    val nombre = varchar("nombre", 50)
    val fecha = datetime("fecha").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(id_log)
}

object Top10 : Table("top10") {
    val id_cazador = integer("id_cazador")
    val nombre = varchar("nombre", 50)
    val birthday = datetime("birthday")
    val rango = varchar("rango", 50)
    val pais_origen = varchar("pais_origen", 50)
}