import java.io.File

data class Cube(val x:Int, val y:Int, val z:Int){
    fun getPlanesZ() = listOf(Square(x, x+1, y, y+1, z), Square(x, x+1, y, y+1, z+1))
    fun getPlanesX() = listOf(Square(z, z+1, y, y+1, x), Square(z, z+1, y, y+1, x+1))
    fun getPlanesY() = listOf(Square(z, z+1, x, x+1, y), Square(z, z+1, x, x+1, y+1))
}

fun readLine(line:String):Cube{
    val coord = line.split(",").map { it.toInt() }
    return  Cube(coord[0], coord[1], coord[2])
}

fun List<String>.readData() : List<Cube> = map { readLine(it) }

data class Square(var xMin:Int, var xMax:Int, var yMin:Int, var yMax:Int, var z:Int )

class Shape3D{
    val planesZ = mutableListOf<Square>()
    val planesX = mutableListOf<Square>()
    val planesY = mutableListOf<Square>()

    fun addCube(cube:Cube) {
        val (planeAZ, planeBZ) =cube.getPlanesZ()
        if (planesZ.contains(planeAZ)) {
            planesZ.remove(planeAZ)
        }else {
            planesZ.add(planeAZ)
        }
        if (planesZ.contains(planeBZ)) {
            planesZ.remove(planeBZ)
        }else {
            planesZ.add(planeBZ)
        }


        val (planeAX, planeBX) =cube.getPlanesX()
        if (planesX.contains(planeAX)) {
            planesX.remove(planeAX)
        } else {
            planesX.add(planeAX)
        }
        if (planesX.contains(planeBX)) {
            planesX.remove(planeBX)
        } else {
            planesX.add(planeBX)
        }

        val (planeAY, planeBY) =cube.getPlanesY()
        if (planesY.contains(planeAY)){
            planesY.remove(planeAY)
        }else {
            planesY.add(planeAY)
        }
        if (planesY.contains(planeBY)){
            planesY.remove(planeBY)
        }else {
            planesY.add(planeBY)
        }
    }
}

fun List<String>.step1():Int {

    val cubes = readData()

    val shape3D = Shape3D()

    cubes.forEach{shape3D.addCube(it) }

    return  shape3D.planesX.size + shape3D.planesY.size + shape3D.planesZ.size
}

enum class Status {
    EMPTY,
    ROCK,
    WATER
}

fun fillWaterStep(matrix:Matrix3D<Status>):Boolean{
    var changed = false
    (matrix.minZ until matrix.maxZ).forEach { z ->
        (matrix.minY until matrix.maxY).forEach { y ->
            (matrix.minX until matrix.maxX).forEach { x ->
                if (matrix[x,y,z] == Status.WATER){
                    if (x+1 < matrix.maxX  && matrix[x+1,y,z] == Status.EMPTY){
                        matrix[x+1,y,z] = Status.WATER
                        changed=true
                    }
                    if (x-1 >= matrix.minX && matrix[x-1,y,z] == Status.EMPTY){
                        matrix[x-1,y,z] = Status.WATER
                        changed=true
                    }
                    if (y+1 < matrix.maxY && matrix[x,y+1,z] == Status.EMPTY){
                        matrix[x,y+1,z] = Status.WATER
                        changed=true
                    }
                    if (y-1 >= matrix.minY && matrix[x,y-1,z] == Status.EMPTY){
                        matrix[x,y-1,z] = Status.WATER
                        changed=true
                    }
                    if (z+1 < matrix.maxZ && matrix[x,y,z+1] == Status.EMPTY){
                        matrix[x,y,z+1] = Status.WATER
                        changed=true
                    }
                    if (z-1 >= matrix.minZ && matrix[x,y,z-1] == Status.EMPTY){
                        matrix[x,y,z-1] = Status.WATER
                        changed=true
                    }
                }
            }
        }
    }
    return  changed
}

fun Matrix3D<Status>.print() {
    (minZ until maxZ).forEach { z ->
        println("Plane $z")
        (minY until maxY).forEach { y ->
            var line = ""
            (minX until maxX).forEach { x ->
                line += when(get(x,y,z)){
                    Status.WATER -> 'W'
                    Status.ROCK -> '#'
                    Status.EMPTY -> '.'
                }
            }
            println(line)
        }
    }
}

fun List<String>.step2():Int {
    val cubes = readData()

    val minZ = cubes.minOf{ it.z } -1
    val maxZ = cubes.maxOf { it.z } +1
    val minX = cubes.minOf { it.x } -1
    val minY = cubes.minOf { it.y } -1
    val maxX = cubes.maxOf { it.x } +1
    val maxY = cubes.maxOf { it.y } +1

    val matrix = Matrix3D(minX, maxX+1, minY, maxY+1, minZ, maxZ+1, Status.EMPTY)
    cubes.forEach { matrix[it.x, it.y, it.z] = Status.ROCK }
    (minZ .. maxZ).forEach{z->
        (minY .. maxY).forEach { y ->
            matrix[minX, y, z] = Status.WATER
            matrix[maxX, y, z] = Status.WATER
         }
        (minX .. maxX).forEach { x ->
            matrix[x, minY, z] = Status.WATER
            matrix[x, maxY, z] = Status.WATER
        }
    }
    (minY .. maxY).forEach { y ->
        (minX .. maxX).forEach { x ->
            matrix[x, y, minZ] = Status.WATER
            matrix[x, y, maxZ] = Status.WATER
        }
    }

    var step=0

    while(fillWaterStep(matrix)){
        step++
    }

    var countFaces = 0
    cubes.forEach {
        if (matrix[it.x-1, it.y, it.z] == Status.WATER)
            countFaces++
        if (matrix[it.x+1, it.y, it.z] == Status.WATER)
            countFaces++
        if (matrix[it.x, it.y+1, it.z] == Status.WATER)
            countFaces++
        if (matrix[it.x, it.y-1, it.z] == Status.WATER)
            countFaces++
        if (matrix[it.x, it.y, it.z+1] == Status.WATER)
            countFaces++
        if (matrix[it.x, it.y, it.z-1] == Status.WATER)
            countFaces++
    }

  return countFaces


}

fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
    println("step1: $step1")
    println("step2: $step2")
}







