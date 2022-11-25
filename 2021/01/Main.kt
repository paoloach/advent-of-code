import java.io.File

fun main(args: Array<String>) {
    val fileName="input.txt"
    val data = File(fileName).readLines().map { it.toInt() }
    var sum = 0
    for ( i in 1 until data.size){
        if (data[i-1] < data[i])
            sum++;
    }
    println(sum)

}