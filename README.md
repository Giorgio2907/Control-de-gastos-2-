# Control de Gastos Personales con SQLite en Kotlin

Programa de terminal escrito en Kotlin que registra gastos personales y los guarda en una base de datos SQLite, así que los datos se conservan al cerrar el programa. Es la ampliación del primer laboratorio, donde los gastos vivían en listas y se perdían al salir. Esta versión conecta con la base de datos de dos formas, con JDBC y sentencias SQL, y con el ORM Exposed.

## Qué hace hoy

- Al iniciar permite elegir cómo conectarse a la base de datos, JDBC con SQL o ORM con Exposed.
- Pide el presupuesto del mes la primera vez y lo guarda, así que después ya no lo vuelve a preguntar.
- Agrega gastos con descripción, monto y categoría. Valida que el monto sea un número mayor a 0 y que la categoría exista.
- Muestra la lista de gastos con su ID.
- Calcula el total gastado y dice cuánto queda del presupuesto o cuánto se excedió.
- Suma los gastos por categoría, Comida, Transporte, Estudios, Ocio y Otros.
- Modifica el monto de un gasto y elimina un gasto usando su ID.

## Cómo correrlo

Necesitas IntelliJ IDEA, un JDK instalado e internet la primera vez, porque Gradle descarga las librerías.

1. Abre la carpeta del proyecto con File > Open, o clónalo con File > New > Project from Version Control.
2. Espera a que Gradle termine de importar el proyecto.
3. Abre `src/main/kotlin/Main.kt` y pulsa el triángulo verde junto a `fun main()`.

La base de datos se crea sola en `data/gastos.db`. Se puede abrir con DB Browser for SQLite para ver las tablas `gastos` y `presupuesto`.

## Estructura del proyecto

- `Main.kt` tiene el array de categorías, las funciones que validan datos y el `main`, que elige entre JDBC y ORM.
- `GastosJdbc.kt` tiene la versión con conexión estándar JDBC y sentencias SQL.
- `GastosExposed.kt` tiene la versión con el ORM Exposed.
- `build.gradle.kts` declara las dependencias, `sqlite-jdbc` y las librerías de Exposed.

## Ejemplo de salida

Con JDBC, un presupuesto de 500 y tres gastos de prueba, un almuerzo de 15, un taxi de 20 y un libro de 45.5.

```
Aun no tienes presupuesto guardado. Monto: 500
Presupuesto del mes: 500.0

----- LISTA DE GASTOS -----
1. Almuerzo - 15.0 - Comida
2. Taxi - 20.0 - Transporte
3. Libro - 45.5 - Estudios

----- RESUMEN -----
Total gastado 80.5
Te quedan 419.5 del presupuesto

----- POR CATEGORIA -----
Comida 15.0
Transporte 20.0
Estudios 45.5
Ocio 0.0
Otros 0.0
```

## Qué conceptos practica

- Conexión a SQLite con JDBC, usando `DriverManager`, `createStatement`, `executeUpdate` y `executeQuery`.
- Sentencias SQL `Create Table`, `Insert`, `Select`, `Update`, `Delete` y la función `Sum`.
- Lectura de resultados con `ResultSet` y `while (rs.next())`.
- ORM con Exposed, con tablas definidas como `object`, bloques `transaction { }` y las funciones `insert`, `selectAll`, `update` y `deleteWhere`.
- Filtros con `where` y el operador `eq`, y orden con `orderBy`.
- Dependencias con Gradle.
- Variables, listas, arrays, funciones, `when` y manejo de nulos con `toIntOrNull()` y `toDoubleOrNull()`, como en el primer laboratorio.

## Limitaciones actuales

- Los valores se escriben dentro del texto de la sentencia SQL en la versión JDBC. Solo se protege el caso de las comillas simples. Una mejora sería usar `PreparedStatement`.
- No se valida que la descripción esté vacía.
- Los gastos no tienen fecha, así que no se pueden separar por meses.
- El presupuesto no se puede cambiar desde el menú.

## Hacia dónde quiere crecer

Estas son ideas para las siguientes etapas y todavía no están hechas.

1. Agregar una tabla de categorías relacionada con la de gastos.
2. Usar Room, el ORM de Android, en lugar de Exposed.
3. Agregar fecha a cada gasto y presupuesto por mes.
4. Pasar a una app Android con lista, formulario y gráficos.

## Nota sobre los datos

Este repositorio es público, así que los ejemplos usan gastos ficticios. La base de datos `data/gastos.db` no se sube al repositorio, se crea al ejecutar el programa.
