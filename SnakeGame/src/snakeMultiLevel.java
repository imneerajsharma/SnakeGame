
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


    public class snakeMultiLevel extends JPanel implements ActionListener, KeyListener {

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
        int currentLevel = 1; // Current level
        int foodsEaten = 0; // Foods eaten in the current level
        int totalScore = 0; // Cumulative score across levels

        snakeMultiLevel(int _boardWidth, int _boardHeight) {
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

            initializeLevel(currentLevel);

            velocity_X = 0;
            velocity_Y = 0;

            gameLoop = new Timer(200, this);
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
            g.setColor(Color.white);
            g.drawString("Score: " + String.valueOf(totalScore), tileSize, tileSize);

            // Level completed messages
            if (foodsEaten == 5) {
                g.setColor(Color.yellow);
                g.drawString("LEVEL COMPLETED", tileSize, tileSize * 2);
            } else if (foodsEaten == 10) {
                g.setColor(Color.yellow);
                g.drawString("LEVEL COMPLETED", tileSize, tileSize * 2);
            } else if (foodsEaten == 15) {
                g.setColor(Color.yellow);
                g.drawString("LEVEL COMPLETED", tileSize, tileSize * 2);
            }

            // Game Over message
            if (gameOver) {
                g.setColor(Color.red);
                g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize, tileSize * 3);
            }
        }

        public void initializeLevel(int level) {
            snakeBody.clear();
            stones.clear();
            foodsEaten = 0;

            placeFood();
            initializeStones(level);

            snakeHead.x = 5;
            snakeHead.y = 5;

            velocity_X = 0;
            velocity_Y = 0;

            gameOver = false;
        }

        public void placeFood() {
            food.x = random_for_Food.nextInt((boardWidth - 200) / tileSize); // 500/25 = 20 (x coordinate = 0 ---> 20)
            food.y = random_for_Food.nextInt((boardHeight - 200) / tileSize); // (y coordinate = 0 ---> 20)
        }

        public void initializeStones(int level) {
            int maxStones = 15 + 5 * (level - 1); // Increase stones by 5 for each level
            int numStones = 0;
            while (numStones < maxStones) {
                int stoneX = random_for_Stones.nextInt(boardWidth / tileSize);
                int stoneY = random_for_Stones.nextInt(boardHeight / tileSize);
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
                foodsEaten++;
                totalScore++; // Increment total score
            }

            // Check collision with stones
            for (Stone stone : stones) {
                if (collision(snakeHead, new Tile(stone.x, stone.y))) {
                    gameOver = true;
                }
            }

            // Snake Body
            for (int i = snakeBody.size() - 1; i > 0; i--) {
                Tile snakePart = snakeBody.get(i);
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
            if (snakeBody.size() > 0) {
                Tile firstBodyPart = snakeBody.get(0);
                firstBodyPart.x = snakeHead.x;
                firstBodyPart.y = snakeHead.y;
            }

            // Move Snake Head
            snakeHead.x += velocity_X;
            snakeHead.y += velocity_Y;

            // Game over conditions
            for (int i = 0; i < snakeBody.size(); i++) {
                Tile snakePart = snakeBody.get(i);
                if (collision(snakeHead, snakePart)) {
                    gameOver = true;
                }
            }
            if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0
                    || snakeHead.y * tileSize > boardWidth) {
                gameOver = true;
            }

//            // Check level completion
//            if (foodsEaten == 5 || foodsEaten == 10 || foodsEaten == 15) {
//                currentLevel++;
//                if (currentLevel <= 3) {
//                    initializeLevel(currentLevel);
//                } else {
//                    // All levels completed
//                    System.out.println("Jackpot");
//                    gameLoop.stop();
//                }
//            }
            // Check level completion
            if (foodsEaten == 5) {
                System.out.println("LEVEL 1 COMPLETED");
            } else if (foodsEaten == 10) {
                System.out.println("LEVEL 2 COMPLETED");
            } else if (foodsEaten == 15) {
                System.out.println("All levels cleared");
                currentLevel++;
                if (currentLevel <= 3) {
                    initializeLevel(currentLevel);
                } else {
                    // All levels completed
                    System.out.println("Jackpot");
                    gameLoop.stop();
                }
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
