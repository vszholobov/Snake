package snake

class Cords(var x: Int, var y: Int) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val cords = o as Cords
        return x == cords.x && y == cords.y
    }

    override fun hashCode(): Int {
        return x xor y
    }
}
