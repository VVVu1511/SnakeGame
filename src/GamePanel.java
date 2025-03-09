import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;


public class GamePanel extends JPanel implements ActionListener{
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    
    int obstacleX;
    int obstacleY;
    int bodyParts = 6;
    int appleEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        newObstacle();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void newObstacle(){
        obstacleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE);
        obstacleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(running){
            g.setColor(Color.red);
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);

            g.setColor(Color.blue);
            g.fillRect(obstacleX, obstacleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.green);
                }
                else{
                    g.setColor(new Color(45,180,0));
                }
                g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
            }

            helpUI(g, Color.red, 40,"Score: " + appleEaten);
                
        }
        else{
            gameOver(g);
        }
    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }

    public void checkCollision(){
        //checks if head collides with body

        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        //check if we collides with obstacle
        if(x[0] == obstacleX && y[0] == obstacleY){
            running = false;
        }

        //check if head touches left border
        if(x[0] < 0){
            running = false;
        }

        if(x[0] > SCREEN_WIDTH){
            running = false;
        }

        if(y[0] < 0){
            running = false;
        }

        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }


    public void helpUI(Graphics g,Color color, int font_size,String description){
        g.setColor(color);
        g.setFont(new Font("Ink Free",Font.BOLD,font_size));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString(description, (SCREEN_WIDTH - metrics1.stringWidth(description) ) / 2, g.getFont().getSize());
        
    }

    public void gameOver(Graphics g){
        helpUI(g, Color.red, 40, "Score: " + appleEaten);
        helpUI(g, Color.red, 100, "Game Over");
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(running == true){
            move();
            checkApple();
            checkCollision();
        }
        
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
