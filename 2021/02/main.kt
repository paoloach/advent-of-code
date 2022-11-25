import java.io.File

data class Pos(var horiz:Int, var depth:Int)

fun main(args: Array<String>) {
    var pos = Pos(0,0)
    val fileName="input.txt"
    val data = File(fileName)
        .readLines()
        .fold(pos) { acc, line ->
                val cmd = line.split(" ")
                val amount = cmd[1].toInt()
                val oper = cmd[0]
                when (oper) {
                    "forward" -> Pos(acc.horiz + amount, acc.depth)
                    "down" -> Pos(acc.horiz, acc.depth + amount)
                    "up" -> Pos(acc.horiz, acc.depth - amount)
                    else -> acc
                }

        }
    println(data)
    println(data.horiz*data.depth)

}