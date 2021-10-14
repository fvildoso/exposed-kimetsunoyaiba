import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect(
        "jdbc:mariadb://localhost:3306/cofradia",
        "org.mariadb.jdbc.Driver",
        "root",
        "local"
    )

    transaction {

        //creamos las tablas si no existen
        SchemaUtils.create(Cazadores)
        SchemaUtils.create(Ataques)
        SchemaUtils.create(CazadoresAtaques)

        exposedLogger.info("1. Mostrar todos los Cazadores.")
        Cazadores.selectAll().forEach { cazador ->
            exposedLogger.info(cazador[Cazadores.nombre])
            exposedLogger.info(cazador[Cazadores.birthday].toString())
            exposedLogger.info(cazador[Cazadores.rango].toString())
            exposedLogger.info(cazador[Cazadores.pais_origen].toString())
        }

        exposedLogger.info("----------------------------------")
        exposedLogger.info("2. Mostrar todos los cazadores que parten con nombre Pipe")
        Cazadores.select{Cazadores.nombre like "Pipe%"}.forEach { cazador ->
            exposedLogger.info(cazador[Cazadores.nombre])
            exposedLogger.info(cazador[Cazadores.birthday].toString())
            exposedLogger.info(cazador[Cazadores.rango])
            exposedLogger.info(cazador[Cazadores.pais_origen])
        }
    }
}