import java.io.File
import java.lang.Math.abs

class CRT {
    val display = mutableListOf<CharArray>()
    var x = 0
    var y = 0

    init {
        (0 .. 5).forEach{y->
            display.add( CharArray(40))

        }
    }

    fun cycle(cpu:CPU){
        if (abs(x-cpu.regX) <= 1){
            display[y][x] = '#'
        } else {
            display[y][x] = '.'
        }
        x++;
        if (x >= 40) {
            x=0;
            y++;
            if (y >= 6){
                y=0;
            }
        }
    }

    fun show() {
        (0 .. 5).forEach {
            val row = String(display[it])
            println(row)
        }
    }


}

class CPU(var cycle:Int, var regX: Int){
    val strengths = mutableListOf<Int>(0,1)
    val crt =  CRT()

    fun noop(){
        cycle++;
        crt.cycle(this)
        strengths.add( cycle*regX)
    }

    fun addx(value:Int){
        crt.cycle(this)
        cycle++
        strengths.add( cycle*regX)
        crt.cycle(this)
        cycle++
        regX+=value
        strengths.add( cycle*regX)

    }
}

fun List<String>.step1():Int {
    val cpu = CPU(1,1)

    forEach{instruction->
        run {
            val cmd = instruction.split(" ")
            when (cmd[0]) {
                "noop" -> cpu.noop()
                "addx" -> cpu.addx(cmd[1].toInt())
            }
        }
    }

    return cpu.strengths[20]+cpu.strengths[60]+cpu.strengths[100]+cpu.strengths[140]+cpu.strengths[180]+cpu.strengths[220]

}

fun List<String>.step2():Int {
    val cpu = CPU(1,1)

    forEach{instruction->
        run {
            val cmd = instruction.split(" ")
            when (cmd[0]) {
                "noop" -> cpu.noop()
                "addx" -> cpu.addx(cmd[1].toInt())
            }
        }
    }

    cpu.crt.show()

    return  0
}

fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
    println("step1: $step1")
    println("step2: $step2")
}







