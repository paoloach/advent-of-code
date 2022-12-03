import java.io.File



fun main(args: Array<String>) {
    step1()
    step2()
}


fun step1(){

    val fileName="input.txt"
    val total = File(fileName)
        .readLines()
        .map { it.chunked(it.length / 2) }
        .flatMap { it[0].toCharArray().intersect(it[1].toList().toSet()) }
        .sumOf { convertPriority(it) }
    println("total  $total")
}

fun step2(){

    val fileName="input.txt"
    val total2 = File(fileName)
        .readLines()
        .chunked(3)
        .flatMap {
            it[0].toList().intersect(it[1].toList()).intersect(it[2].toList())
        }
        .sumOf { convertPriority(it) }
    println("total2  $total2")
}

fun convertPriority(it: Char):Int {
    if (it.isLowerCase()){
        return it.code-'a'.code+1
    } else {
        return it.code-'A'.code+27
    }
}

