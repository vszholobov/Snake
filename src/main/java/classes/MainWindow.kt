package classes

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
        gameField = GameField(fieldSize)
        this.add(gameField)
    }

    fun close() {
        this.removeAll()
        dispose()
    }
}