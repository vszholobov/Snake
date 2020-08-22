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

public class GameField extends JPanel implements ActionListener {
    // Размер в пикселях одной клетки поля
    private final int DOT_SIZE = 16;

    // Размеры игрового поля
    private final int WIDTH;
    private final int HEIGHT;

    // Отвечает за скорость игры. Чем меньше, тем быстрее.
    private int speed = 35;

    private Image dot;
    private Image apple;

    private int appleX;
    private int appleY;

    private final ArrayList<Cords> SNAKE_CORDS = new ArrayList<>();

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;

    private boolean moveKeyPressed = false;
    private boolean inGame = true;

    public GameField(Dimension windowSize) {
        this.WIDTH = windowSize.width;
        this.HEIGHT = windowSize.height;

        this.setBackground(Color.black);
        this.loadImages();
        this.initGame();
        this.addKeyListener(new FieldKeyListener());
        this.setFocusable(true);

        // Удаление курсора
        this.setCursor( this.getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
    }

    public void initGame(){
        int snakeLength = 3;
        for(int i = 0; i < snakeLength; i++) {
            SNAKE_CORDS.add(new Cords(this.DOT_SIZE * 5, this.DOT_SIZE * 5));
        }

        Timer timer = new Timer(this.speed, this);
        timer.start();

        createApple();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void createApple(){
         while(true) {
             this.appleX = new Random().nextInt(this.WIDTH / this.DOT_SIZE) * this.DOT_SIZE;
             this.appleY = new Random().nextInt(this.HEIGHT / this.DOT_SIZE) * this.DOT_SIZE;
             Cords appleCords = new Cords(appleX, appleY);

             if(this.SNAKE_CORDS.contains(appleCords)) {
                 continue;
             }
             break;
        }
    }

    public void loadImages() {
        try {
            this.apple = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/apple.png"));
            this.dot = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/dot.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(this.inGame){
            g.drawImage(this.apple, this.appleX, this.appleY,this);
            for(Cords cords : this.SNAKE_CORDS) {
                g.drawImage(this.dot, cords.getX(), cords.getY(), this);
            }
        } else{
            this.gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String text = "Game Over";

        g.setColor(Color.white);
        Font font = new Font("Arial",Font.PLAIN, 50);
        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics(font);

        int x = (this.WIDTH - metrics.stringWidth(text)) / 2;
        int y = ((this.HEIGHT - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
    }

    public void moveSnake(){
        for(int i = this.SNAKE_CORDS.size() - 1; i > 0; i--) {
            this.SNAKE_CORDS.get(i).setX(this.SNAKE_CORDS.get(i - 1).getX());
            this.SNAKE_CORDS.get(i).setY(this.SNAKE_CORDS.get(i - 1).getY());
        }

        Cords headCords = this.SNAKE_CORDS.get(0);
        if(left) {
            headCords.setX(headCords.getX() - this.DOT_SIZE);
        }
        if(right) {
            headCords.setX(headCords.getX() + this.DOT_SIZE);
        }
        if(up) {
            headCords.setY(headCords.getY() - this.DOT_SIZE);
        }
        if(down) {
            headCords.setY(headCords.getY() + this.DOT_SIZE);
        }
        this.moveKeyPressed = false;
    }

    public void checkApple(){
        Cords headCords = this.SNAKE_CORDS.get(0);
        if(headCords.getX() == this.appleX && headCords.getY() == this.appleY) {
            this.SNAKE_CORDS.add(new Cords(-1,-1));
            this.createApple();
        }
    }

    public void checkCollisions(){
        Cords headCords = this.SNAKE_CORDS.get(0);

        // Столкновения с собой
        for(int i = this.SNAKE_CORDS.size() - 1; i > 3; i--) {
            Cords peaceCords = this.SNAKE_CORDS.get(i);
            if(headCords.getX() == peaceCords.getX() && headCords.getY() == peaceCords.getY()) {
                this.inGame = false;
                break;
            }
        }

        // Выход за границы поля
        if(headCords.getX() >= this.WIDTH) {
            headCords.setX(-this.DOT_SIZE);
            this.down = false;
            this.up = false;
            this.left = false;
            this.right = true;
        } else if(headCords.getX() < 0) {
            headCords.setX(this.WIDTH - this.WIDTH % this.DOT_SIZE);
            this.down = false;
            this.up = false;
            this.left = true;
            this.right = false;
        } else if(headCords.getY() >= this.HEIGHT) {
            headCords.setY(-this.DOT_SIZE);
            this.down = true;
            this.up = false;
            this.left = false;
            this.right = false;
        } else if(headCords.getY() < 0) {
            headCords.setY(this.HEIGHT - this.HEIGHT % this.DOT_SIZE);
            this.down = false;
            this.up = true;
            this.left = false;
            this.right = false;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if(this.inGame){
            this.checkApple();
            this.checkCollisions();
            this.moveSnake();
        }
        this.repaint();
    }

    class FieldKeyListener extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            if(!moveKeyPressed) {
                super.keyPressed(e);
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_LEFT && !right) {
                    left = true;
                    up = false;
                    down = false;
                }
                if (key == KeyEvent.VK_RIGHT && !left) {
                    right = true;
                    up = false;
                    down = false;
                }
                if (key == KeyEvent.VK_UP && !down) {
                    right = false;
                    up = true;
                    left = false;
                }
                if (key == KeyEvent.VK_DOWN && !up) {
                    right = false;
                    down = true;
                    left = false;
                }
                moveKeyPressed = true;
            }
        }
    }

    static class Cords {
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