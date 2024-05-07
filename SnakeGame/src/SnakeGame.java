import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener ,KeyListener{

    private class Tile{
        int x;
        int y;
        Tile(int _x, int _y){
            this.x=_x;
            this.y=_y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random_for_Food;
    Random random_for_Snake;

    //Game logic
    Timer gameLoop;
    int velocity_X;
    int velocity_Y;


    SnakeGame(int _boardWidth, int _boardHeight){
        this.boardWidth=_boardWidth;
        this.boardHeight=_boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.darkGray);
        addKeyListener(this);
        setFocusable(true);

        snakeHead=new Tile(5,5);
        snakeBody =new ArrayList<Tile>();


        food=new Tile(10,10);
        random_for_Food=new Random();
        placeFood();
        //placeFood2();
        //placeFood_from_centre();
        random_for_Snake=new Random();
        placeSnake();

        velocity_X=0;
        velocity_Y=0;

        gameLoop=new Timer(100,this);
        gameLoop.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        //Grid
        for(int i=0;i<boardWidth/tileSize;i++){
            // (x1, y1, x2,y2)
            g.drawLine(i*tileSize,0,i*tileSize,boardHeight); // Vertical Line
            g.drawLine(0,i*tileSize,boardWidth,i*tileSize); // Horizontal Lines

        }

        //Food
        g.setColor(Color.red);
        g.fillRect(food.x* tileSize , food.y*tileSize,tileSize,tileSize);

        // Snake Head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);

        // snake Body
        for(int i=0;i<snakeBody.size();i++){
            Tile snakePart=snakeBody.get(i);
            g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize,tileSize,tileSize );
        }
    }
    public void placeFood(){
        food.x=random_for_Food.nextInt((boardWidth-200)/tileSize); // 500/25 =24   (x coordinate = 0 --->  24 )
        food.y=random_for_Food.nextInt((boardWidth-200)/tileSize); //              (y coordinate = 0 --->  24 )

    }
//    public void placeFood2(){
////        // Calculate the maximum range of valid coordinates, leaving some space from the edges
////        int maxX = (boardWidth - 200) / tileSize; // Adjust as needed
////        int maxY = (boardHeight - 200) / tileSize; // Adjust as needed
////
////        // Generate random coordinates within the valid range
////        food.x = 1 + random_for_Food.nextInt(maxX - 1); // Avoid placing food on the first column
////        food.y = 1 + random_for_Food.nextInt(maxY - 1); // Avoid placing food on the first row
//
//
//        // Calculate the maximum range of valid coordinates, avoiding the first and last rows and columns
//        int maxX = (boardWidth - 200) / tileSize - 2; // Adjust as needed
//        int maxY = (boardHeight - 200) / tileSize - 2; // Adjust as needed
//
//        // Generate random coordinates within the valid range
//        food.x = 1 + random_for_Food.nextInt(maxX - 1); // Avoid placing food on the first or last column
//        food.y = 1 + random_for_Food.nextInt(maxY - 1); // Avoid placing food on the first or last row
//    }
//
//    public void placeFood_from_centre(){
//        // Calculate the center coordinates of the board
//        int centerX = boardWidth / 2 / tileSize;
//        int centerY = boardHeight / 2 / tileSize;
//
//        // Define the range from the center within which food can be generated
//        int range = 10; // Adjust as needed
//
//        // Generate random coordinates within the specified range around the center
//        food.x = centerX + random_for_Food.nextInt(range * 2 + 1) - range; // Random x within [centerX - range, centerX + range]
//        food.y = centerY + random_for_Food.nextInt(range * 2 + 1) - range; // Random y within [centerY - range, centerY + range]
//
//        // Ensure the generated coordinates are within the bounds of the board
//        food.x = Math.max(1, Math.min(food.x, (boardWidth / tileSize) - 2)); // Ensure food.x is within [1, width-2] to avoid edges
//        food.y = Math.max(1, Math.min(food.y, (boardHeight / tileSize) - 2)); // Ensure food.y is within [1, height-2] to avoid edges
//    }


    public void placeSnake(){
        snakeHead.x=random_for_Snake.nextInt(boardWidth/tileSize); // 600/25 =24   (x coordinate = 0 --->  24 )
        snakeHead.y=random_for_Snake.nextInt(boardWidth/tileSize); //              (y coordinate = 0 --->  24 )
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x==tile2.x && tile1.y==tile2.y;
    }

    public void move(){
        // eat food
        if(collision(snakeHead , food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }


        //Snake Body
        for(int i=snakeBody.size()-1; i>=0; i--){
            Tile snakePart =snakeBody.get(i);
            if(i==0){
                snakePart.x= snakeHead.x;
                snakePart.y= snakeHead.y;
            }
            else{
                Tile prevSnakePart=snakeBody.get(i-1);
                snakePart.x= prevSnakePart.x;
                snakePart.y= prevSnakePart.y;
            }
        }

        //Snake Head
        snakeHead.x +=velocity_X;
        snakeHead.y +=velocity_Y;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP && velocity_Y!=1){
           velocity_X=0;
           velocity_Y=-1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocity_Y!=-1){
            velocity_X=0;
            velocity_Y=1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocity_X!=1){
            velocity_X=-1;
            velocity_Y=0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocity_X!=-1){
            velocity_X=1;
            velocity_Y=0;
        }
    }

    // donot need this function
    @Override
    public void keyTyped(KeyEvent e) {
    }
    // donot need this function
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
