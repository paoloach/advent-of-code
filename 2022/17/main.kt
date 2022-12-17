import java.io.File
import java.util.*
import kotlin.collections.HashSet


const val iterationTot = 1000000000000

const val  printStep  = 1000000L
const val wallWide = 7

enum class Status {
    INIT,
    SEARCH,
    END
}

fun List<Pos>.moveLeft(fallenRocks: Set<Pos>) {
    if (firstOrNull { it.x == 1 || fallenRocks.contains(it.left())} != null)
        return
    forEach { it.x -- }
    return
}

fun List<Pos>.moveRight(fallenRocks: Set<Pos>) {
    if (firstOrNull { it.x == wallWide || fallenRocks.contains(it.right())} != null)
        return
    forEach { it.x++}
    return
}

fun List<Pos>.top():Int {
    return maxOf { it.y }
}

fun List<Pos>.moveDown() {
    forEach { it.y-- }
}

fun List<Pos>.canGoDown(fallenRocks: Set<Pos>):Boolean{
    return firstOrNull { fallenRocks.contains(it.up()) || it.y == 1 } == null
}

fun List<Pos>.moveTo(pos:Pos ){
    forEach {
        it.x += pos.x
        it.y += pos.y
    }
}

fun MutableSet<Pos>.removeHiddenLayer(): HashSet<Pos> {
    val result = HashSet<Pos>()
    val minYRow = mutableListOf(0,0,0,0,0,0,0)
    forEach {
        if (it.y > minYRow[it.x.toInt()-1] ){
            minYRow[it.x.toInt()-1] = it.y
        }
    }
    val minY = minYRow.min()

    forEach {
        if (it.y >= minY)
            result.add(Pos(it.x, it.y-minY))
    }
    return result

}


fun MutableSet<Pos>.print() {

    val max = maxOf{it.y}
    println("|.......|")
    for(y in max downTo 1){
        val points = filter { it.y == y }
        val line = (1 .. 7).map {x-> points.firstOrNull { it.x ==x } ?.let { '#'} ?: '.' }.joinToString("")
        println("|$line|")
    }
    println("#########")
    println("")
}


fun List<String>.step1():Int {
    val jetsDir = get(0)

    val rock1 = listOf(Pos(0,0), Pos(1,0), Pos(2,0), Pos(3,0))
    val rock2 = listOf(Pos(1,0), Pos(0,1), Pos(1,1), Pos(2,1), Pos(1,2))
    val rock3 = listOf(Pos(0,0), Pos(1,0), Pos(2,0), Pos(2,1), Pos(2,2))
    val rock4 = listOf(Pos(0,0), Pos(0,1), Pos(0,2), Pos(0, 3))
    val rock5 = listOf(Pos(0,0), Pos(1,0), Pos(0,1), Pos(1,1))

    val rocks = listOf( rock1, rock2, rock3, rock4, rock5)
    var rockIndex=0


    var level =1
    var jetIndex=0
    val fallenRocks = HashSet<Pos>(10000)
    for(rockCounter in 1 .. 2022) {
        val lrPos = Pos(3, level + 3)
        val fallingRock = rocks[rockIndex].map { it.clone() }.toList()
        fallingRock.moveTo(lrPos)
        while(true){
            when(jetsDir[jetIndex]){
                '<' -> fallingRock.moveLeft(fallenRocks)
                '>' -> fallingRock.moveRight(fallenRocks)
            }
            jetIndex++
            if (jetIndex == jetsDir.length)
                jetIndex=0
            if (!fallingRock.canGoDown(fallenRocks))
                break
            fallingRock.moveDown()

        }
        fallenRocks.addAll(fallingRock)
        level = fallenRocks.maxOf { it.y }+1
        rockIndex++
        if (rockIndex == rocks.size)
            rockIndex=0
        if (rockCounter % printStep == 0L) {
            fallenRocks.print()
            println()
        }


    }
    fallenRocks.print()
    println()

    return fallenRocks.maxOf { it.y }
}

val nearRightBorder   = "00000001".toInt(2)
val nearLeftBorder    = "01000000".toInt(2)


data class Rock( var def:MutableList<Int>){

    fun clone()  =  Rock(def.toMutableList())

    fun moveLeft(fallenRocks: MutableList<Int>, down:Int){
        val sum = def.map { it.and(nearLeftBorder) }.sum()
        if ( sum != 0)
            return
        val newDef = def.map { it.shl(1) }.toMutableList()
        val andFallenRock = newDef.mapIndexed { index, it ->
            val fallenRock = if (down-index > 0){
                fallenRocks[fallenRocks.size-down+index]
            } else {
                0x00
            }
            it.and(fallenRock)
        }.sum()
        if (andFallenRock == 0){
            def = newDef
        }

    }

    fun moveRight(fallenRocks: MutableList<Int>, down:Int){
        val sum = def.map { it.and(nearRightBorder) }.sum()
        if (sum != 0)
            return

        val newDef = def.map { it.shr(1) }.toMutableList()
        val andFallenRock = newDef.mapIndexed { index, it ->
            val fallenRock = if (down-index > 0){
                fallenRocks[fallenRocks.size-down+index]
            } else {
                0x00
            }
            it.and(fallenRock)
        }.sum()
        if (andFallenRock == 0){
            def = newDef
        }

    }

    fun move(dir:Char, fallenRocks: MutableList<Int> = mutableListOf<Int>(), down:Int=0){
        when(dir) {
            '>' -> moveRight(fallenRocks,down)
            '<' -> moveLeft(fallenRocks,down)
        }
    }

    fun move(jets:String) : Rock{
        jets.forEach { move(it)}
        return this
    }

    fun getBottom()  = def[0]

    fun canGoDown(fallenRocks: MutableList<Int>, down:Int):Boolean {
        for( i in 0 .. def.size-1){
            if (down-i >= 0){
                if (def[i].and(fallenRocks[fallenRocks.size-down+i-1]) != 0)
                    return false
            } else {
                return true
            }
        }
        return true
    }
}


fun Int.toCaveLine():String {
    var line = "|"
    if (and(0x40) != 0){
        line += "#"
    } else {
        line += "."
    }
    if (and(0x20) != 0){
        line += "#"
    } else {
        line += "."
    }
    if (and(0x10) != 0){
        line += "#"
    } else {
        line += "."
    }
    if (and(0x8) != 0){
        line += "#"
    } else {
        line += "."
    }
    if (and(0x4) != 0){
        line += "#"
    } else {
        line += "."
    }
    if (and(0x2) != 0){
        line += "#"
    } else {
        line += "."
    }
    if (and(0x1) != 0){
        line += "#"
    } else {
        line += "."
    }
    line += "|"
    return line
}

data class Key(val conf:List<Int>, val indexJ:Int, val indexRock:Int)

data class Value(val rockCounter:Long, val height:Int)


fun List<String>.step2():Long {
    val jetsDir = get(0)
    var status = Status.INIT
    var searchingConf = listOf<Int>()
    var searchingJetIndex = 0
    var searchingRockIndex=0

    val rock1 = Rock(listOf("00011110").map { it.toInt(2) }.toMutableList())
    val rock2 = Rock(listOf("00001000","00011100","00001000").map { it.toInt(2) }.toMutableList())
    val rock3 = Rock(listOf("00011100","00000100","00000100").map { it.toInt(2) }.toMutableList())
    val rock4 = Rock(listOf("00010000","00010000","00010000", "00010000").map { it.toInt(2) }.toMutableList())
    val rock5 = Rock(listOf("00011000","00011000").map { it.toInt(2) }.toMutableList())
    val rocks = listOf(rock1, rock2, rock3, rock4, rock5)

    val initialMoves = listOf("<<<<",">>>>",  "<<<>","<<><","<><<","><<<", "<>>>","><>>",">><>",">>><",  "<<>>","><<>","<>><",">><<","<><>","><><")

    val possibleStartingPos = rocks.map { rock ->
        initialMoves.map { it to  rock.clone().move(it) }.toMap()
    }

    var rockIndex = 0


    var setConf = mutableMapOf<Key, Value>()
    var jetIndex = 0
    var fallenRocks = mutableListOf<Int>()
    fallenRocks.add("1111111".toInt(2))
    val start = Date().toInstant().epochSecond
    val iterations = iterationTot //1000000000000L
    var lineRemoved=0L
    var rockCounter=0L
    while (rockCounter < iterationTot){
        rockCounter++
        var startJets = ""
        (1 .. 4).forEach {
            startJets += jetsDir[jetIndex]
            jetIndex++;
            if (jetIndex >= jetsDir.length)
                jetIndex=0
        }

        val fallingRock = possibleStartingPos[rockIndex][startJets]!!.clone()

        var down = 0

        while (true) {
            if (!fallingRock.canGoDown(fallenRocks, down))
                break;
            down++
            fallingRock.move(jetsDir[jetIndex], fallenRocks, down)
            jetIndex++
            if (jetIndex == jetsDir.length)
                jetIndex = 0
        }

        fallingRock.def.forEachIndexed { index, value ->
            if (down > index ){
                fallenRocks[fallenRocks.size-down+index] = fallenRocks[fallenRocks.size-down+index].or(value)
            } else {
                fallenRocks.add(value)
            }
        }
        rockIndex++;
        if (rockIndex >= 5)
            rockIndex=0

        var fullLine = -1
        var line=0
        for (index in fallenRocks.size-1 downTo 0){
           line = line.or(fallenRocks[index])
           if (line == 0x7F){
               fullLine = index
               break;
           }
        }


        if (status == Status.INIT && fullLine > 0){
            val conf = fallenRocks.subList(fullLine, fallenRocks.size).toList()
            val key = Key(conf, jetIndex, rockIndex)
            if (setConf.contains(key)){
                val lastCounter = setConf[key]!!
                val deltaRockCounter = rockCounter-lastCounter.rockCounter
                val deltaHeight = fallenRocks.size-lastCounter.height
                println("In $deltaRockCounter rocks the queue raise of $deltaHeight ")
                val remainCounter = iterationTot-rockCounter
                val completeCycle = remainCounter/deltaRockCounter
                lineRemoved = completeCycle*deltaHeight
                val restartFrom = completeCycle*deltaRockCounter
                rockCounter += restartFrom

                println("remain complete cycle: $completeCycle")
                println("jump for ${completeCycle*deltaRockCounter}")
                println("jump at $rockCounter")
                println("raise the queue of $lineRemoved ")
                println("actual queue eight: ${fallenRocks.size}")
                status = Status.END
            } else {
                setConf[key]=Value(rockCounter, fallenRocks.size)

            }
        }



    }
    println("final queue eight: ${fallenRocks.size}")
    return fallenRocks.size.toLong()-1L+lineRemoved

}


fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
    println("step1: $step1")
    println("step2: $step2")
}







