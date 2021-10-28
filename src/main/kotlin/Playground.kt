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
        val cazadores = Cazadores.selectAll()

        cazadores.forEach { cazador ->
            exposedLogger.info(cazador[Cazadores.nombre])
            exposedLogger.info(cazador[Cazadores.birthday].toString())
            exposedLogger.info(cazador[Cazadores.rango].toString())
            exposedLogger.info(cazador[Cazadores.pais_origen].toString())
            println("🎉")
            println(cazador[Cazadores.pais_origen])
        }

        exposedLogger.info("----------------------------------")
        exposedLogger.info("2. Mostrar todos los cazadores que parten con nombre Pipe")
        Cazadores.select { Cazadores.nombre like "Pipe%" }.forEach { cazador ->
            exposedLogger.info(cazador[Cazadores.nombre])
            exposedLogger.info(cazador[Cazadores.birthday].toString())
            exposedLogger.info(cazador[Cazadores.rango])
            exposedLogger.info(cazador[Cazadores.pais_origen])
        }

        exposedLogger.info("3. Pruebas durante la clase")
        val test = Cazadores.slice(Cazadores.id_cazador)
            .selectAll() //podría ser mejor usar un filtro en la query, depende del caso
            .filter { cazador ->
                cazador[Cazadores.id_cazador] <= 10 //solo aceptamos las id que son menores o iguales a 10
            }
        val prueba2 = test.toList()
        println(prueba2)
        println(prueba2[0][Cazadores.id_cazador])

        exposedLogger.info("4. Subquery")
        val subQuery = Cazadores
            .selectAll()
            .orderBy(Cazadores.id_cazador to SortOrder.ASC)
            .limit(2)
            .alias("cualquiercosa")

        val unCazador = subQuery
            .slice(subQuery[Cazadores.id_cazador])
            .selectAll()
            .limit(1)

        unCazador.forEach {
            println(it)
        }
    }
}