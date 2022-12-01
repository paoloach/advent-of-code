import java.io.File



fun main(args: Array<String>) {
    step1()
    step2()
}
fun step1(){
    val elvesCal = mutableListOf<Int>()
    var elfCal=0;
    val fileName="input.txt"
    File(fileName).forEachLine { line->
            line.toIntOrNull() ?.let { cal -> elfCal+=cal } ?: run {
                elvesCal.add(elfCal);
                elfCal=0}
        }

    elvesCal.sortDescending()

    println("Elf carrying the most cal : ${elvesCal[0]}")

}

fun step2(){
    val elvesCal = mutableListOf<Int>()
    var elfCal=0;
    val fileName="input.txt"
    File(fileName).forEachLine { line->
        line.toIntOrNull() ?.let { cal -> elfCal+=cal } ?: run {
            elvesCal.add(elfCal);
            elfCal=0}
    }

    elvesCal.sortDescending()

    println("The three elves carrying the most cal : ${elvesCal[0]+elvesCal[1]+elvesCal[2]}")

}