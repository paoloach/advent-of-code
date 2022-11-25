import java.io.File



fun main(args: Array<String>) {
    var tmp=Int.MAX_VALUE
    val fileName="input.txt"
    val data = File(fileName)
        .readLines()
        .map { it.toInt() }
        .map {
            val sup = it < tmp
            tmp = it;
            sup}
        .count { !it }
    println(data)

}