import java.io.File

data class Pos(var y:Int, var x:Int) {
    fun up() = Pos(y+1, x)
    fun down() = Pos(y-1, x)
    fun left() = Pos(y, x-1)
    fun right() = Pos(y, x+1)

}

fun List<List<Int>>.isMinimum(y:Int, x:Int)  = get(y)[x] < get(y)[x-1] &&
       get(y)[x] <get(y)[x+1] &&
       get(y)[x] < get(y-1)[x] &&
       get(y)[x] < get(y+1)[x]

fun List<List<Int>>.value(pos:Pos)  = get(pos.y)[pos.x]

fun addBasin(matrix: List<List<Int>>,  basinPoint: MutableSet<Pos>, pos: Pos){
    if (matrix.value(pos) >= 9)
        return
    basinPoint.add(pos)
    checkUpperBasin(basinPoint, pos.up(), matrix, pos)
    checkUpperBasin(basinPoint, pos.down(), matrix, pos)
    checkUpperBasin(basinPoint, pos.left(), matrix, pos)
    checkUpperBasin(basinPoint, pos.right(), matrix, pos)
}

private fun checkUpperBasin(basinPoint: MutableSet<Pos>, up: Pos, matrix: List<List<Int>>, pos: Pos) {
    if (!basinPoint.contains(up) && matrix.value(up) > matrix.value(pos) && matrix.value(up) < 9) {
        addBasin(matrix, basinPoint, up)
    }
}

fun main(args: Array<String>) {
    step1()
    step2()
}
fun step1(){
    val matrix = mutableListOf<MutableList<Int>>(mutableListOf())

    val fileName="input.txt"
    File(fileName).forEachLine { line->
            val row = mutableListOf(Int.MAX_VALUE)
            line.forEach { height->
                row.add(height.digitToInt())
            }
            row.add(Int.MAX_VALUE)
            matrix.add(row)
        }
    val colLength = matrix[1].size
    matrix[0] = MutableList(colLength){ Int.MAX_VALUE}
    matrix.add(MutableList(colLength){ Int.MAX_VALUE})
    val rowLength = matrix.size

    var totalRiskLevel = 0;
    for( x in 1 .. colLength-2 ){
        for (y in 1 .. rowLength-2){
            if( matrix.isMinimum(y,x)){
                totalRiskLevel += matrix[y][x]+1
            }
        }
    }

    println("Risk level $totalRiskLevel")

}

fun step2(){
    val matrix = mutableListOf<MutableList<Int>>(mutableListOf())

    val fileName="input.txt"
    File(fileName).forEachLine { line->
        val row = mutableListOf(Int.MAX_VALUE)
        line.forEach { height->
            row.add(height.digitToInt())
        }
        row.add(Int.MAX_VALUE)
        matrix.add(row)
    }
    val colLength = matrix[1].size
    matrix[0] = MutableList(colLength){ Int.MAX_VALUE}
    matrix.add(MutableList(colLength){ Int.MAX_VALUE})
    val rowLength = matrix.size

    val basinsSize = mutableListOf<Int>()
    for( x in 1 .. colLength-2 ){
        for (y in 1 .. rowLength-2){
            if( matrix.isMinimum(y,x)){
                val pos = Pos(y,x)
                val basinPoint = mutableSetOf(pos)
                addBasin(matrix, basinPoint, pos.up())
                addBasin(matrix, basinPoint, pos.down())
                addBasin(matrix, basinPoint, pos.left())
                addBasin(matrix, basinPoint, pos.right())
                basinsSize.add(basinPoint.size)
            }
        }
    }

    basinsSize.sortDescending()
    val result = basinsSize[0] * basinsSize[1] * basinsSize[2]
    println("first three basins $result")


}