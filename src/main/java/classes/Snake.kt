package classes

import java.awt.Image

class Snake(val icon: Image, snakeLength: Int, private val gameFieldWidth: Int, private val gameFieldHeight: Int) {
        val cords: ArrayList<Cords> = ArrayList()
        private var left = false
        private var right = true
        private var up = false
        private var down = false
        var isMoved = false
        var isAlive = true
            private set

        init {
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

        fun getCordsAt(index: Int): Cords {
            return this.cords[index]
        }

        private fun size(): Int {
            return this.cords.size - 1
        }
    }
