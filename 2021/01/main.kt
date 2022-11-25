import java.io.File



fun main(args: Array<String>) {
    var tmp=Int.MAX_VALUE
    val fileName="input.txt"
    val data = File(fileName)
        .readLines()
        .map { it.toInt() }

    val count = data.map {
            val sup = it < tmp
            tmp = it;
            sup}
        .count { !it }
    println(count)

    val aggregate = mutableListOf<Int>()
    for( index in 0 .. data.size-3){
        aggregate.add( data[index]+data[index+1]+data[index+2])
    }
    tmp = Int.MAX_VALUE
    val aggrCount = aggregate.map {
        val sup = it > tmp
        tmp = it
        if (sup) 1 else 0}
        .sum()
    println(aggrCount)



}