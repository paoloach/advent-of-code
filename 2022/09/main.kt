import java.io.File

data class Pos2D(val x:Int, val y:Int){
    fun up() = Pos2D(x, y-1)
    fun down() = Pos2D(x, y+1)
    fun left() = Pos2D(x-1, y)
    fun right() = Pos2D(x+1, y)
    fun follow(h: Pos2D):Pos2D{
        if (kotlin.math.abs(h.x - x) >= 2 && kotlin.math.abs(h.y - y) >= 2){
            val newY = if (h.y > y) h.y-1
                       else h.y+1
            val newX = if (h.x > x) h.x-1
                        else h.x+1
            return Pos2D(newX, newY)
        }
        if (h.x == x){
            if (h.y -y >= 2){
                return Pos2D(x, h.y-1)
            }
            if (y - h.y >= 2){
                return Pos2D(x, h.y+1)
            }
        }
        if (h.y == y){
            if (h.x -x >= 2){
                return Pos2D(h.x-1, y)
            }
            if (x - h.x >= 2){
                return Pos2D(h.x+1, y)
            }
        }
        if (kotlin.math.abs(h.x - x) <=1){
            if (kotlin.math.abs(h.y - y) <= 1){
                return Pos2D(x,y)
            }
            return if (h.y > y){
                Pos2D(h.x, h.y-1)
            }else {
                Pos2D(h.x, h.y+1)
            }
        }
        return if (h.x > x){
            Pos2D(h.x-1, h.y)
        }else {
            Pos2D(h.x+1, h.y)
        }


    }
}

fun List<String>.step1():Int {
    var H = Pos2D(0,0)
    var T = Pos2D(0,0)
    val positionsOfT = mutableSetOf<Pos2D>()
    positionsOfT.add(T)
    forEach {
        val (dir, amount) = it.split(" ")
        var steps = amount.toInt()
        while(steps > 0 ){
            when(dir) {
                "L" -> H = H.left()
                "R" -> H = H.right()
                "U" -> H = H.up()
                "D" -> H = H.down()
            }
            steps--
            T = T.follow(H)
            positionsOfT.add(T)
        }
    }


    return positionsOfT.size
}

fun List<String>.step2():Int {
    val rope = mutableListOf<Pos2D>()
    (0 .. 9).forEach{ _ -> rope.add(Pos2D(0,0))}
    val positionsOfT = mutableSetOf<Pos2D>()
    positionsOfT.add(rope[0])
    forEach {
        val (dir, amount) = it.split(" ")
        var steps = amount.toInt()
        while(steps > 0 ){
            when(dir) {
                "L" -> rope[0] = rope[0].left()
                "R" -> rope[0] = rope[0].right()
                "U" -> rope[0] = rope[0].up()
                "D" -> rope[0] = rope[0].down()
            }
            steps--
            (1 .. 9).forEach{
                rope[it] = rope[it].follow(rope[it-1])
            }
            positionsOfT.add(rope[9])
        }
    }


    return positionsOfT.size

}

fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
    println("step1: $step1")
    println("step2: $step2")
}







