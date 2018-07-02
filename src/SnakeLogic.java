import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TimerTask;

public class SnakeLogic extends JPanel implements ActionListener, Observer {


    private Timer timer;
    private int moves = 0;
    private boolean isItAWin = false;
    //[delay] changes update speed. default is 100.
    protected int delay = 100;
    private long mouseTime = 0;

    //snake and mouse coordinates and hard limits
    protected final int MOUSELIMIT = 4;
    protected final int SNAKELIMIT = 50;
    protected int[] snakeX = new int[SNAKELIMIT];
    protected int[] snakeY = new int[SNAKELIMIT];
    protected ArrayList<Integer> mouseX = new ArrayList<>();
    protected ArrayList<Integer> mouseY = new ArrayList<>();
    protected int mouseNum = 0;
    protected int snakeLength = 3;
    protected boolean[] mouseMoveState = new boolean[MOUSELIMIT];

    //mouse spawn and move numbers
    //[mouseDelay] changes rate that the mice spawn and move, delay will be [mouseDelay]*[mouseTime]. default is 20.
    private int mouseDelay = 20;
    private int mouseDelayTime = 0;
    private int mouseSpawnPercentage = 50;
    private int mouseMovePercentage = 50;

    //snake movement and facing direction
    private boolean north = false;
    private boolean south = false;
    private boolean east = false;
    private boolean west = false;

    //Artwork
    // background images *randomize eventually*
    private BufferedImage bgGrass;
    private BufferedImage bgDesert;
    private BufferedImage bgKitchen;

    // snake and mouse art
    private ImageIcon mouseIdle;
    private ImageIcon mouseMove;
    private ImageIcon northMouth;
    private ImageIcon eastMouth;
    private ImageIcon southMouth;
    private ImageIcon westMouth;
    private ImageIcon northMouth1;
    private ImageIcon eastMouth1;
    private ImageIcon southMouth1;
    private ImageIcon westMouth1;
    private ImageIcon snakeBody;

    public SnakeLogic(World world) {
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        world.addObserver(this);
        this.addKeyListener(world);
        initBoard();
    }

    private void initBoard() {
        try {
            bgGrass = ImageIO.read(new File("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\bgGrass.png"));
//            bgDesert = ImageIO.read(new File("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\bgDesert.png"));
//            bgKitchen = ImageIO.read(new File("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\bgKitchen.png"));

            mouseIdle = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\mouseIdle.png");
            mouseMove = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\mouseMove.png");
            northMouth = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\northMouth.png");
            northMouth1 = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\northMouth1.png");
            eastMouth = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\eastMouth.png");
            eastMouth1 = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\eastMouth1.png");
            southMouth = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\southMouth.png");
            southMouth1 = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\southMouth1.png");
            westMouth = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\westMouth.png");
            westMouth1 = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\westMouth1.png");
            snakeBody = new ImageIcon("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\snakeBody.png");

        } catch (IOException ex) {
            this.setBackground(Color.BLACK);
            ex.printStackTrace();
        }

        timer = new Timer(delay, this);
        timer.start();
        Arrays.fill(mouseMoveState, false);
    }

    //multiplies x,y coords in order for painting
    protected int calc(char x, int i) {
        // for snake: x,y
        //for mouse: m,n
        if (x == 'x') {
            return ((snakeX[i] * 25) + 25);
        }
        if (x == 'y') {
            return ((snakeY[i] * 25) + 25);
        }
        if (x == 'm') {
            return ((mouseX.get(i) * 25) + 25);
        }
        if (x == 'n') {
            return ((mouseY.get(i) * 25) + 25);
        }
        System.out.println("calc error");
        return 1;
    }

    protected void stopMovement() {
        north = false;
        south = false;
        east = false;
        west = false;
    }

    private void bodyLocationUpdate(int i) {
        snakeY[i] = snakeY[i - 1];
        snakeX[i] = snakeX[i - 1];
    }

    //if spot is not occupied by snake or boundaries
    private boolean spotIsValid(int x, int y) {
        if (x > 0 && x < 27 && y > 0 && y < 26) {
            for (int i = 0; i < snakeLength; i++) {
                if (x == snakeX[i] && y == snakeY[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean mouseSpotIsValid(int x, int y) {
        if (spotIsValid(x, y)) {
            for (int i = 0; i < mouseNum; i++) {
                if (x == mouseX.get(i) && y == mouseY.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    protected void mouseSpawn() {
        Random rand = new Random();
        int x = 1;
        int y = 1;
        do {
            x = Math.abs(rand.nextInt() % 26);
            y = Math.abs(rand.nextInt() % 26);
        }
        while (!(mouseSpotIsValid(x, y)));
        mouseX.add(x);
        mouseY.add(y);
        mouseNum++;
    }


    private void mouseMove(int i) {
        Random rand = new Random();
        int n = Math.abs(rand.nextInt() % 4);
        switch (n) {
            case 0:
                if (mouseSpotIsValid(mouseX.get(i) + 1, mouseY.get(i))) {
                    mouseX.set(i, mouseX.get(i) + 1);
                }
                break;
            case 1:
                if (mouseSpotIsValid(mouseX.get(i) - 1, mouseY.get(i))) {
                    mouseX.set(i, mouseX.get(i) - 1);
                }
                break;
            case 2:
                if (mouseSpotIsValid(mouseX.get(i), mouseY.get(i) + 1)) {
                    mouseY.set(i, mouseY.get(i) + 1);
                }
                break;
            case 3:
                if (mouseSpotIsValid(mouseX.get(i), mouseY.get(i) - 1)) {
                    mouseY.set(i, mouseY.get(i) - 1);
                }
                break;
        }
    }

    protected boolean eatBehavior(int i) {
        if(snakeLength<SNAKELIMIT) {
            mouseX.remove(i);
            mouseY.remove(i);
            mouseNum--;
            growSnake();
            return true;
        }
        return false;
    }
    protected void growSnake(){
        snakeX[snakeLength] = snakeX[snakeLength - 1];
        snakeY[snakeLength] = snakeY[snakeLength - 1];
        snakeLength++;
    }

    private void endGame() {
        System.out.println("game over");
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draws bg depending on the number called
        //NEEDS UPDATING
        g.drawImage(bgGrass, 0, 0, this);

        //paints initial snake position
        if (moves == 0) {
            snakeX[0] = 6;
            snakeX[1] = 5;
            snakeX[2] = 4;

            snakeY[0] = 4;
            snakeY[1] = 4;
            snakeY[2] = 4;
            east = true;
        }

        //paints snake head and body
        for (int i = 0; i < snakeLength; i++) {
            if (i == 0) {
                if (north) {
                    northMouth.paintIcon(this, g, calc('x', i), calc('y', i));
                }
                if (east) {
                    eastMouth.paintIcon(this, g, calc('x', i), calc('y', i));
                }
                if (west) {
                    westMouth.paintIcon(this, g, calc('x', i), calc('y', i));
                }
                if (south) {
                    southMouth.paintIcon(this, g, calc('x', i), calc('y', i));
                }
            }
            if (i != 0) {
                snakeBody.paintIcon(this, g, calc('x', i), calc('y', i));
            }
        }

        //paints mice
        for (int i = 0; i < mouseNum; i++) {
            if (mouseMoveState[i] == true) {
                mouseMove.paintIcon(this, g, calc('m', i), calc('n', i));
            } else {
                mouseIdle.paintIcon(this, g, calc('m', i), calc('n', i));
            }
        }


    }

    @Override
    @SuppressWarnings("Duplicates")
    public void actionPerformed(ActionEvent e) {
//        timer.start();

        //snake head and body move
        if (north) {
            for (int i = snakeLength - 1; i > -1; i--) {
                if (i == 0) {
                    if (!spotIsValid(snakeX[0], snakeY[0] - 1)) {
                        endGame();
                    }
                    snakeY[0]--;
                } else bodyLocationUpdate(i);
            }
        }
        if (south) {
            for (int i = snakeLength - 1; i > -1; i--) {
                if (i == 0) {
                    if (!spotIsValid(snakeX[0], snakeY[0] + 1)) {
                        endGame();
                    }
                    snakeY[0]++;
                } else bodyLocationUpdate(i);
            }
        }
        if (east) {
            for (int i = snakeLength - 1; i > -1; i--) {
                if (i == 0) {
                    if (!spotIsValid(snakeX[0] + 1, snakeY[0])) {
                        endGame();
                    }
                    snakeX[0]++;
                } else bodyLocationUpdate(i);
            }
        }
        if (west) {
            for (int i = snakeLength - 1; i > -1; i--) {
                if (i == 0) {
                    if (!spotIsValid(snakeX[0] - 1, snakeY[0])) {
                        endGame();
                    }
                    snakeX[0]--;
                } else bodyLocationUpdate(i);
            }
        }

        //mouse spawn and move
        mouseDelayTime++;
        if (mouseDelayTime == mouseDelay) {
            Random rand = new Random();
            if (Math.abs(rand.nextInt() % 100) < mouseSpawnPercentage && mouseNum < MOUSELIMIT) {
                mouseSpawn();
            }
            for (int i = 0; i < mouseNum; i++) {
                if (Math.abs(rand.nextInt() % 100) < mouseMovePercentage) {
                    mouseMove(i);
                }
            }
            mouseDelayTime = 0;
        }


        //eating behavior
        if (mouseX.contains(snakeX[0])) {
            for (int i = 0; i < mouseNum; i++) {
                if (mouseX.get(i) == snakeX[0] && mouseY.get(i) == snakeY[0]) {
                    eatBehavior(i);

                }
            }
        }


        repaint();
    }

    //changes direction that the snake is facing
    public void update(KeyEvent e) {

        //WILL CLOSE GAME AND WINDOW
//        if (e.getKeyCode() == KeyEvent.VK_1) {
//            JComponent comp = (JComponent) e.getSource();
//            Window win = SwingUtilities.getWindowAncestor(comp);
//            win.dispose();
//            System.out.println("goodbye");
//        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (!south) {
                moves++;
                stopMovement();
                north = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (!north) {
                moves++;
                stopMovement();
                south = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (!west) {
                moves++;
                stopMovement();
                east = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (!east) {
                moves++;
                stopMovement();
                west = true;
            }
        }

    }
}
