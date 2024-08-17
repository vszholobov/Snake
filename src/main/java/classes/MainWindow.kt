package classes;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private GameField gameField;
    private Dimension fieldSize;

    public MainWindow() {
        this.setTitle("Змейка");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Полноэкранный режим
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);

        this.fieldSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.gameField = new GameField(this.fieldSize);

        this.add(this.gameField);
    }

    public void close() {
        this.removeAll();
        this.dispose();
    }
}