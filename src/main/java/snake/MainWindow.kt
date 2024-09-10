package snake

import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.JFrame

class MainWindow : JFrame() {
    private val gameField: GameField

    init {
        title = "Змейка"
        defaultCloseOperation = EXIT_ON_CLOSE

        // Полноэкранный режим
        this.extendedState = MAXIMIZED_BOTH
        this.isUndecorated = true

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val fieldWidth = screenSize.width
        val fieldHeight = screenSize.height

        val snakeIcon = Toolkit.getDefaultToolkit().getImage(this.javaClass.getResource("/dot.png"))
        val appleIcon = Toolkit.getDefaultToolkit().getImage(this.javaClass.getResource("/apple.png"))

        val snake = Snake(
            snakeIcon,
            3,
            fieldWidth,
            fieldHeight
        )

        val apple = Apple(
            appleIcon,
            fieldWidth,
            fieldHeight,
            snake
        )
        gameField = GameField(
            fieldWidth,
            fieldHeight,
            snake,
            apple
        )
        this.add(gameField)
    }
}