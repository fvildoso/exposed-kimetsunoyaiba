
import Ataques.id_ataque
import Cazadores.id_cazador
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.kohsuke.randname.RandomNameGenerator
import java.time.LocalDateTime


fun main() {
    Database.connect(
        "jdbc:mariadb://localhost:3306/cofradia",
        "org.mariadb.jdbc.Driver",
        "root",
        "local"
    )

    //nombres random
    val rnd = RandomNameGenerator(0)
    rnd.next().toString()

    transaction {

        //creamos las tablas si no existen
        SchemaUtils.create(Cazadores)
        SchemaUtils.create(Ataques)
        SchemaUtils.create(CazadoresAtaques)

        //obtenemos el ultimo ID de los cazadores
        var lastId = Cazadores.selectAll()
            .orderBy(id_cazador to SortOrder.DESC)
            .limit(1)
            .first()[id_cazador]

        //creamos cazadores
        for (i in 0..10) {
            lastId++
            Cazadores.insert { row ->
                row[id_cazador] = lastId
                row[nombre] = rnd.next().toString()
                row[rango] = "El mejor"
                row[pais_origen] = "Chile"
                row[birthday] = LocalDateTime.parse("2007-12-03T10:15:30")
            }
        }

        //obtenemos el ultimo ID de los cazadores
        val resultIdAtaque = Ataques.selectAll()
            .orderBy(id_ataque to SortOrder.DESC)
            .limit(1)

        var lastIdAtaque = 0
        if (resultIdAtaque.count() >= 1) {
            lastIdAtaque = resultIdAtaque.first()[id_ataque]
        }
        exposedLogger.info(lastIdAtaque.toString())

        //creamos ataques
        for (i in 0..10) {
            lastIdAtaque++
            Ataques.insert { row ->
                row[id_ataque] = lastIdAtaque
                row[nombre_ataque] = rnd.next().toString()
                row[arma] = rnd.next().toString()
            }
        }
    }
}