fun main(args: Array<String>) {

    val nbvalues : List<Int> =   listOf(4, 5)
    val rangevalue : List<Int> = listOf(5, 9)
    val nbloop : List<Int> =     listOf(20, 12, 10)

    try {
        println(selectLevel(nbvalues, rangevalue, nbloop))
        val config = config(nbvalues, rangevalue, nbloop)
        val toFind = init(config["values"]!!, config["range"]!!)
        println(toFind)// ---> a mettre en comment

        var exit = false
        var i = 0
        do {
            val choice = userAction(config["values"]!!, config["range"]!!)
            println(choice)// ---> a mettre en comment

            val result = search(choice, toFind, config["values"]!!)
            println(result)// ---> a mettre en comment

            val display = displayResult(result)
            var total : Int = 0

            for (i in result.indices) {
                total += result[i]!!
            }

            if (total == config["values"]) {
                exit = true
            }

            println(display)
            i++
        } while (!exit && i < config["loop"]!!)

        println("Quitté")


    } catch (e : VerifyError) { println(e.message) }
      catch (e : IllegalArgumentException) { println(e.message) }

}

fun displayResult(result: MutableList<Int?>): String {
    var nbOk = 0
    var nbKo = 0
    for (i in result.indices) {
        if(result[i] == 0) { nbKo += 1 }
        else if(result[i] == 1) { nbOk += 1 }
    }

    var str : String = """
        nombre de de bien placé : $nbOk
        nombre de mal placé : $nbKo
    """.trimIndent()

    return str
}

fun searchExact(v: Int, k: Int, list: List<Int>, result: MutableList<Int?>) : MutableList<Int?> {
    if(list[k] == v) { result[k] = 1 }
    return result
}

fun searchFalse (v: Int, k: Int, list: List<Int>, result: MutableList<Int?>) : MutableList<Int?> {
    if (list.indexOf(v) == -1) { result[k] = -1 }
    return result
}

fun search(insertList : List<Int>, toFind : List<Int>, nbValues: Int) : MutableList<Int?> {
    var result : MutableList<Int?> = mutableListOf()
    var i = 0
    while (i < nbValues) {
        result.add(0)
        i++
    }

    for (i in insertList.indices) {
       result = searchExact(insertList[i], i, toFind, result)
       result = searchFalse(insertList[i], i, toFind, result)
    }

    return result
}

fun init(nbValues: Int, rangeValue: Int) : List<Int> {
    var a = mutableListOf<Int>()
    var i = 0
    while (i < nbValues!!) {
        a.add((0..rangeValue!!).random())
        i++
    }

    return a.toList()
}

fun selectLevel(nbValues : List<Int>, rangeValue : List<Int>, nbLoop : List<Int>): String {
    return """Sélectionner un niveau:
         - 1 : Facile ->    ${nbValues[0]} valeurs, ${nbLoop[0]} essais. Les valeurs sont limité entre 0 et ${rangeValue[0]}.
         - 2 : Normal ->    ${nbValues[0]} valeurs, ${nbLoop[1]} essais.
         - 3 : Difficile -> ${nbValues[1]} valeurs, ${nbLoop[2]} essais.
    """.trimMargin()
}

fun config(nbValues : List<Int>, rangeValue : List<Int>, nbLoop : List<Int>) : Map<String, Int> {
    var config : MutableMap<String, Int> = mutableMapOf()

    var levelUser : Int? = readLine()?.toIntOrNull()
    if (levelUser != null && levelUser in 1..3) {
        if (levelUser == 1) {
            config["values"] =    nbValues[0]
            config["range"] =     rangeValue[0]
            config["loop"] =      nbLoop[0]
        } else if(levelUser == 2) {
            config["values"] =    nbValues[0]
            config["range"] =     rangeValue[1]
            config["loop"] =      nbLoop[1]
        } else if(levelUser == 3) {
            config["values"] =    nbValues[1]
            config["range"] =     rangeValue[1]
            config["loop"] =      nbLoop[1]
        } else { throw VerifyError("erreur de choix entre 1 - 3") }
    } else { throw VerifyError("erreur de choix") }
    return config.toMap()
}

fun userAction(nbValues: Int, range: Int) : List<Int> {
    println("Inserer une ligne de $nbValues chiffres")
    var insert = readLine()!!.trim()
    var insertList : List<String> = insert.split("[ \\-_/]+".toRegex())

    if (insertList.size != nbValues) {
        throw IllegalArgumentException ("le nombre d'entrée est pas égale à $nbValues") }

    insertList.map { if(it.toDouble() > range.toDouble()) {
        throw IllegalArgumentException ("valeur trop élevée, pas de valeur supérieur à $range") }  }

    return insertList.map{ it.toInt() }
}


