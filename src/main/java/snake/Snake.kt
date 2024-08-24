package snake

import java.awt.Image
import java.util.LinkedList

class Snake(val icon: Image, snakeLength: Int, private val gameFieldWidth: Int, private val gameFieldHeight: Int) {
        val cords: LinkedList<Cords> = LinkedList()
        private var left = false
        private var right = true
        private var up = false
        private var down = false
        var isMoved = false
        private var head: Cords = Cords(0,0)
        var isAlive = true
            private set

        init {
            for (i in 0 until snakeLength) {
                addCords(DOT_SIZE * 5, DOT_SIZE * 5)
            }
            head = cords[0]
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
            this.cords.removeAt(cords.size - 1)
            val newHead = Cords(head.x, head.y)

            if (left) {
                newHead.x = head.x - DOT_SIZE
            }
            if (right) {
                newHead.x = head.x + DOT_SIZE
            }
            if (up) {
                newHead.y = head.y - DOT_SIZE
            }
            if (down) {
                newHead.y = head.y + DOT_SIZE
            }
            this.cords.add(0, newHead)
            head = newHead
            this.isMoved = false
        }

        fun checkCollisions() {
            val headCords = getHead()

            // Столкновения с собой
            for (i in this.size() downTo 4) {
                val peaceCords = cords[i]
                if (headCords.x == peaceCords.x && headCords.y == peaceCords.y) {
                    this.isAlive = false
                    break
                }
            }

            // Выход за границы поля
            if (headCords.x >= gameFieldWidth) {
                headCords.x = -DOT_SIZE
                changeDirection(Direction.right)
            } else if (headCords.x < 0) {
                headCords.x = gameFieldWidth - gameFieldWidth % DOT_SIZE
                changeDirection(Direction.left)
            } else if (headCords.y >= gameFieldHeight) {
                headCords.y = -DOT_SIZE
                changeDirection(Direction.down)
            } else if (headCords.y < 0) {
                headCords.y = gameFieldHeight - gameFieldHeight % DOT_SIZE
                changeDirection(Direction.up)
            }
        }

        fun checkCords(cords: Cords): Boolean {
            return this.cords.contains(cords)
        }

        fun addCords(x: Int, y: Int) {
            this.cords.add(Cords(x, y))
        }

        fun getHead(): Cords {
            return head
        }

        private fun size(): Int {
            return this.cords.size - 1
        }
    }
