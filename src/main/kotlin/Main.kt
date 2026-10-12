val categorias = arrayOf("Comida", "Transporte", "Estudios", "Ocio", "Otros")

fun leerMonto(): Double {
    while (true) {
        print("Monto: ")
        val monto = readln().toDoubleOrNull()
        if (monto != null && monto > 0) return monto
        println("Monto invalido, intenta de nuevo")
    }
}

fun elegirCategoria(): String {
    while (true) {
        for (i in categorias.indices) {
            println("${i + 1}. ${categorias[i]}")
        }
        print("Elige una categoria: ")
        val opcion = readln().toIntOrNull()
        if (opcion != null && opcion in 1..categorias.size) return categorias[opcion - 1]
        println("Opcion invalida, intenta de nuevo")
    }
}

fun main() {
    java.io.File("data").mkdirs()
    println("===== CONTROL DE GASTOS CON BASE DE DATOS =====")
    println("1. Conexion JDBC con sentencias SQL")
    println("2. ORM con Exposed")
    print("Elige como conectarte ")

    when (readln()) {
        "1" -> programaSql()
        "2" -> programaOrm()
        else -> println("Opcion no valida")
    }
}