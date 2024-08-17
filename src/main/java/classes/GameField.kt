package classes

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel
import javax.swing.Timer

class GameField(
    private val width: Int,
    private val height: Int,
    val snake: Snake
) : JPanel(), ActionListener {
    // Отвечает за скорость игры. Чем меньше, тем быстрее.
    private var speed = 15
    private var apple: Apple? = null

    init {
        background = Color.black
        initGame()
        addKeyListener(FieldKeyListener())
        this.isFocusable = true

        // Удаление курсора
        cursor = this.toolkit.createCustomCursor(
            BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), Point(), null
        )
    }

    private fun initGame() {
        try {
            val appleIcon = Toolkit.getDefaultToolkit().getImage(this.javaClass.getResource("/apple.png"))
            apple = Apple(appleIcon, width, height, snake)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val timer = Timer(speed, this)
        timer.start()
        apple!!.create()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (snake.isAlive) {
            g.drawImage(apple!!.ICON, apple!!.getX(), apple!!.getY(), this)
            for (cords in snake.cords) {
                g.drawImage(snake.icon, cords.x, cords.y, this)
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
        val x = (this.width - metrics.stringWidth(text)) / 2
        val y = (this.height - metrics.height) / 2 + metrics.ascent
        g.drawString(text, x, y)
    }

    private fun checkAppleEaten() {
        val headCords = snake.getCordsAt(0)
        if (headCords.x == apple!!.getX() && headCords.y == apple!!.getY()) {
            snake.addCords(-1, -1)
            apple!!.create()
        }
    }

    override fun actionPerformed(e: ActionEvent) {
        if (snake.isAlive) {
            checkAppleEaten()
            snake.checkCollisions()
            snake.move()
        }
        this.repaint()
    }

    internal inner class FieldKeyListener : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            if (!snake.isMoved) {
                super.keyPressed(e)
                val key = e.keyCode
                if (key == KeyEvent.VK_LEFT && snake.direction != Direction.right) {
                    snake.changeDirection(Direction.left)
                }
                if (key == KeyEvent.VK_RIGHT && snake.direction != Direction.left) {
                    snake.changeDirection(Direction.right)
                }
                if (key == KeyEvent.VK_UP && snake.direction != Direction.down) {
                    snake.changeDirection(Direction.up)
                }
                if (key == KeyEvent.VK_DOWN && snake.direction != Direction.up) {
                    snake.changeDirection(Direction.down)
                }
                snake.isMoved = true
            }
        }
    }
}