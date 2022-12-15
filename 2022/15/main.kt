import java.io.File
import java.lang.Integer.min
import java.lang.Integer.max
import java.lang.Math.abs
import java.util.*

const val EMPTY = 0
const val SENSOR = 1
const val BAECON = 2
const val NO_BAECON = 3

fun Pos.distL1(other:Pos) =  kotlin.math.abs(x - other.x) + abs(y-other.y)


data class SensAndBeacon(val sensor:Pos, val baecon: Pos) {
    fun isInside(pos:Pos):Boolean{
        val baeconDist = sensor.distL1(baecon)
        val posDist = sensor.distL1(pos)
        return posDist <= baeconDist
    }
}

fun parseLine(line:String): SensAndBeacon {
    val groups = "Sensor at x=(-*\\d+), y=(-*\\d+): closest beacon is at x=(-*\\d+), y=(-*\\d+)".toRegex().find(line)
    return  SensAndBeacon(Pos(groups!!.groupValues[1].toInt(),groups!!.groupValues[2].toInt()), Pos(groups!!.groupValues[3].toInt(), groups!!.groupValues[4].toInt()))
}




fun List<String>.step1():Int {
    val  rows = map{parseLine(it)}

    val minX = rows.map{ it.sensor.x-it.sensor.distL1(it.baecon)}.min()
    val maxX = rows.map { it.sensor.x +it.sensor.distL1(it.baecon)}.max()

    val points = mutableSetOf<Pos>()

    rows.forEach {
        points.add(it.sensor)
        points.add(it.baecon)

    }

    val count = (minX .. maxX).map { x->Pos(x, 2000000) }
        .filter { !points.contains(it) }
        .count{pos->
            rows.find { sensAndBeacon ->  sensAndBeacon.isInside(pos) } != null
        }

    return count
}

fun List<String>.step2():Long {
    val  rows = map{parseLine(it)}

    for( y in (0 .. 4000000)) {
        var x=0
        while(x < 4000000 ) {
            val pos = Pos(x, y)

            val sensor = rows.find { sensAndBeacon ->  sensAndBeacon.isInside(pos)  }
            if (sensor == null) {
                println("found at $pos")
                return pos.x.toLong()*4000000L+ pos.y.toLong()
                break;
            }
            val baeconDistance = sensor.sensor.distL1(sensor.baecon)
            val deltaX = baeconDistance-abs(pos.y-sensor.sensor.y)

            val lastXNoBeacon = (sensor.sensor.x +deltaX)
            x = lastXNoBeacon+1
        }
    }

    println("NOT FOUND")
    return 0
  }

fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
   println("step1: $step1")
    println("step2: $step2")
}







