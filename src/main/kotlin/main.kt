import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.*

fun main() {
    println("hola hola")
    println("esto es solo una prueba")
    println("esto es solo una prueba otra vez")

    val connectionProps = Properties()
    connectionProps.put("user", "root")
    connectionProps.put("password", "local")
    try {
        Class.forName("org.mariadb.jdbc.Driver").newInstance()
        val conn = DriverManager.getConnection(
            "jdbc:" + "mysql" + "://" +
                    "127.0.0.1" +
                    ":" + "3306" + "/" +
                    "",
            connectionProps
        )

        val stmt: Statement?
        var resultset: ResultSet?

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SHOW DATABASES;")

            if (stmt.execute("SHOW DATABASES;")) {
                resultset = stmt.resultSet
            }

            while (resultset!!.next()) {
                println(resultset.getString("Database"))
            }
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        }
    } catch (ex: SQLException) {
        // handle any errors
        ex.printStackTrace()
    } catch (ex: Exception) {
        // handle any errors
        ex.printStackTrace()
    }
}