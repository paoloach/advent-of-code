import java.io.File

fun List<String>.step1():Int {
    return map{it.split(",")}
        .map{ElvesPair(convertToRange(it[0]), convertToRange(it[1]))}
        .filter { it.contained() }
        .size
}

fun List<String>.step2():Int {
    return map{it.split(",")}
        .map{ElvesPair(convertToRange(it[0]), convertToRange(it[1]))}
        .filter { it.overlap() }
        .size
}

data class Range(val min:Int, val max: Int)

data class ElvesPair(val first:Range, val second:Range){
    fun contained() = (first.min <=  second.min && second.max <= first.max)  || (second.min <=  first.min && first.max <= second.max)
    fun overlap() = (first.min <=  second.min && second.min <= first.max) ||
            (first.min <=  second.max && second.max <= first.max) ||
            (second.min <=  first.min && first.min <= second.max)  ||
            (second.min <=  first.max && first.max <= second.max)

}

fun convertToRange(str: String):Range{
    val values = str.split("-")
    return Range(values[0].toInt(), values[1].toInt())
}

fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
    println("step1: $step1")
    println("step2: $step2")
}






