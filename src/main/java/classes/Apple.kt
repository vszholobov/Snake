package classes

import java.awt.Image
import java.util.Random

class Apple(icon: Image, private val WIDTH: Int, private val HEIGHT: Int, private val snake: Snake) {
    private val CORDS: Cords

    val ICON: Image

    init {
        CORDS = Cords(0, 0)
        this.ICON = icon
    }

    fun create() {
        while (true) {
            CORDS.x = Random().nextInt(WIDTH / DOT_SIZE) * DOT_SIZE
            CORDS.y = Random().nextInt(HEIGHT / DOT_SIZE) * DOT_SIZE
            val appleCords = Cords(this.getX(), this.getY())
            if (snake.checkCords(appleCords)) {
                continue
            }
            break
        }
    }

    fun getX(): Int {
        return CORDS.x
    }

    fun getY(): Int {
        return CORDS.y
    }
}