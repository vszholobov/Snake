package snake

import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.JFrame

class MainWindow : JFrame() {
    private val gameField: GameField
    private val fieldSize: Dimension

    init {
        title = "Змейка"
        defaultCloseOperation = EXIT_ON_CLOSE

        // Полноэкранный режим
        this.extendedState = MAXIMIZED_BOTH
        this.isUndecorated = true
        fieldSize = Toolkit.getDefaultToolkit().screenSize
        val snakeIcon = Toolkit.getDefaultToolkit().getImage(this.javaClass.getResource("/dot.png"))

        val snake = Snake(
            snakeIcon,
            3,
            fieldSize.width,
            fieldSize.height
        )
        gameField = GameField(
            fieldSize.width,
            fieldSize.height,
            snake
        )
        this.add(gameField)
    }
}