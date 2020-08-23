package classes;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;

// Создай класс яблока и змеи
public class GameField extends JPanel implements ActionListener {
    // Размер в пикселях одной клетки поля
    private final int DOT_SIZE = 16;

    // Размеры игрового поля
    private final int WIDTH;
    private final int HEIGHT;

    // Отвечает за скорость игры. Чем меньше, тем быстрее.
    private int speed = 35;

    private Apple apple;
    private Snake snake;

    // Направления движения змейки
    private enum Direction {
        left,
        right,
        up,
        down
    }

    public GameField(Dimension windowSize) {
        this.WIDTH = windowSize.width;
        this.HEIGHT = windowSize.height;

        this.setBackground(Color.black);
        this.initGame();
        this.addKeyListener(new FieldKeyListener());
        this.setFocusable(true);

        // Удаление курсора
        this.setCursor( this.getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
    }

    public void initGame() {
        try {
            Image appleIcon = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/apple.png"));
            Image snakeIcon = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/dot.png"));
            this.apple = new Apple(appleIcon);
            this.snake = new Snake(snakeIcon, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Timer timer = new Timer(this.speed, this);
        timer.start();

        this.apple.create();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(this.snake.isAlive()){
            g.drawImage(this.apple.getIcon(), this.apple.getX(), this.apple.getY(),this);
            for(Cords cords : this.snake.getCORDS()) {
                g.drawImage(this.snake.getICON(), cords.getX(), cords.getY(), this);
            }
        } else{
            this.gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String text = "ВЫ ПОГИБЛИ";

        g.setColor(new Color(239, 41, 41));
        Font font = new Font("Arial", Font.PLAIN, 100);
        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics(font);

        int x = (this.WIDTH - metrics.stringWidth(text)) / 2;
        int y = ((this.HEIGHT - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
    }

    public void checkAppleEaten() {
        Cords headCords = this.snake.getCordsAt(0);
        if(headCords.getX() == apple.getX() && headCords.getY() == apple.getY()) {
            this.snake.addCords(-1, -1);
            apple.create();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(this.snake.isAlive()){
            this.checkAppleEaten();
            this.snake.checkCollisions();
            this.snake.move();
        }
        this.repaint();
    }

    class FieldKeyListener extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            if(!snake.isMoved()) {
                super.keyPressed(e);
                int key = e.getKeyCode();

                if(key == KeyEvent.VK_LEFT && !(snake.getDirection() == Direction.right)) {
                    snake.changeDirection(Direction.left);
                }
                if(key == KeyEvent.VK_RIGHT && !(snake.getDirection() == Direction.left)) {
                    snake.changeDirection(Direction.right);
                }
                if(key == KeyEvent.VK_UP && !(snake.getDirection() == Direction.down)) {
                    snake.changeDirection(Direction.up);
                }
                if(key == KeyEvent.VK_DOWN && !(snake.getDirection() == Direction.up)) {
                    snake.changeDirection(Direction.down);
                }
                snake.setMoved(true);
            }
        }
    }

    private class Apple {
        private final Cords CORDS;
        private final Image ICON;

        public Apple(Image icon) {
            this.CORDS = new Cords(0,0);
            this.ICON = icon;
        }

        public void create() {
            while(true) {
                this.CORDS.setX(new Random().nextInt(WIDTH / DOT_SIZE) * DOT_SIZE);
                this.CORDS.setY(new Random().nextInt(HEIGHT / DOT_SIZE) * DOT_SIZE);
                Cords appleCords = new Cords(this.getX(), this.getY());

                if(snake.checkCords(appleCords)) {
                    continue;
                }
                break;
            }
        }

        public int getX() {
            return this.CORDS.getX();
        }

        public int getY() {
            return this.CORDS.getY();
        }

        public Image getIcon() {
            return this.ICON;
        }
    }

    private class Snake {
        private final ArrayList<Cords> CORDS;
        private final Image ICON;

        private boolean left = false;
        private boolean right = true;
        private boolean up = false;
        private boolean down = false;

        private boolean moved = false;
        private boolean alive = true;

        public Snake(Image icon, int snakeLength) {
            this.CORDS = new ArrayList<>();
            this.ICON = icon;

            for(int i = 0; i < snakeLength; i++) {
                this.addCords(DOT_SIZE * 5, DOT_SIZE * 5);
            }
        }

        public boolean isAlive() {
            return alive;
        }

        public boolean isMoved() {
            return this.moved;
        }

        public void setMoved(boolean moved) {
            this.moved = moved;
        }

        public void changeDirection(Direction direction) {
            this.left = false;
            this.right = false;
            this.up = false;
            this.down = false;

            switch(direction) {
                case left:
                    this.left = true;
                    break;
                case right:
                    this.right = true;
                    break;
                case up:
                    this.up = true;
                    break;
                case down:
                    this.down = true;
                    break;
            }
        }

        public Direction getDirection() {
            if(this.left) {
                return Direction.left;
            }
            if(this.right) {
                return Direction.right;
            }
            if(this.up) {
                return Direction.up;
            }
            if(this.down) {
                return Direction.down;
            }
            return null;
        }

        public void move(){
            for(int i = this.size(); i > 0; i--) {
                this.getCordsAt(i).setX(this.getCordsAt(i - 1).getX());
                this.getCordsAt(i).setY(this.getCordsAt(i - 1).getY());
            }

            Cords headCords = this.getCordsAt(0);
            if(this.left) {
                headCords.setX(headCords.getX() - DOT_SIZE);
            }
            if(this.right) {
                headCords.setX(headCords.getX() + DOT_SIZE);
            }
            if(this.up) {
                headCords.setY(headCords.getY() - DOT_SIZE);
            }
            if(this.down) {
                headCords.setY(headCords.getY() + DOT_SIZE);
            }
            this.moved = false;
        }

        public void checkCollisions() {
            Cords headCords = this.getCordsAt(0);

            // Столкновения с собой
            for(int i = this.size(); i > 3; i--) {
                Cords peaceCords = this.getCordsAt(i);
                if(headCords.getX() == peaceCords.getX() && headCords.getY() == peaceCords.getY()) {
                    this.alive = false;
                    break;
                }
            }

            // Выход за границы поля
            if(headCords.getX() >= WIDTH) {
                headCords.setX(-DOT_SIZE);
                this.changeDirection(Direction.right);
            } else if(headCords.getX() < 0) {
                headCords.setX(WIDTH - WIDTH % DOT_SIZE);
                this.changeDirection(Direction.left);
            } else if(headCords.getY() >= HEIGHT) {
                headCords.setY(-DOT_SIZE);
                this.changeDirection(Direction.down);
            } else if(headCords.getY() < 0) {
                headCords.setY(HEIGHT - HEIGHT % DOT_SIZE);
                this.changeDirection(Direction.up);
            }
        }

        public boolean checkCords(Cords cords) {
            return this.CORDS.contains(cords);
        }

        public void addCords(int x, int y) {
            this.CORDS.add(new Cords(x, y));
        }

        public Cords getCordsAt(int index) {
            return this.CORDS.get(index);
        }

        public ArrayList<Cords> getCORDS() {
            return CORDS;
        }

        public Image getICON() {
            return ICON;
        }

        public int size() {
            return this.CORDS.size() - 1;
        }
    }

    private static class Cords {
        private int x;
        private int y;

        public Cords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cords cords = (Cords) o;
            return this.x == cords.getX() && this.y == cords.getY();
        }

        @Override
        public int hashCode() {
            return this.x ^ this.y;
        }

        public int getX() {
            return this.x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}