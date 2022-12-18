import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min


data class Tunnel(val name:String, val flow:Int, val lead:List<String>)

fun parseLine(line:String): Tunnel {
    val founds = "Valve ([A-Z][A-Z]) has flow rate=(\\d*); tunnels? leads? to valves? (.*)".toRegex().find(line)
    return Tunnel(founds!!.groupValues[1]
        , founds.groupValues[2].toInt()
        , founds.groupValues[3].split(",").toList().map { it.trim() })
}

data class Status(val minuteLeft: Int, val flow:Int, val nextTunnel: String, val move:Boolean, val story:MutableList<String>)

data class Children(val name:String, val time:Int=0, val weight:Int=0)

data class Tree(val parent:Tree?, val name: String, val totalFlow:Int,  val time:Int=0, val weight:Int, var flowOpen:Map<String, Int>, var children:MutableList<Tree> = mutableListOf())

data class Tree2( val name1: String, val name2: String, val totalFlow:Int,  val time1:Int, val time2:Int, val weight:Int, val story1: List<String>, val story2: List<String>, var flowOpen:Map<String, Int>){
    init {
        totalChildren ++
        if (totalChildren % 10000000L == 0L ){
            println("after ${totalChildren/1000000} Mchildren, max is $currentMax")
        }

    }
}


var currentMax = 0
var totalChildren=0L
var timeMap = mutableMapOf<Int, Long>()

fun Map<String, Int>.maxFlowAvailable(timer:Int):Int{
    return values.sortedDescending().mapIndexed{ index, value->
        val time = max(timer -2*index, 0)
        value*time
    }.sum()
}

fun Map<String, Int>.maxFlowAvailable2(timer1:Int, timer2:Int):Int{
    var time1 = timer1
    var time2 = timer2
    var maxFlow =0
    val sortedFlows  = values.sortedDescending()
    var flowIndex=0
    while (time1 > 0 && time2 > 0 && flowIndex  <  size){
        val maxTime = max(time1, time2)
        val minTime = min(time1, time2)
        maxFlow += sortedFlows[flowIndex]*maxTime
        flowIndex++
        if (flowIndex== size)
            break
        maxFlow += sortedFlows[flowIndex]*minTime
        flowIndex++
        time2 -= 2
        time1 -= 2
    }
    return maxFlow
}

fun getChildren(parent:Tree,  tunnels:Map<String, Tunnel>): List<Tree>{
    if (parent.time <=0  )
        return listOf()

    val maxFlow = parent.flowOpen.maxFlowAvailable(parent.time)

    if (parent.totalFlow+maxFlow < currentMax)
        return emptyList()

    val children =
        tunnels[parent.name]!!.lead.flatMap {
            val tunnel = tunnels[it]!!
            val result = mutableListOf(Tree(parent, it, parent.totalFlow, parent.time - 1, 0, parent.flowOpen))
            if (parent.flowOpen.contains(it) && parent.time > 1) {
                val newValvesAvailable = parent.flowOpen.toMutableMap()

                val weight = (parent.time - 2) * newValvesAvailable[it]!!
                val flow = parent.totalFlow+weight
                if (flow > currentMax){
                    currentMax = flow
                }
                newValvesAvailable.remove(it)

                result.add(
                    Tree(parent, it, parent.totalFlow+weight,  parent.time - 2, (parent.time - 2) * tunnel.flow, newValvesAvailable)
                )
            }
            result
        }
    return children.sortedBy { it.weight }.reversed()
}

fun checkCycle(story: List<String>, dest:String):Boolean{
    val lastDest = story.indexOfLast {  it == dest }
    if (lastDest == -1)
        return false
    val prevLastDest = story.subList(0, lastDest).indexOfLast { it == dest }
    if (prevLastDest == -1)
        return false
    val cycleSize = lastDest-prevLastDest
    if (lastDest+cycleSize >= story.size-1)
        return false
    for( index in prevLastDest until lastDest){
        if (story[index].endsWith("_1"))
            return false

        if (story[index] != story[index+cycleSize])
            return false
    }
    return true
}

fun getChildren2(parent:Tree2,  tunnels:Map<String, Tunnel>): List<Tree2>{
    if ( (parent.time1 <= 0 && parent.time2 <=0) || parent.flowOpen.isEmpty() )
        return listOf()

    val maxFlow = parent.flowOpen.maxFlowAvailable2(parent.time1, parent.time2)

    if (parent.totalFlow+maxFlow < currentMax)
        return emptyList()

    val children =tunnels[parent.name1]!!.lead.flatMap {dest1->
        tunnels[parent.name2]!!.lead.flatMap { dest2->
            val weight = calcWeight(tunnels, dest1, dest2, parent.time1-1, parent.time2-2, parent.flowOpen)
            val realDest1 = if (parent.time1 > 0)
                                dest1
                            else
                                parent.name1
            val realDest2 = if (parent.time2 > 0)
                                dest2
                            else
                                parent.name2
            val cycleStory1 = checkCycle(parent.story1, realDest1).or(checkCycle(parent.story2, realDest1))
            val cycleStory2 = checkCycle(parent.story2, realDest2).or(checkCycle(parent.story1, realDest2))

            val story1 = parent.story1.toMutableList()
            story1.add(realDest1)
            val story2 = parent.story2.toMutableList()
            story2.add(realDest2)


            val result = if (!cycleStory1 && !cycleStory2)
                            mutableListOf(Tree2( realDest1, realDest2, parent.totalFlow, max(parent.time1 - 1,0), max(parent.time2-1,0), weight, story1, story2 , parent.flowOpen))
                        else {
                            mutableListOf()
                        }
            if (parent.flowOpen.contains(dest1) && parent.time1 > 1){
                val newValvesAvailable = parent.flowOpen.toMutableMap()

                val weight = (parent.time1 - 2) * newValvesAvailable[dest1]!!
                val flow = parent.totalFlow+weight
                if (flow > currentMax){
                    currentMax = flow
                }
                newValvesAvailable.remove(dest1)

                if (parent.time2 <= 0){
                    val childWeight = calcWeight(tunnels, dest1, parent.name2, parent.time1-2, 0, newValvesAvailable)
                    val story1 = parent.story1.toMutableList()
                    story1.add(dest1+"_1")
                    result.add(Tree2( dest1, parent.name2, parent.totalFlow + weight, parent.time1 - 2, 0, weight+childWeight, story1, parent.story2, newValvesAvailable))
                } else {
                    if (!cycleStory2) {
                        val story1 = parent.story1.toMutableList()
                        story1.add(dest1+ "_1")
                        val story2 = parent.story2.toMutableList()
                        story2.add(dest2)
                        val childWeight =
                            calcWeight(tunnels, dest1, dest2, parent.time1 - 2, parent.time2 - 1, newValvesAvailable)
                        result.add(
                            Tree2(dest1, dest2, parent.totalFlow + weight, parent.time1 - 2, parent.time2 - 1, weight + childWeight, story1, story2, newValvesAvailable)
                        )
                    }
                }
                if (newValvesAvailable.contains(dest2) && parent.time2 > 1){
                    val newValvesAvailable2 = newValvesAvailable.toMutableMap()
                    val weight2 = (parent.time2 - 2) * newValvesAvailable2[dest2]!!
                    val flow = parent.totalFlow+weight+weight2
                    if (flow > currentMax){
                        currentMax = flow
                    }
                    newValvesAvailable2.remove(dest2)

                    val childWeight = calcWeight(tunnels, dest1, dest2, parent.time1-2, parent.time2 - 2, newValvesAvailable2)
                    val story1 = parent.story1.toMutableList()
                    story1.add(dest1+"_1")
                    val story2 = parent.story2.toMutableList()
                    story2.add(dest2+"_1")
                    result.add(
                        Tree2( dest1, dest2,  parent.totalFlow+weight+weight2,  parent.time1 - 2, parent.time2-2, weight+weight2+childWeight, story1,story2, newValvesAvailable2)
                    )
                }
            } else if (parent.flowOpen.contains(dest2) && parent.time2 > 1){
                val newValvesAvailable = parent.flowOpen.toMutableMap()

                val weight = (parent.time2 - 2) * newValvesAvailable[dest2]!!
                val flow = parent.totalFlow+weight
                if (flow > currentMax){
                    currentMax = flow
                }
                newValvesAvailable.remove(dest2)

                if (parent.time1 <= 0){
                    val childWeight = calcWeight(tunnels, parent.name1, dest2, 0, parent.time2 - 2, newValvesAvailable)
                    val story2 = parent.story2.toMutableList()
                    story2.add(dest2+"_1")
                    result.add(Tree2( parent.name1, dest2, parent.totalFlow + weight, 0, parent.time2 - 2, weight+childWeight, parent.story1, story2, newValvesAvailable))
                } else {
                    if (!cycleStory1) {
                        val childWeight =
                            calcWeight(tunnels, dest1, dest2, parent.time1 - 1, parent.time2 - 2, newValvesAvailable)
                        val story1 = parent.story1.toMutableList()
                        story1.add(dest1)
                        val story2 = parent.story2.toMutableList()
                        story2.add(dest2 + "_1")
                        result.add(
                            Tree2(dest1, dest2, parent.totalFlow + weight, parent.time1 - 1, parent.time2 - 2, weight + childWeight, story1, story2, newValvesAvailable)
                        )
                    }
                }
            }

            result
        }

    }


    return children.sortedBy { it.weight }.reversed()
}

fun calcWeight(tunnels: Map<String, Tunnel>, dest1: String, dest2: String, time1: Int, time2: Int, flowOpen: Map<String, Int>): Int {
    return tunnels[dest1]!!.lead.maxOf { flowOpen[it]?:0 }*time1 + tunnels[dest2]!!.lead.maxOf { flowOpen[it]?:0 }*time2
}

fun createTree(tree:Tree, tunnels:Map<String, Tunnel> ):List<Tree> {

    val children = getChildren(tree, tunnels)
    tree.children.addAll(children)

    return children.flatMap { child->createTree(child,  tunnels) }

}

fun createTree2(tree:Tree2, tunnels:Map<String, Tunnel> ):List<Tree2> {

    if (tree.name1=="AA" && tree.name2=="GG" && tree.time1==21 && tree.time2==20){
        println("eccomi")
    }
    val children = getChildren2(tree, tunnels)

    val newTrees =  children
        .flatMap { child->createTree2(child,  tunnels) }
        .filter {
            val maxFlow = it.flowOpen.maxFlowAvailable2(it.time1, it.time2)
            it.totalFlow+maxFlow >currentMax
        }

    return newTrees

}

fun List<String>.step1():Int {
    currentMax=0
    val tunnels = map{parseLine(it)}.map { it.name to it }.toMap()

    val valves = tunnels.filter { it.value.flow > 0 }.map { it.key to it.value.flow }.toMap()


    val root = Tree(null, "AA", 0, 30, 0,valves)

    val timer = 30

    var children = createTree(root,  tunnels)



    return currentMax
}

fun List<String>.step2():Int {
    currentMax=0
    val tunnels = map{parseLine(it)}.map { it.name to it }.toMap()

    val valves = tunnels.filter { it.value.flow > 0 }.map { it.key to it.value.flow }.toMap()

    val root = Tree2( "AA", "AA",0, 26, 26, 0, listOf("AA"), listOf("AA"), valves)

 
    var children = createTree2(root,  tunnels)

    println("TotalChildren: $totalChildren")
    println("timeMap: $timeMap")
    return currentMax

}



fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    println("step1: $step1")

    val step2 = File(fileName).readLines().step2()
    println("step2: $step2")
}







