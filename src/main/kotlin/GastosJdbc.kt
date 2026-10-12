import java.sql.DriverManager

fun programaSql() {
    val url = "jdbc:sqlite:data/gastos.db"
    val connection = DriverManager.getConnection(url)
    val stmt = connection.createStatement()

    stmt.executeUpdate("Create Table If Not Exists gastos (id Integer Primary Key, descripcion TEXT, monto REAL, categoria TEXT)")
    stmt.executeUpdate("Create Table If Not Exists presupuesto (id Integer Primary Key, monto REAL)")

    var presupuesto = 0.0
    val rsPresupuesto = stmt.executeQuery("Select monto From presupuesto Where id = 1")
    if (rsPresupuesto.next()) presupuesto = rsPresupuesto.getDouble("monto")
    if (presupuesto == 0.0) {
        print("Aun no tienes presupuesto guardado. ")
        presupuesto = leerMonto()
        stmt.executeUpdate("Insert Into presupuesto (id, monto) Values (1, $presupuesto)")
    }
    println("Presupuesto del mes: $presupuesto")

    var salir = false
    while (!salir) {
        println("\n----- MENU -----")
        println("1. Agregar gasto")
        println("2. Ver gastos")
        println("3. Resumen")
        println("4. Gastos por categoria")
        println("5. Modificar monto de un gasto")
        println("6. Eliminar un gasto")
        println("0. Salir")
        print("Elige una opcion ")

        when (readln()) {
            "1" -> {
                print("Descripcion ")
                val descripcion = readln().replace("'", "''")
                val monto = leerMonto()
                val categoria = elegirCategoria()
                stmt.executeUpdate("Insert Into gastos (descripcion, monto, categoria) Values ('$descripcion', $monto, '$categoria')")
                println("Gasto guardado")
            }
            "2" -> {
                println("\n----- LISTA DE GASTOS -----")
                val rs = stmt.executeQuery("Select * From gastos")
                while (rs.next()) {
                    println("${rs.getInt("id")}. ${rs.getString("descripcion")} - ${rs.getDouble("monto")} - ${rs.getString("categoria")}")
                }
            }
            "3" -> {
                val rs = stmt.executeQuery("Select Sum(monto) As total From gastos")
                rs.next()
                val total = rs.getDouble("total")
                println("\n----- RESUMEN -----")
                println("Total gastado $total")
                if (total <= presupuesto) {
                    println("Te quedan ${presupuesto - total} del presupuesto")
                } else {
                    println("Te pasaste del presupuesto por ${total - presupuesto}")
                }
            }
            "4" -> {
                println("\n----- POR CATEGORIA -----")
                for (cat in categorias) {
                    val rs = stmt.executeQuery("Select Sum(monto) As total From gastos Where categoria = '$cat'")
                    rs.next()
                    println("$cat ${rs.getDouble("total")}")
                }
            }
            "5" -> {
                print("ID del gasto a modificar ")
                val idGasto = readln().toIntOrNull()
                if (idGasto == null) {
                    println("ID invalido")
                } else {
                    val nuevoMonto = leerMonto()
                    val filas = stmt.executeUpdate("Update gastos Set monto = $nuevoMonto Where id = $idGasto")
                    if (filas > 0) println("Gasto actualizado") else println("No existe un gasto con ese ID")
                }
            }
            "6" -> {
                print("ID del gasto a eliminar ")
                val idGasto = readln().toIntOrNull()
                if (idGasto == null) {
                    println("ID invalido")
                } else {
                    val filas = stmt.executeUpdate("Delete From gastos Where id = $idGasto")
                    if (filas > 0) println("Gasto eliminado") else println("No existe un gasto con ese ID")
                }
            }
            "0" -> {
                salir = true
                println("Hasta luego")
            }
            else -> println("Opcion no valida")
        }
    }
    connection.close()
}