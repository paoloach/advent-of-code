import java.io.File
import java.lang.Integer.min
import java.util.*

data class DataPacket(val num:Int?=null, val dataList: List<DataPacket> ?=null ){
    fun isNumber() = num != null
}

enum class CompareValue {
    RIGHT,
    LEFT,
    SAME
}


fun isRightOrder(left: List<DataPacket>, right: List<DataPacket>) : CompareValue{
    val index=0
    val minIndex = min(left.size, right.size)
    for (index in 0 .. minIndex-1){
        val leftDataPacket = left[index]
        val rightDataPacket = right[index]
        if (leftDataPacket.isNumber() && rightDataPacket.isNumber()){
            if (leftDataPacket.num!! < rightDataPacket.num!!)
                return CompareValue.RIGHT
            if (leftDataPacket.num!! > rightDataPacket.num!!)
                return  CompareValue.LEFT
            continue
        }
        val leftList = if (leftDataPacket.isNumber())
            listOf(leftDataPacket)
        else
            leftDataPacket.dataList!!

        val rightList = if (rightDataPacket.isNumber())
            listOf(rightDataPacket)
        else
            rightDataPacket .dataList!!
        val compareValue = isRightOrder(leftList, rightList)
        if (compareValue != CompareValue.SAME)
            return compareValue
    }
    if(left.size < right.size)
        return CompareValue.RIGHT
    if (left.size > right.size)
        return CompareValue.LEFT
    return CompareValue.SAME
}


data class Couple(val left: List<DataPacket>, val rigth: List<DataPacket>){
    fun isRightOrder() = isRightOrder(left, rigth) == CompareValue.RIGHT

}

fun List<String>.step1():Int {
    var couples = mutableListOf<Couple>()
    for(index in 0 .. size-1 step 3){
        val firstLine = get(index)
        val secondLine = get(index+1)
        val couple = generateCouple(firstLine, secondLine)
        couples.add(couple)
    }
    var sum=0
    for( index in 0 .. couples.size-1){
        if (couples[index].isRightOrder()){
            sum += (index+1)
        }
    }
    return sum
}

fun decodeLine(line: String): List<DataPacket> {
    val stackListDataPacket = Stack<MutableList<DataPacket>>()
    var currentList = mutableListOf<DataPacket>()
    var number = ""
    line.forEach {
        when(it){
            '[' -> {
                stackListDataPacket.push(currentList)
                currentList = mutableListOf<DataPacket>()
                }
            ']' -> {
                if (number.isNotEmpty()){
                    currentList.add(DataPacket(num = number.toInt()))
                    number=""
                }
                val oldList = stackListDataPacket.pop()
                oldList.add(DataPacket(dataList=currentList))
                currentList = oldList
                }
            '0','1','2','3','4','5','6','7','8','9'-> {
                number += it
                }
            ',' -> {
                if (number.isNotEmpty()) {
                    currentList.add(DataPacket(num = number.toInt()))
                    number = ""
                }
            }

        }
    }
    return currentList
}

fun generateCouple(firstLine: String, secondLine: String): Couple {
    val firstRow = decodeLine(firstLine)
    val secondRow = decodeLine(secondLine)
    return Couple(firstRow, secondRow)
}

val dataListComparator = Comparator<List<DataPacket>> { listA, listB ->
    when(isRightOrder(listA!!, listB!!)){
        CompareValue.RIGHT -> -1
        CompareValue.LEFT -> 1
        CompareValue.SAME -> 0
    }
}

fun List<String>.step2():Int {
    var couples = mutableListOf<Couple>()
    for(index in 0 .. size-1 step 3){
        val firstLine = get(index)
        val secondLine = get(index+1)
        val couple = generateCouple(firstLine, secondLine)
        couples.add(couple)
    }
    val rightPackets = couples
        .flatMap { mutableListOf(it.left, it.rigth) }

        .toMutableList()
    val firstDivider = listOf(DataPacket(dataList = listOf(DataPacket(dataList = listOf(DataPacket(num=2))))))
    val secondDivider =  listOf(DataPacket(dataList = listOf(DataPacket(dataList = listOf(DataPacket(num=6))))))
    rightPackets.add(firstDivider)
    rightPackets.add(secondDivider)

    val sortedPackets = rightPackets.sortedWith(dataListComparator)

    return (sortedPackets.indexOf(firstDivider)+1)*(sortedPackets.indexOf(secondDivider)+1)

}

fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
   println("step1: $step1")
    println("step2: $step2")
}







