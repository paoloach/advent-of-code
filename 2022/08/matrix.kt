data class Pos(val x:Int, val y:Int){
    fun up() = Pos(x, y-1)
    fun down() = Pos(x, y+1)
    fun left() = Pos(x-1, y)
    fun right() = Pos(x+1, y)
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

class MatrixInt (var width: Int, var height: Int){
    var data  = Array<Int>(width*height){ 0}

    operator fun get(x:Int, y:Int)=  data[y*width+x]
    operator fun get(pos:Pos) = get(pos.x, pos.y)
    operator fun set(x:Int, y:Int, value:Int) {
        data[y * width + x] = value;
    }

    fun indexOfFirstRight(pos:Pos, predicate: (Int)->Boolean):Int {
        val startRow = pos.y*width
        for(x in pos.x.. width-1){
            if ( predicate(data[x+startRow]) )
                return x
        }
        return -1
    }

    fun findRight(pos:Pos, predicate: (Int)->Boolean) = indexOfFirstRight(pos,predicate) != -1

    fun indexOfFirstLeft(pos:Pos, predicate: (Int)->Boolean):Int {
        val startRow = pos.y*width
        for(x in pos.x downTo  0){
            if ( predicate(data[x+startRow]) )
                return x
        }
        return -1
    }

    fun findLeft(pos:Pos, predicate: (Int)->Boolean) = indexOfFirstLeft(pos,predicate) != -1

    fun indexOfFirstTop( pos:Pos, predicate: (Int)->Boolean):Int {
        for(y in pos.y downTo  0){
            if ( predicate(data[pos.x + y*width]) )
                return y
        }
        return -1
    }

    fun findTop( pos:Pos, predicate: (Int)->Boolean) = indexOfFirstTop(pos,predicate) != -1

    fun indexOfFirstBottom( pos:Pos, predicate: (Int)->Boolean):Int {
        for(y in pos.y ..  height-1){
            if ( predicate(data[pos.x + y*width]) )
                return y
        }
        return -1
    }

    fun findBottom( pos:Pos, predicate: (Int)->Boolean) = indexOfFirstBottom(pos,predicate) != -1
}