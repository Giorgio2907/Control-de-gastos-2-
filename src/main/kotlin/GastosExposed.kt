import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object TablaGastos : Table("gastos") {
    val id = integer("id").autoIncrement()
    val descripcion = varchar("descripcion", 100)
    val monto = double("monto")
    val categoria = varchar("categoria", 30)
    override val primaryKey = PrimaryKey(id)
}

object TablaPresupuesto : Table("presupuesto") {
    val id = integer("id")
    val monto = double("monto")
    override val primaryKey = PrimaryKey(id)
}

fun programaOrm() {
    Database.connect("jdbc:sqlite:data/gastos.db", driver = "org.sqlite.JDBC")

    transaction {
        SchemaUtils.create(TablaGastos, TablaPresupuesto)
    }

    var presupuesto = 0.0
    transaction {
        TablaPresupuesto.selectAll().where { TablaPresupuesto.id eq 1 }.forEach {
            presupuesto = it[TablaPresupuesto.monto]
        }
    }
    if (presupuesto == 0.0) {
        print("Aun no tienes presupuesto guardado. ")
        presupuesto = leerMonto()
        transaction {
            TablaPresupuesto.insert {
                it[TablaPresupuesto.id] = 1
                it[TablaPresupuesto.monto] = presupuesto
            }
        }
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
                val desc = readln()
                val valor = leerMonto()
                val cat = elegirCategoria()
                transaction {
                    TablaGastos.insert {
                        it[TablaGastos.descripcion] = desc
                        it[TablaGastos.monto] = valor
                        it[TablaGastos.categoria] = cat
                    }
                }
                println("Gasto guardado")
            }
            "2" -> {
                println("\n----- LISTA DE GASTOS -----")
                transaction {
                    TablaGastos.selectAll().orderBy(TablaGastos.id to SortOrder.ASC).forEach {
                        println("${it[TablaGastos.id]}. ${it[TablaGastos.descripcion]} - ${it[TablaGastos.monto]} - ${it[TablaGastos.categoria]}")
                    }
                }
            }
            "3" -> {
                var total = 0.0
                transaction {
                    TablaGastos.selectAll().forEach {
                        total += it[TablaGastos.monto]
                    }
                }
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
                for (nombre in categorias) {
                    var totalCategoria = 0.0
                    transaction {
                        TablaGastos.selectAll().where { TablaGastos.categoria eq nombre }.forEach {
                            totalCategoria += it[TablaGastos.monto]
                        }
                    }
                    println("$nombre $totalCategoria")
                }
            }
            "5" -> {
                print("ID del gasto a modificar ")
                val idGasto = readln().toIntOrNull()
                if (idGasto == null) {
                    println("ID invalido")
                } else {
                    val nuevoMonto = leerMonto()
                    val filas = transaction {
                        TablaGastos.update({ TablaGastos.id eq idGasto }) {
                            it[TablaGastos.monto] = nuevoMonto
                        }
                    }
                    if (filas > 0) println("Gasto actualizado") else println("No existe un gasto con ese ID")
                }
            }
            "6" -> {
                print("ID del gasto a eliminar ")
                val idGasto = readln().toIntOrNull()
                if (idGasto == null) {
                    println("ID invalido")
                } else {
                    val filas = transaction {
                        TablaGastos.deleteWhere { TablaGastos.id eq idGasto }
                    }
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
}