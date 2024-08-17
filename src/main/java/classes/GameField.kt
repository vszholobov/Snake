package classes

import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.Image
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.util.Random
import javax.swing.JPanel
import javax.swing.Timer

// Создай класс яблока и змеи
class GameField(windowSize: Dimension) : JPanel(), ActionListener {
    // Размер в пикселях одной клетки поля
    private val DOT_SIZE = 16

    // Размеры игрового поля
    private val WIDTH: Int
    private val HEIGHT: Int

    // Отвечает за скорость игры. Чем меньше, тем быстрее.
    private var speed = 15
    private var apple: Apple? = null
    private var snake: Snake? = null

    // Направления движения змейки
    private enum class Direction {
        left, right, up, down
    }

    init {
        this.WIDTH = windowSize.width
        this.HEIGHT = windowSize.height
        background = Color.black
        initGame()
        addKeyListener(FieldKeyListener())
        this.isFocusable = true

        // Удаление курсора
        cursor = this.toolkit.createCustomCursor(
            BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), Point(), null
        )
    }

    fun initGame() {
        try {
            val appleIcon = Toolkit.getDefaultToolkit().getImage(this.javaClass.getResource("/apple.png"))
            val snakeIcon = Toolkit.getDefaultToolkit().getImage(this.javaClass.getResource("/dot.png"))
            apple = Apple(appleIcon)
            snake = Snake(snakeIcon, 3)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val timer = Timer(speed, this)
        timer.start()
        apple!!.create()
    }

    fun setSpeed(speed: Int) {
        this.speed = speed
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (snake!!.isAlive) {
            g.drawImage(apple!!.ICON, apple!!.getX(), apple!!.getY(), this)
            for (cords in snake!!.CORDS) {
                g.drawImage(snake!!.ICON, cords.x, cords.y, this)
            }
        } else {
            gameOver(g)
        }
    }

    private fun gameOver(g: Graphics) {
        val text = "ВЫ ПОГИБЛИ"
        g.color = Color(239, 41, 41)
        val font = Font("Arial", Font.PLAIN, 100)
        g.font = font
        val metrics = g.getFontMetrics(font)
        val x = (this.WIDTH - metrics.stringWidth(text)) / 2
        val y = (this.HEIGHT - metrics.height) / 2 + metrics.ascent
        g.drawString(text, x, y)
    }

    fun checkAppleEaten() {
        val headCords = snake!!.getCordsAt(0)
        if (headCords.x == apple!!.getX() && headCords.y == apple!!.getY()) {
            snake!!.addCords(-1, -1)
            apple!!.create()
        }
    }

    override fun actionPerformed(e: ActionEvent) {
        if (snake!!.isAlive) {
            checkAppleEaten()
            snake!!.checkCollisions()
            snake!!.move()
        }
        this.repaint()
    }

    internal inner class FieldKeyListener : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            if (!snake!!.isMoved) {
                super.keyPressed(e)
                val key = e.keyCode
                if (key == KeyEvent.VK_LEFT && snake!!.direction != Direction.right) {
                    snake!!.changeDirection(Direction.left)
                }
                if (key == KeyEvent.VK_RIGHT && snake!!.direction != Direction.left) {
                    snake!!.changeDirection(Direction.right)
                }
                if (key == KeyEvent.VK_UP && snake!!.direction != Direction.down) {
                    snake!!.changeDirection(Direction.up)
                }
                if (key == KeyEvent.VK_DOWN && snake!!.direction != Direction.up) {
                    snake!!.changeDirection(Direction.down)
                }
                snake!!.isMoved = true
            }
        }
    }

    private inner class Apple(icon: Image) {
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
                if (snake!!.checkCords(appleCords)) {
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

    private inner class Snake(icon: Image, snakeLength: Int) {
        val CORDS: ArrayList<Cords>
        val ICON: Image
        private var left = false
        private var right = true
        private var up = false
        private var down = false
        var isMoved = false
        var isAlive = true
            private set

        init {
            this.CORDS = ArrayList<Cords>()
            this.ICON = icon
            for (i in 0 until snakeLength) {
                addCords(DOT_SIZE * 5, DOT_SIZE * 5)
            }
        }

        fun changeDirection(direction: Direction?) {
            left = false
            right = false
            up = false
            down = false
            when (direction) {
                Direction.left -> left = true
                Direction.right -> right = true
                Direction.up -> up = true
                Direction.down -> down = true
                else -> {}
            }
        }

        val direction: Direction?
            get() {
                if (left) {
                    return Direction.left
                }
                if (right) {
                    return Direction.right
                }
                if (up) {
                    return Direction.up
                }
                return if (down) {
                    Direction.down
                } else null
            }

        fun move() {
            for (i in this.size() downTo 1) {
                getCordsAt(i).x = getCordsAt(i - 1).x
                getCordsAt(i).y = getCordsAt(i - 1).y
            }
            val headCords = getCordsAt(0)
            if (left) {
                headCords.x = headCords.x - DOT_SIZE
            }
            if (right) {
                headCords.x = headCords.x + DOT_SIZE
            }
            if (up) {
                headCords.y = headCords.y - DOT_SIZE
            }
            if (down) {
                headCords.y = headCords.y + DOT_SIZE
            }
            this.isMoved = false
        }

        fun checkCollisions() {
            val headCords = getCordsAt(0)

            // Столкновения с собой
            for (i in this.size() downTo 4) {
                val peaceCords = getCordsAt(i)
                if (headCords.x == peaceCords.x && headCords.y == peaceCords.y) {
                    this.isAlive = false
                    break
                }
            }

            // Выход за границы поля
            if (headCords.x >= WIDTH) {
                headCords.x = -DOT_SIZE
                changeDirection(Direction.right)
            } else if (headCords.x < 0) {
                headCords.x = WIDTH - WIDTH % DOT_SIZE
                changeDirection(Direction.left)
            } else if (headCords.y >= HEIGHT) {
                headCords.y = -DOT_SIZE
                changeDirection(Direction.down)
            } else if (headCords.y < 0) {
                headCords.y = HEIGHT - HEIGHT % DOT_SIZE
                changeDirection(Direction.up)
            }
        }

        fun checkCords(cords: Cords): Boolean {
            return this.CORDS.contains(cords)
        }

        fun addCords(x: Int, y: Int) {
            this.CORDS.add(Cords(x, y))
        }

        fun getCordsAt(index: Int): Cords {
            return this.CORDS.get(index)
        }

        fun size(): Int {
            return this.CORDS.size - 1
        }
    }

    private class Cords(var x: Int, var y: Int) {

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
}