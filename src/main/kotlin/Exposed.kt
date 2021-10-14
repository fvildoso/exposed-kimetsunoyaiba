import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

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
    val arma =  varchar("arma", 50)
    override val primaryKey = PrimaryKey(id_ataque)
}

object CazadoresAtaques : Table() {
    val id_cazador_ataque = integer("id_cazador_ataque")
    val id_cazador = integer("id_cazador") references Cazadores.id_cazador
    val id_ataque = integer("id_ataque") references Ataques.id_ataque
    override val primaryKey = PrimaryKey(id_ataque)
}