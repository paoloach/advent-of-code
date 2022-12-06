import java.io.File
import java.util.*



fun String.step1():Int {
    for ( index in 0 .. length-4){
        val code = setOf(get(index), get(index+1), get(index+2), get(index+3))
        if (code.size == 4){
            return index+4
        }
    }
    return 0
}

fun String.step2():Int {
    val markLength = 14
    for ( index in 0 .. length-markLength){
        val code = mutableSetOf<Char>()
        for( codeStart in 0 .. markLength-1){
            code.add(get(index +codeStart))
        }
        if (code.size == markLength){
            return index+markLength
        }
    }
    return 0
}




fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readText().step1()
    val step2 = File(fileName).readText().step2()
    println("step1: $step1")
    println("step2: $step2")
}






