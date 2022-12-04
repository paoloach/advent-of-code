import java.io.File



fun main(args: Array<String>) {
    step1()
    step2()
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

fun step1(){

    val fileName="input.txt"
    val total = File(fileName)
        .readLines()
        .map{it.split(",")}
        .map{ElvesPair(convertToRange(it[0]), convertToRange(it[1]))}
        .filter {
            it.contained()
        }
        .size

    println("Total completed overlapped pairs: $total")


}

fun step2(){


    val fileName="input.txt"
    val total = File(fileName)
        .readLines()
        .map{it.split(",")}
        .map{ElvesPair(convertToRange(it[0]), convertToRange(it[1]))}
        .filter {
            it.overlap()
        }
        .size

    println("Total overlapped pairs: $total")

}


