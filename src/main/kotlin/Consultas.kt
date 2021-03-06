import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.year
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    //variables de entorno del .env
    val dotenv = dotenv()

    Database.connect(
        dotenv["DATABASE_URL"],
        "org.mariadb.jdbc.Driver",
        dotenv["DATABASE_USER"],
        dotenv["DATABASE_PASS"]
    )

    transaction {

        exposedLogger.info("1. Cazadores que en su rango tienen 'en'.")
        Cazadores.select { Cazadores.rango like "%en%" }.forEach { cazador ->
            exposedLogger.info(cazador.toString())
        }

        exposedLogger.info("----------------------------------")
        exposedLogger.info("")
        exposedLogger.info("2. Enfrentamiento con mayor cantidad de involucrados el 2021")
        val personasEnfretamiento = mutableMapOf<Int, MutableList<String>>()
        (DetalleEnfrentamientos innerJoin Enfrentamientos)
            .slice(
                DetalleEnfrentamientos.id_cazador,
                DetalleEnfrentamientos.id_demonio,
                Enfrentamientos.id_enfrentamiento,
                Enfrentamientos.fecha
            )
            .select { Enfrentamientos.fecha.year().eq(2021) }
            .orderBy(Enfrentamientos.id_enfrentamiento to SortOrder.DESC)
            .forEach {
                println(it)

                //agregamos cazadores
                personasEnfretamiento
                    .putIfAbsent(
                        it[Enfrentamientos.id_enfrentamiento],
                        mutableListOf("Cazador#${it[DetalleEnfrentamientos.id_cazador]}")
                    )?.add("Cazador#${it[DetalleEnfrentamientos.id_cazador]}")

                //agregamos demonios
                personasEnfretamiento
                    .putIfAbsent(
                        it[Enfrentamientos.id_enfrentamiento],
                        mutableListOf("Demonio#${it[DetalleEnfrentamientos.id_demonio]}")
                    )?.add("Demonio#${it[DetalleEnfrentamientos.id_demonio]}")

                //limpiamos duplicados
                personasEnfretamiento[it[Enfrentamientos.id_enfrentamiento]] =
                    personasEnfretamiento[it[Enfrentamientos.id_enfrentamiento]]?.distinct() as MutableList<String>
            }
        exposedLogger.info(personasEnfretamiento.toString())

        //buscamos el mayor
        val maxPersonasEnfretamiento = personasEnfretamiento.maxByOrNull { t -> t.value.count() }
        exposedLogger.info(maxPersonasEnfretamiento.toString())

        exposedLogger.info("----------------------------------")
        exposedLogger.info("")
        exposedLogger.info("3. Ataques que m??s se repiten para Cazador con Tan o Jiro")
        var maxAtaques = 0L
        val ataques = (Cazadores innerJoin CazadoresAtaques innerJoin Ataques)
            .slice(
                Ataques.nombre_ataque,
                Ataques.id_ataque,
                Cazadores.nombre,
                Ataques.id_ataque.count()
            )
            .select { (Cazadores.nombre like "%milk%") or (Cazadores.nombre like "%test 1%") }
            .groupBy(Ataques.id_ataque)
            .orderBy(Ataques.id_ataque.count() to SortOrder.DESC)
            .filter {
                if (it[Ataques.id_ataque.count()] > maxAtaques) {
                    maxAtaques = it[Ataques.id_ataque.count()]
                }
                it[Ataques.id_ataque.count()] == maxAtaques
            }
            .map {
                return@map it
            }
        exposedLogger.info(ataques.toString())

        exposedLogger.info("----------------------------------")
        exposedLogger.info("")
        exposedLogger.info("4. Demonio m??s resistente")
        var maxDemonioMasResistente = 0L
        val demonioMasResistente = (DetalleEnfrentamientos innerJoin Demonios)
            .slice(
                Demonios.nombre,
                DetalleEnfrentamientos.estado_demonio,
                Demonios.id_demonio.count()
            )
            .selectAll()
            .groupBy(Demonios.id_demonio, DetalleEnfrentamientos.estado_demonio)
            .having { DetalleEnfrentamientos.estado_demonio like "vivo" }
            .orderBy(Demonios.id_demonio.count() to SortOrder.DESC)
            .map {
                if (it[Demonios.nombre].contains("Barby")) {
                    it[Demonios.nombre] = "Nezuko"
                }
                return@map it
            }
            .filter {
                if (it[Demonios.id_demonio.count()] > maxDemonioMasResistente) {
                    maxDemonioMasResistente = it[Demonios.id_demonio.count()]
                }
                it[Demonios.id_demonio.count()] == maxDemonioMasResistente
            }
        exposedLogger.info(demonioMasResistente.toString())

        exposedLogger.info("----------------------------------")
        exposedLogger.info("")
        exposedLogger.info("5. Demonios matados por Tanjiro")
        val demoniosTanjiro = (Cazadores innerJoin DetalleEnfrentamientos innerJoin Demonios)
            .slice(
                Demonios.nombre,
                DetalleEnfrentamientos.estado_demonio
            )
            .select {
                (DetalleEnfrentamientos.estado_demonio like "muerto") and (Cazadores.nombre like "Pipe%")
            }
            .map {
                return@map it
            }
        exposedLogger.info(demoniosTanjiro.toString())

        exposedLogger.info("----------------------------------")
        exposedLogger.info("")
        exposedLogger.info("Extra1. Revisamos vista")
        val vista = Top10
            .selectAll()
            .map {
                return@map it
            }
        exposedLogger.info(vista.toString())


        exposedLogger.info("----------------------------------")
        exposedLogger.info("")
        exposedLogger.info("Extra2. Stefano (consulta 2)")
        var max = 0
        var id = 1
        DetalleEnfrentamientos
            .leftJoin(
                Enfrentamientos,
                { id_enfrentamiento },
                { id_enfrentamiento })
            .slice(
                DetalleEnfrentamientos.id_enfrentamiento,
                DetalleEnfrentamientos.id_cazador.countDistinct(),
                DetalleEnfrentamientos.id_demonio.countDistinct()
            )
            .select { Enfrentamientos.fecha.year() eq 2021 }
            .groupBy(DetalleEnfrentamientos.id_enfrentamiento)
            .forEach {
                println(it)

                val cantidadParticipantes = it[DetalleEnfrentamientos.id_cazador.countDistinct()] +
                        it[DetalleEnfrentamientos.id_demonio.countDistinct()]
                val idEnfrentamiento = it[DetalleEnfrentamientos.id_enfrentamiento]
                if (max < cantidadParticipantes) {
                    max = cantidadParticipantes.toInt()
                    id = idEnfrentamiento
                }
//                exposedLogger.info("ID enfrentamiento " + id.toString())
//                exposedLogger.info("Cantidad de participantes " + max.toString())
            }
        exposedLogger.info("ID enfrentamiento $id")
        exposedLogger.info("Cantidad de participantes $max")

        exposedLogger.info("----------------------------------")
        exposedLogger.info("")
        exposedLogger.info("Extra3. Random")
        val cazadorRandom = Cazadores
            .slice(
                Cazadores.id_cazador,
                Cazadores.nombre
            )
            .selectAll()
            .map { return@map it }
            .random()
        exposedLogger.info(cazadorRandom.toString())

        val lista = listOf("a","b",1).random()
    }
}