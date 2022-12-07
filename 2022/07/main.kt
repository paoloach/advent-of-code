import java.io.File

data class ElfFile(val path:String, val size:Long){
    override fun equals(other: Any?): Boolean {
        return if (other is ElfFile){
            path == other.path
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}

data class ElfDir(val parent:ElfDir?, val path:String, val files: MutableSet<ElfFile> = mutableSetOf(), val children: MutableMap<String, ElfDir> = mutableMapOf()){
    override fun equals(other: Any?): Boolean {
        return if (other is ElfDir){
            path == other.path
        } else {
            false
        }
    }

    private fun fileSize() = files.map { it.size }.sum()

    fun dirSize():Long  {
        return fileSize() + children.values.sumOf { it.dirSize() }
    }

    fun getAllChildren():List<ElfDir> {
        return children.values + children.values.flatMap { it.getAllChildren() }
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}

fun List<String>.step1():Long {
    val rootDir = ElfDir(null,"/")
    var currentDir = rootDir
    forEach {
        if (it.startsWith("$ cd")){
            val cdDest = it.substring(5)
            if (cdDest == "/"){
                currentDir = rootDir
            } else if (cdDest == "..") {
                currentDir = currentDir.parent ?: rootDir
            } else {
                val newDir = currentDir.children[cdDest] ?: ElfDir(currentDir, cdDest)
                currentDir.children[cdDest] = newDir
                currentDir = newDir
            }

        } else if (it.startsWith("$ ls")) {
            // skip
        } else if (it.startsWith("dir")){
            // skip
        } else {
            val (size, name) = it.split(" ")
            currentDir.files.add(ElfFile(name, size.toLong()))
        }
    }

    val allDir = mutableListOf( rootDir)
    allDir.addAll(rootDir.getAllChildren())
    return allDir
        .filter { it.dirSize() <= 100000  }
        .sumOf { it.dirSize() }
}

fun List<String>.step2():Long {
    val rootDir = ElfDir(null,"/")
    var currentDir = rootDir
    forEach {
        if (it.startsWith("$ cd")){
            val cdDest = it.substring(5)
            if (cdDest == "/"){
                currentDir = rootDir
            } else if (cdDest == "..") {
                currentDir = currentDir.parent ?: rootDir
            } else {
                val newDir = currentDir.children[cdDest] ?: ElfDir(currentDir, cdDest)
                currentDir.children[cdDest] = newDir
                currentDir = newDir
            }

        } else if (it.startsWith("$ ls")) {
            // skip
        } else if (it.startsWith("dir")){
            // skip
        } else {
            val (size, name) = it.split(" ")
            currentDir.files.add(ElfFile(name, size.toLong()))
        }
    }

    val allDir = mutableListOf( rootDir)
    allDir.addAll(rootDir.getAllChildren())

    val totalSize = rootDir.dirSize()
    val available = 70000000-totalSize
    val need = 30000000-available

    val usefulToDelete = allDir.filter { it.dirSize() >= need }.sortedBy { it.dirSize() }

    return usefulToDelete[0].dirSize()

}




fun main(args: Array<String>) {
    val fileName="input.txt"
    val step1 = File(fileName).readLines().step1()
    val step2 = File(fileName).readLines().step2()
    println("step1: $step1")
    println("step2: $step2")
}






