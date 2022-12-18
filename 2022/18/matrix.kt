import kotlin.math.sqrt

data class Pos(var x:Int, var y:Int){
    fun up() = Pos(x, y-1)
    fun down() = Pos(x, y+1)
    fun left() = Pos(x-1, y)
    fun right() = Pos(x+1, y)

    fun dist(pos:Pos):Int {
        return sqrt(((pos.x-x) *(pos.x-x)+(pos.y-y)*(pos.y-y)).toDouble()).toInt()
    }

    fun clone() = Pos(x, y)
}

data class PosLong(var x:Long, var y:Long){
    fun up() = PosLong(x, y-1)
    fun down() = PosLong(x, y+1)
    fun left() = PosLong(x-1, y)
    fun right() = PosLong(x+1, y)

    fun dist(pos:PosLong):Int {
        return sqrt(((pos.x-x) *(pos.x-x)+(pos.y-y)*(pos.y-y)).toDouble()).toInt()
    }

    fun clone() = PosLong(x, y)
}

class PosIterator(val startX:Int, val endX:Int, startY:Int, val endY: Int) : Iterator<Pos> {
    var currentX = startX
    var currentY = startY

    override fun hasNext(): Boolean {
        if (currentX == endX-1 && currentY == endY-1){
            return false;
        }
        return true;
    }

    override fun next(): Pos {
        val pos = Pos(currentX, currentY)
        currentX++
        if (currentX == endX){
            if (currentY < endY){
                currentX = startX;
                currentY++;
            }
        }
        return pos

    }
}

class Range2D(val startX:Int, val endX:Int, val startY:Int, val endY: Int) : Iterable<Pos>{

    override fun iterator(): Iterator<Pos> = PosIterator(startX,endX, startY, endY)
}

class MatrixBool (var width: Int, var height: Int){
    var data  = Array<Boolean>(width*height){ false}

    operator fun get(x:Int, y:Int)=  data[y*width+x]
    operator fun get(pos:Pos) = get(pos.x, pos.y)
    operator fun set(x:Int, y:Int, value:Boolean) {
        data[y * width + x] = value;
    }
    operator fun set(pos: Pos, value:Boolean) = set(pos.x, pos.y, value)


}

class MatrixInt (var width: Int, var height: Int, val startX:Int=0, val startY: Int=0){
    var data  = Array<Int>(width*height){ 0}

    operator fun get(x:Int, y:Int)=  data[y*width+x]
    operator fun get(pos:Pos) = get(pos.x, pos.y)
    operator fun set(x:Int, y:Int, value:Int) {
        data[(y-startY) * width + (x-startX)] = value;
    }
    operator fun set(pos: Pos, value:Int) = set(pos.x, pos.y, value)

    fun indexOfFirstRight(pos:Pos, predicate: (Int)->Boolean):Int {
        val startRow = (pos.y-startY)*width
        for(x in pos.x.. width-1){
            if ( predicate(data[(x-startX)+startRow]) )
                return x
        }
        return -1
    }

    fun findRight(pos:Pos, predicate: (Int)->Boolean) = indexOfFirstRight(pos,predicate) != -1

    fun indexOfFirstLeft(pos:Pos, predicate: (Int)->Boolean):Int {
        val startRow = (pos.y-startY)*width
        for(x in pos.x downTo  0){
            if ( predicate(data[x-startX+startRow]) )
                return x
        }
        return -1
    }

    fun findLeft(pos:Pos, predicate: (Int)->Boolean) = indexOfFirstLeft(pos,predicate) != -1

    fun indexOfFirstTop( pos:Pos, predicate: (Int)->Boolean):Int {
        for(y in pos.y downTo  0){
            if ( predicate(data[pos.x-startX + (y-startY)*width]) )
                return y
        }
        return -1
    }

    fun findTop( pos:Pos, predicate: (Int)->Boolean) = indexOfFirstTop(pos,predicate) != -1

    fun indexOfFirstBottom( pos:Pos, predicate: (Int)->Boolean):Int {
        for(y in pos.y ..  height-1){
            if ( predicate(data[pos.x-startX + (y-startY)*width]) )
                return y
        }
        return -1
    }

    fun findBottom( pos:Pos, predicate: (Int)->Boolean) = indexOfFirstBottom(pos,predicate) != -1
}

class Matrix3D<T>(val minX:Int, val maxX:Int, val minY:Int, val maxY:Int, val minZ:Int, val maxZ:Int, init:T){
    val width = maxX-minX
    val height = maxY-minY
    val planeSize = width*height
    val depth = maxZ-minZ
    val data = MutableList<T>(width*height*depth){init}

    operator fun get(x:Int, y:Int, z:Int) = data[ (z-minZ)*planeSize+(y-minY)*width+(x-minX) ]
    operator fun set(x:Int, y:Int, z:Int, value:T)  {
        data[ (z-minZ)*planeSize+(y-minY)*width+(x-minX) ] = value
    }
}