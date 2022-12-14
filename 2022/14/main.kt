import java.io.File
import java.lang.Integer.min
import java.lang.Integer.max
import java.util.*

enum class FallingStatus {
    FALLING,
    REST,
    LOST
}


const val FLOOR = 3
const val ROCK = 1
const val SAND = 2
const val EMPTY = 0

fun getPath(line: String): List<Pos> {
    val couples = "(\\d+),(\\d+)".toRegex().findAll(line)
    val list = couples.map { Pos(it.groupValues[1].toInt(), it.groupValues[2].toInt()) }.toList()
    return list
}

fun fillWithRock(matrixInt: MatrixInt, paths: List<List<Pos>>,  minX:Int){
    paths.forEach {path->
        for (index in 0..path.size - 2) {
            val start = path[index]
            val end  = path[index+1]
            if (start.x == end.x){
                val minY = min(start.y, end.y)
                val maxY = max(start.y, end.y)
                for (y in IntRange(minY,maxY)){
                    matrixInt[Pos(start.x-minX,y )] = ROCK
                }
            } else {
                val minLineX = min(start.x, end.x)
                val maxLineX = max(start.x, end.x)
                for (x in IntRange (minLineX, maxLineX)){
                    matrixInt[Pos(x-minX, start.y)] = ROCK
                }
            }
        }
    }
}

fun Pos.move(matrix: MatrixInt):FallingStatus{
    val newPos = down()
    if (newPos.y >= matrix.height) {
        return FallingStatus.LOST
    }
    if (matrix[newPos] == FLOOR){
        return FallingStatus.REST
    }
    if (matrix[newPos] == EMPTY){
        y = newPos.y
        return FallingStatus.FALLING
    }
    val bottomLeft = newPos.left()
    if (bottomLeft.x < 0){
        return FallingStatus.LOST
    }
    if (matrix[bottomLeft] == EMPTY){
        y = bottomLeft.y
        x = bottomLeft.x
        return FallingStatus.FALLING
    }
    val bottomRight = newPos.right()
    if (bottomRight.x >= matrix.width){
        return FallingStatus.LOST
    }
    if (matrix[bottomRight] == EMPTY){
        y = bottomRight.y
        x = bottomRight.x
        return FallingStatus.FALLING
    }
    return FallingStatus.REST

}

fun MatrixInt.print() {
    for (y in (0 .. height-1)){
        val row = (0 .. width-1).map {x->
            when(get(Pos(x,y))){
                EMPTY->'.'
                ROCK->'#'
                FLOOR -> '#'
                SAND->'o'
                else -> 'A'
            } }.joinToString("") { it.toString() }
        println(row)
    }
}

fun List<String>.step1():Int {
    val paths = map{getPath(it)}
    val maxX = paths.maxOf { it.maxOf { it.x }}
    val minX = paths.minOf { it.minOf { it.x }}
    val maxY= paths.maxOf { it.maxOf { it.y }}


    val width = maxX-minX +1
    val height = maxY+1

    val matrix = MatrixInt(width, height)

    fillWithRock(matrix, paths, minX)
    matrix[Pos(500-minX, 0)] = SAND

    matrix.print()

    var allRest = false
    var grainOfSand=0
    while(allRest==false){
        val sandPos = Pos(500-minX,0)
        var status = FallingStatus.FALLING
        while(status == FallingStatus.FALLING){
            status = sandPos.move(matrix)
        }
        if (status == FallingStatus.LOST){
            allRest=true
        }
        matrix[sandPos] = SAND
        grainOfSand++;
    }
    matrix.print()

    return grainOfSand-1

}



fun List<String>.step2():Int {
    val paths = map{getPath(it)}
    val maxY= paths.maxOf { it.maxOf { it.y }}
    val maxX = max(500+(maxY+2), paths.maxOf { it.maxOf { it.x }})
    val minX = min( 500-(maxY+2), paths.minOf { it.minOf { it.x }})

    val width = maxX-minX +1
    val height = maxY+1

    val matrix = MatrixInt(width, height+2)

    fillWithRock(matrix, paths, minX)
    (minX .. maxX).forEach { x ->
        matrix[Pos(x-minX, maxY+1)] = EMPTY
        matrix[Pos(x-minX, maxY+2)] = FLOOR
     }
    matrix[Pos(500-minX, 0)] = SAND

    matrix.print()

    var allRest = false
    var grainOfSand=0
    while(allRest==false){
        val sandPos = Pos(500-minX,0)
        var status = FallingStatus.FALLING
        while(status == FallingStatus.FALLING){
            status = sandPos.move(matrix)
        }
        if (status == FallingStatus.LOST || sandPos.y == 0){
            allRest=true
        }
        matrix[sandPos] = SAND
        grainOfSand++;
    }
    matrix.print()

    return grainOfSand
}

fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
   println("step1: $step1")
    println("step2: $step2")
}







