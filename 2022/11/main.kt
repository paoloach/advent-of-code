import java.io.File
import kotlin.math.sqrt

enum class Oper {POWER2, PLUS, MULT}

class Monkey(val items: MutableList<Int>, val oper:Oper, val operFactor: Int, val testDivision: Int, val monkeyIfTrue: Int, val monkeyIfFalse: Int) {
    var activityStep1 = 0
    var inspectedItemsStep2 = 0
    fun hisTurn(monketList: MutableList<Monkey>) {
        activityStep1 += items.size
        items.forEach {
            var level = when(oper){
                Oper.MULT -> it*operFactor
                Oper.PLUS -> it+operFactor
                Oper.POWER2 -> it*it
            }
            level = level / 3
            val destMonkey = if (level % testDivision == 0){
                 monketList[monkeyIfTrue]
            } else {
                monketList[monkeyIfFalse]
            }
            destMonkey.items.add(level)
        }
        items.clear()
    }

    fun hisTurn2(monketList: MutableList<Monkey>, module:Int) {


        inspectedItemsStep2 += items.size
        items.forEach {
            var level = when(oper){
                Oper.MULT ->{
                    val maxVal = Int.MAX_VALUE / operFactor
                    if (it < maxVal)
                        it*operFactor
                    else
                        it
                }
                Oper.PLUS -> it+operFactor
                Oper.POWER2 ->  {
                    val maxVal = sqrt(Int.MAX_VALUE.toDouble()).toInt()
                    if (it < maxVal)
                        it*it
                    else
                        it
                }
            }
            level = (level / 3) % module


            val destMonkey = if (level % testDivision == 0){
                monketList[monkeyIfTrue]
            } else {
                monketList[monkeyIfFalse]
            }
            destMonkey.items.add(level)
        }
        items.clear()
    }
}


fun List<String>.step1():Int {
    var monkeyList = parseFile()
    (0 .. 19).forEach {
        monkeyList.forEach {
            it.hisTurn(monkeyList)
        }

    }

    val sorted = monkeyList.sortedBy { it.activityStep1 }.reversed()


    return sorted[0].activityStep1* sorted[1].activityStep1 ;
}

private fun List<String>.parseFile() : MutableList<Monkey>{
    val monkeyList = mutableListOf<Monkey>()
    var currentStartingList = mutableListOf<Int>()
    var currentOper: Oper = Oper.PLUS
    var currentOperFactor: Int = 0
    var currentTestDivision: Int = 0
    var currentMonkeyIfTrue: Int = 0
    var currentMonkeyIfFalse: Int = 0
    var firstMonkey=true

    forEach {
        val line = it.trim()
        if (it.startsWith("Monkey ")) {
            if (firstMonkey == false) {
                monkeyList.add(
                    Monkey(
                        currentStartingList,
                        currentOper,
                        currentOperFactor,
                        currentTestDivision,
                        currentMonkeyIfTrue,
                        currentMonkeyIfFalse
                    )
                )
                currentStartingList = mutableListOf()
            }
            firstMonkey = false;

        }
        if (line.startsWith("Starting items: ")) {
            val list = line.substring("Starting items: ".length)
            list.split(",").forEach {
                currentStartingList.add(it.trim().toInt())
            }
        }
        if (line.startsWith("Operation: new = old ")) {
            val oper = line.substring("Operation: new = old ".length)
            if (oper == "* old")
                currentOper = Oper.POWER2
            else if (oper[0] == '+') {
                currentOper = Oper.PLUS
                currentOperFactor = oper.substring(1).trim().toInt()
            } else if (oper[0] == '*') {
                currentOper = Oper.MULT
                currentOperFactor = oper.substring(1).trim().toInt()
            } else {
                println("Invalid operarion string: $oper")
            }
        }
        if (line.trim().startsWith("Test: divisible by ")) {
            currentTestDivision = line.substring("Test: divisible by ".length).trim().toInt()
        }
        if (line.startsWith("If true: throw to monkey ")) {
            currentMonkeyIfTrue = line.substring("If true: throw to monkey ".length).trim().toInt()
        }
        if (line.startsWith("If false: throw to monkey ")) {
            currentMonkeyIfFalse = line.substring("If false: throw to monkey ".length).trim().toInt()
        }
    }
    monkeyList.add(
        Monkey(
            currentStartingList,
            currentOper,
            currentOperFactor,
            currentTestDivision,
            currentMonkeyIfTrue,
            currentMonkeyIfFalse
        )
    )
    return monkeyList
}


fun List<String>.step2():Int {
    var monkeyList = parseFile()

    val module = monkeyList.map { it.testDivision }.sum()

    val displayList = listOf(1, 19, 20, 21, 1000, 2000, 3000, 4000)
    (1 .. 10000).forEach {cycle->
        if (displayList.contains(cycle) ) {
            println("after round ${cycle }")
        }
        monkeyList.forEachIndexed { index,it->
            it.hisTurn2(monkeyList, module)
            if (displayList.contains(cycle)) {
                println("Monkey $index inspected items ${it.inspectedItemsStep2}")
            }
        }

    }

    val sorted = monkeyList.sortedBy { it.activityStep1 }.reversed()


    return sorted[0].activityStep1* sorted[1].activityStep1 ;
}

fun main(args: Array<String>) {
    val fileName="input.txt"
 //   val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
//    println("step1: $step1")
    println("step2: $step2")
}







