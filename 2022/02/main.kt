import java.io.File



fun main(args: Array<String>) {
    step1()
    step2()
}

val win=6
val draw=3
val lose=0
val  choiceX = 1
val  choiceY = 2
val  choiceZ = 3
val  choiceA = 1
val  choiceB = 2
val  choiceC = 3

// A win Y
// B win Z
// C win X
fun calcPoint(result: String) = when(result) {
    "A Y" -> choiceY + win
    "A X" -> choiceX + draw
    "A Z" -> choiceZ + lose
    "B Y" -> choiceY + draw
    "B X" -> choiceX + lose
    "B Z" -> choiceZ + win
    "C Y" -> choiceY + lose
    "C X" -> choiceX + win
    "C Z" -> choiceZ + draw
    else -> 0
}

// x -> lose
// y -> draw
// z -> win
// A win C
// B win A
// C win B
fun calcPoint2(result: String) = when(result) {
    "A Y" -> draw + choiceA
    "A X" -> lose + choiceC
    "A Z" -> win + choiceB
    "B Y" -> draw + choiceB
    "B X" -> lose + choiceA
    "B Z" -> win + choiceC
    "C Y" -> draw + choiceC
    "C X" -> lose + choiceB
    "C Z" -> win + choiceA
    else -> 0
}


fun step1(){

    val fileName="input.txt"
    val totol = File(fileName).readLines().map {calcPoint(it) }.sum()
    println("total  $totol")
}

fun step2(){
    val fileName="input.txt"
    val totol = File(fileName).readLines().map {calcPoint2(it) }.sum()
    println("total  real $totol")
}