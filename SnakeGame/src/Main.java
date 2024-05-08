import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        int boardWidth =800;
        int boardHeight=boardWidth;

        JFrame frame =new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        SnakeGame snakeGame=new SnakeGame(boardWidth,boardHeight);
//        frame.add(snakeGame);
//
//        frame.pack();
//        snakeGame.requestFocus();

        // with Stones
        snakeMultiLevel _snakeMultiLevel=new snakeMultiLevel(boardWidth,boardHeight);
      frame.add(_snakeMultiLevel);

        frame.pack();
        _snakeMultiLevel.requestFocus();


    }
}