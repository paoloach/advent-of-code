import java.io.File

fun List<String>.step1():Int {
    val width= get(0).length
    val height = size

    val matrix = MatrixInt(width, height)

    for (y in 0 .. size-1){
        val row = get(y)
        for (x in 0 .. row.length-1){
            matrix[x, y] = row[x].digitToInt()
        }
    }

    var total = 2*(width+height-2)
    val range=Range2D(1, width-1, 1, height-1)

    for ( pos in range) {
        val treeHeight = matrix[pos]
        val visibleLeft = !matrix.findLeft(pos.left()) { it >= treeHeight }
        val visibleRight = !matrix.findRight(pos.right()) { it >= treeHeight }
        val visibleTop = !matrix.findTop(pos.up()) { it >= treeHeight }
        val visibleBottom = !matrix.findBottom(pos.down()) { it >= treeHeight }
        if (visibleRight || visibleLeft || visibleBottom || visibleTop) {
            total++;
        }
    }


    return total
}

fun List<String>.step2():Int {
    val grid = map { it.map { c->c.digitToInt() } }
    val invertedGrid = mutableListOf<List<Int>>()
    val width= grid[0].size
    val height = grid.size
    for(x in 0 .. width-1) {
        val newRow = mutableListOf<Int>()
        for(y in 0 .. height-1){
            newRow.add(grid[y][x])
        }
        invertedGrid.add(newRow)
    }

   var maxScore=0

    for(y in 0 .. height-1){
        for(x in 0 .. width-1){
            val treeHeight = grid[y][x]
            val left = grid[y].subList(0,x).reversed()
            val right = grid[y].subList(x+1, width)
            val top = invertedGrid[x].subList(0, y).reversed()
            val bottom = invertedGrid[x].subList(y+1, height)
            var scoreLeft = left.indexOfFirst { it >= treeHeight }+1
            if (scoreLeft == 0){
                scoreLeft = left.size
            }
            var scoreRight = right.indexOfFirst { it >= treeHeight }+1
            if (scoreRight == 0){
                scoreRight = right.size
            }
            var scoreTop = top.indexOfFirst { it >= treeHeight }+1
            if (scoreTop == 0){
                scoreTop = top.size
            }
            var scoreBottom = bottom.indexOfFirst { it >= treeHeight }+1
            if (scoreBottom == 0)
                scoreBottom = bottom.size
            val totalScore = scoreLeft*scoreRight*scoreBottom*scoreTop
            if (maxScore < totalScore){
                maxScore = totalScore
            }
        }
    }
    return maxScore

}

fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
    println("step1: $step1")
    println("step2: $step2")
}







