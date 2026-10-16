import java.util.*;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{

    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int titleSize = 25; 

    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    
    Tile food;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placefood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        //Grid
        for(int i = 0; i < boardWidth/titleSize; i++){
            //(x1, y1, x2, y2)
            g.drawLine(i*titleSize, 0, i*titleSize, boardHeight); //vertical lines
            g.drawLine(0, i*titleSize, boardWidth, i*titleSize); //Horizontal lines
        }

        //food
        g.setColor(Color.red);
        g.fillRect(food.x * titleSize, food.y * titleSize, titleSize, titleSize);

        //Snake head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * titleSize, snakeHead.y * titleSize, titleSize, titleSize);

        //Snake body
        for (int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * titleSize, snakePart.y * titleSize, titleSize, titleSize);
        }
    }

    public void placefood(){
        food.x = random.nextInt(boardWidth/titleSize);
        food.y = random.nextInt(boardHeight/titleSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return  tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){

        // eat food
        if (collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placefood();
        }
        
        //snake body
        for (int i = snakeBody.size()-1; i >= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if (i ==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }


        //snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i =0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)){gameOver = true;}
        }

        if (snakeHead.x*titleSize < 0 || snakeHead.x*titleSize > boardWidth ||
            snakeHead.y*titleSize < 0 || snakeHead.y*titleSize > boardHeight){
            gameOver = true;
        }
    }
    
    @Override 
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
    }

    //do not need these methods
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
