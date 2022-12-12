import java.io.File
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.abs
import kotlin.math.max


const val width=154
const val height = 41


data class Node(val pos:Pos, val dist:Int){
    override fun equals(other: Any?): Boolean {
        if (other is Node){
            return pos == other.pos
        }
        return false
    }

    override fun hashCode(): Int {
        return pos.hashCode()
    }
}

fun insertIfTheCase(node:Node, map:MatrixInt, queue: ArrayDeque<Node>, usedNode:MutableSet<Node>,  point: (pos:Pos)->Pos) {
    val newPos =  point(node.pos)
    if (newPos.x in 0..width-1 && newPos.y in  0 ..  height-1){
        val elevation = map[node.pos]
        val newElevation = map[newPos]
        if (newElevation - elevation <= 1) {
            val node = Node(newPos, node.dist + 1)
            if (!usedNode.contains(node)) {
                queue.add(node)
                usedNode.add(node)
            }
        }
    }
}

fun searchEnd(start:Pos, end:Pos, map:MatrixInt):Int {
    val queue = ArrayDeque<Node>()

    val usedNode = mutableSetOf(Node(start,0))
    queue.add(Node(start, 0))

    while(!queue.isEmpty()){
        val node = queue.first()
        queue.removeFirst()
        if (node.pos == end){
            return node.dist
        }
        insertIfTheCase(node, map, queue, usedNode){it.up()}
        insertIfTheCase(node, map, queue, usedNode){it.down()}
        insertIfTheCase(node, map, queue, usedNode){it.left()}
        insertIfTheCase(node, map, queue, usedNode){it.right()}
    }
    return -1
}

fun calcPath(usedNode: MutableSet<Node>, end: Pos, map:MatrixInt) {
    var posToSearch=end
    while(true){
        println(posToSearch)
        val oldNode = usedNode.find { it.pos == posToSearch }!!
        val level = oldNode.dist
        val neighborn = listOf(
            usedNode.find {it.pos == oldNode.pos.up()},
            usedNode.find {it.pos == oldNode.pos.down()},
            usedNode.find {it.pos == oldNode.pos.left()},
            usedNode.find {it.pos == oldNode.pos.right()}
        )
        .sortedBy { node -> distance(node, oldNode, map) }
        if (neighborn[0]!!.dist == 1)
            return
        posToSearch = neighborn[0]!!.pos
    }
}

fun distance(up: Node?, oldNode: Node, map: MatrixInt) : Int{
   return up?.let {node->
       val  nodeHeight = map[node.pos]
       val originHeight = map[oldNode.pos]
       if (nodeHeight-originHeight >= -1){
           return node.dist
       } else {
           return Int.MAX_VALUE
       }
   } ?: Int.MAX_VALUE
}

fun List<String>.step1():Int {
    var start =Pos(0,0)
    var end = Pos(0,0)
    var current = Pos(0,0)
    var map = MatrixInt(width, height)

    forEach { line->
      line.forEach { row->
          if (row == 'S'){
              start = Pos(current.x, current.y)
          } else  if (row == 'E')
              end  = Pos(current.x, current.y)
          else {
              map[current] = row - 'a'
          }
          current = current.right()
      }
      current = Pos( 0, current.down().y)
    }
    map[end] = ('z'-'a')
    map[start] = -1

    return searchEnd(start,end, map)
}



fun List<String>.step2():Int {
    var start =Pos(0,0)
    var end = Pos(0,0)
    var current = Pos(0,0)
    val map = MatrixInt(width, height)
    val startingPoints = mutableListOf<Pos>()

    forEach { line->
        line.forEach { row->
            if (row == 'S' || row == 'a'){
                start = Pos(current.x, current.y)
                startingPoints.add(start)
            } else  if (row == 'E')
                end  = Pos(current.x, current.y)
            else {
                map[current] = row - 'a'
            }
            current = current.right()
        }
        current = Pos( 0, current.down().y)
    }
    map[end] = ('z'-'a')
    map[start] = -1


    return startingPoints.map { searchEnd(it, end, map) }.filter { it > 0 }.min()

}

fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
   println("step1: $step1")
    println("step2: $step2")
}







