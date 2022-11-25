import java.io.File

data class OnesZeros(val ones: List<String>, val zeros: List<String>){
    fun getMostCommon()  = if (ones.size >= zeros.size)
                              ones
                            else
                                zeros
    fun getLeastCommon()  = if (ones.size < zeros.size)
                                ones
                            else
                                zeros
}

/*
fun List<String>.onesZeros(bitIndex: Int):OnesZeros{
    val ones = filter { it[bitIndex] == '1' }.count()
    val zeros = size - ones
    return OnesZeros(ones, zeros)
}
*/

fun List<String>.splitByBit(bitIndex:Int) = OnesZeros(filter { it[bitIndex] == '1' }, filter { it[bitIndex] == '0' } )


fun main(args: Array<String>) {
    val fileName="input.txt"
    val data = File(fileName).readLines()
    val bitsCount = data[0].length
    var bitsCounter = listOf<Int>()
    data.forEach {
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

    val splitted = data.splitByBit(0)
    var oxigen =splitted.getMostCommon()
    var co2 = splitted.getLeastCommon();

    for (bitIndex in 1 .. bitsCount){
        if (oxigen.size > 1){
            val oxigenSplit = oxigen.splitByBit(bitIndex)
            oxigen = oxigenSplit.getMostCommon()
        }
        if (co2.size > 1){
            val co2Splitted = co2.splitByBit(bitIndex)
            co2 = co2Splitted.getLeastCommon()
        }
    }

    var oxigenGenerator = oxigen[0].toInt(2)
    var co2Generator = co2[0].toInt(2)

    println(oxigenGenerator*co2Generator)



}