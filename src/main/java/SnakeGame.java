import java.util.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel{

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
    
    Tile food;
    Random random;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);

        snakeHead = new Tile(5, 5);

        food = new Tile(10, 10);
        random = new Random();
        placefood();
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

        //Snake
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * titleSize, snakeHead.y * titleSize, titleSize, titleSize);
    }

    public void placefood(){
        food.x = random.nextInt(boardWidth/titleSize);
        food.y = random.nextInt(boardHeight/titleSize);
    }
    
}
