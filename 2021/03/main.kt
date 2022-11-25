import java.io.File

data class Pos(var horiz:Int, var depth:Int)

fun main(args: Array<String>) {
    var pos = Pos(0,0)
    val fileName="input.txt"
    var bitsCounter = listOf<Int>()
    File(fileName)
        .forEachLine {
            var lineBitCounter = mutableListOf<Int>()
            it.forEach { bit->
                if (bit=='1')
                    lineBitCounter.add(1)
                else
                    lineBitCounter.add(-1)
            }
            bitsCounter = lineBitCounter.mapIndexed { index, bitVal ->
                if (bitsCounter.size > index)
                    bitVal + bitsCounter[index]
                else
                    bitVal
            }
        }

   val gamma = bitsCounter.fold(""){acc, bit ->
       if (bit > 0 )
           acc+'1'
       else
           acc+'0'
    }.toInt(2)
    val epsilon = bitsCounter.fold(""){acc, bit ->
        if (bit < 0 )
            acc+'1'
        else
            acc+'0'
    }.toInt(2)

    println(gamma*epsilon)

}