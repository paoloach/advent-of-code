import java.io.File
import java.util.*



fun List<String>.step1():String {
    val startingStackDraw = mutableListOf<String>()
    var stackIndex = ""
    val moves = mutableListOf<String>()

    forEach {
        if (!it.isEmpty()) {
            if (it.contains("[") ) {
                startingStackDraw.add(it)
            }
            if (it.startsWith(" 1")) {
                stackIndex = it
            }
            if (it.startsWith("move")) {
                moves.add(it)
            }
        }
    }

    val stackLen = calcStackLen(stackIndex)

    val stacks = createStacks(startingStackDraw, stackLen)

    applyMove(moves, stacks)

    val top =stacks.map { it.peek() }.fold(""){ acc, c->acc+c}

    return top
}

fun List<String>.step2():String {
    val startingStackDraw = mutableListOf<String>()
    var stackIndex = ""
    val moves = mutableListOf<String>()

    forEach {
        if (!it.isEmpty()) {
            if (it.contains("[") ) {
                startingStackDraw.add(it)
            }
            if (it.startsWith(" 1")) {
                stackIndex = it
            }
            if (it.startsWith("move")) {
                moves.add(it)
            }
        }
    }

    val stackLen = calcStackLen(stackIndex)

    val stacks = createStacks(startingStackDraw, stackLen)

    applyMove9001(moves, stacks)

    val top =stacks.map { it.peek() }.fold(""){ acc, c->acc+c}

    return top
}

fun createStacks(stackDraw: List<String>, stackLen: Int): List<Stack<Char>> {
    val stacks = IntRange(1, stackLen).map { Stack<Char>() }
    stackDraw.reversed().forEach {
        "(\\[[A-Z]\\])".toRegex().findAll(it).forEach {match->
            val stackIndex = ((match.range.first+1) / 4)
            val content = match.value[1]
            stacks[stackIndex].push(content)
        }
    }

    return stacks
}

fun applyMove(moves: List<String>, stacks: List<Stack<Char>>) {
    moves.forEach {
        val match = "move (\\d+) from (\\d+) to (\\d+)".toRegex().find(it)
        val counter = match!!.groups[1]!!.value.toInt()
        val start = match!!.groups[2]!!.value.toInt()
        val end = match!!.groups[3]!!.value.toInt()
        (1 .. counter).map{stacks[start-1].pop()}
            .forEach { stacks[end-1].push(it) }

    }
}

fun applyMove9001(moves: List<String>, stacks: List<Stack<Char>>) {
    moves.forEach {
        val match = "move (\\d+) from (\\d+) to (\\d+)".toRegex().find(it)
        val counter = match!!.groups[1]!!.value.toInt()
        val start = match!!.groups[2]!!.value.toInt()
        val end = match!!.groups[3]!!.value.toInt()
        val crates = (1 .. counter)
            .map{stacks[start-1].pop()}.
            reversed().
            forEach{stacks[end-1].push(it)}
    }
}

fun calcStackLen(stackIndex: String): Int {

    val found = "(\\d+)".toRegex().findAll(stackIndex)
    return found.count()
}





fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
    println("step1: $step1")
    println("step2: $step2")
}






