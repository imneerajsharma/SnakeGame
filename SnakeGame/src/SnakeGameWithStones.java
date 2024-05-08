
import java.awt.*;
        import java.awt.event.*;
        import java.util.ArrayList;
        import java.util.Random;
        import javax.swing.*;

public class SnakeGameWithStones extends JPanel implements ActionListener, KeyListener {

    private class Tile {
        int x;
        int y;

        Tile(int _x, int _y) {
            this.x = _x;
            this.y = _y;
        }
    }

    private class Stone {
        int x;
        int y;

        Stone(int _x, int _y) {
            this.x = _x;
            this.y = _y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 15;

    // Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // Food
    Tile food;

    // Stones
    ArrayList<Stone> stones;

    // Random generators
    Random random_for_Food;
    Random random_for_Snake;
    Random random_for_Stones;

    // Game logic
    Timer gameLoop;
    int velocity_X;
    int velocity_Y;
    boolean gameOver = false;

    SnakeGameWithStones(int _boardWidth, int _boardHeight) {
        this.boardWidth = _boardWidth;
        this.boardHeight = _boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.darkGray);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        stones = new ArrayList<Stone>();

        food = new Tile(10, 10);
        random_for_Food = new Random();
        random_for_Snake = new Random();
        random_for_Stones = new Random();

        placeFood();
        initializeStones();

        velocity_X = 0;
        velocity_Y = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Food
        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // Snake Head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // Stones
        g.setColor(Color.gray);
        for (Stone stone : stones) {
            g.fill3DRect(stone.x * tileSize, stone.y * tileSize, tileSize, tileSize, true);
        }

        // Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        } else {
            g.drawString("Score : " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        food.x = random_for_Food.nextInt((boardWidth - 200) / tileSize); // 500/25 = 20 (x coordinate = 0 ---> 20)
        food.y = random_for_Food.nextInt((boardHeight - 200) / tileSize); // (y coordinate = 0 ---> 20)
    }

    public void initializeStones() {
        stones = new ArrayList<>();
        int maxStones = 15; // Maximum number of stones
        int numStones = 0; // Counter for the number of stones generated
        while (numStones < maxStones) {
            int stoneX = random_for_Stones.nextInt(boardWidth / tileSize);
            int stoneY = random_for_Stones.nextInt(boardHeight / tileSize);
            // Ensure stones don't overlap with snake or food
            if (!collision(new Tile(stoneX, stoneY), snakeHead) && !collision(new Tile(stoneX, stoneY), food)) {
                stones.add(new Stone(stoneX, stoneY));
                numStones++;
            }
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // Eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // Check collision with stones
        for (Stone stone : stones) {
            if (collision(snakeHead, new Tile(stone.x, stone.y))) {
                gameOver = true;
            }
        }

        // Snake Body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Move Snake Head
        snakeHead.x += velocity_X;
        snakeHead.y += velocity_Y;

        // Game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // Collide with the snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0
                || snakeHead.y * tileSize > boardWidth) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocity_Y != 1) {
            velocity_X = 0;
            velocity_Y = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocity_Y != -1) {
            velocity_X = 0;
            velocity_Y = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocity_X != 1) {
            velocity_X = -1;
            velocity_Y = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocity_X != -1) {
            velocity_X = 1;
            velocity_Y = 0;
        }
    }

    // Do not need these functions
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
