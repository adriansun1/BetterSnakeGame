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



    //this will need to be changed to the correct path
    String filePath = "C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\";


    private Timer timer;
    private int moves = 0;
    private boolean isItAWin = false;
    //[delay] changes update speed. default is 100.
    protected int delay = 100;
    MovingMachine vehicle;
    Random rand = new Random();
    private int endGameInt;
    protected World world;
    protected GameWindow game;

    //snake and mouse coordinates and hard limits
    protected final int MOUSELIMIT = 8;
    protected final int SNAKELIMIT = 300;
    protected int[] snakeX = new int[SNAKELIMIT];
    protected int[] snakeY = new int[SNAKELIMIT];
    protected ArrayList<Integer> mouseX = new ArrayList<>();
    protected ArrayList<Integer> mouseY = new ArrayList<>();
    protected int mouseNum = 0;
    protected int snakeLength = 3;
    protected boolean[] mouseMoveState = new boolean[MOUSELIMIT];

    //mouse spawn and move numbers
    //[mouseDelay] changes rate that the mice spawn and move, delay will be [mouseDelay]*[delay]. default is 20.
    private int mouseDelay = 10;
    private int mouseDelayTime = 0;
    private int mouseSpawnPercentage = 50;
    private int mouseMovePercentage = 50;

    //car delay time
    //[carDelay] changes rate that the car is summoned. default 500
    protected int carDelayTime = 0;
    protected int carDelay = 150;
    protected int carWarningTime = 120;
    protected boolean carWarning = false;
    protected int leftOrRight;

    //rock spawn and limit
    protected final int ROCKLIMIT = 8;
    protected int[] rockX = new int[ROCKLIMIT];
    protected int[] rockY = new int[ROCKLIMIT];
    protected int[] rockType = new int[ROCKLIMIT];

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
    private BufferedImage mouseIdle;
    private BufferedImage mouseMove;
    private BufferedImage northMouth;
    private BufferedImage eastMouth;
    private BufferedImage southMouth;
    private BufferedImage westMouth;
    private BufferedImage northMouth1;
    private BufferedImage eastMouth1;
    private BufferedImage southMouth1;
    private BufferedImage westMouth1;
    private BufferedImage snakeBody;
    private BufferedImage vacuum;
    private BufferedImage car;
    private BufferedImage deathSign;
    private BufferedImage rock1;
    private BufferedImage rock2;
    private BufferedImage rock3;

    public SnakeLogic(World world, GameWindow game) {
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.world = world;
        this.game = game;
        world.addObserver(this);
        this.addKeyListener(world);
        initBoard();
    }

    private void initBoard() {
        try {
            bgGrass = ImageIO.read(new File(filePath + "bgGrass.png"));
//            bgDesert = ImageIO.read(new File("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\bgDesert.png"));
//            bgKitchen = ImageIO.read(new File("C:\\Users\\Adrian\\Desktop\\Programming\\Github\\BetterPrettySnake\\BetterSnakeGame\\src\\assets\\bgKitchen.png"));

            mouseIdle = ImageIO.read(new File(filePath + "mouseIdle.png"));
            mouseMove = ImageIO.read(new File(filePath + "mouseMove.png"));
            northMouth = ImageIO.read(new File(filePath + "northMouth.png"));
            northMouth1 = ImageIO.read(new File(filePath + "northMouth2.png"));
            eastMouth = ImageIO.read(new File(filePath + "eastMouth.png"));
            eastMouth1 = ImageIO.read(new File(filePath + "eastMouth2.png"));
            southMouth = ImageIO.read(new File(filePath + "southMouth.png"));
            southMouth1 = ImageIO.read(new File(filePath + "southMouth2.png"));
            westMouth = ImageIO.read(new File(filePath + "westMouth.png"));
            westMouth1 = ImageIO.read(new File(filePath + "westMouth2.png"));
            snakeBody = ImageIO.read(new File(filePath + "snakeBody.png"));
            car = ImageIO.read(new File(filePath + "car.png"));
            vacuum = ImageIO.read(new File(filePath + "vacuum.png"));
            deathSign = ImageIO.read(new File(filePath + "deathSign.png"));
            rock1 = ImageIO.read(new File(filePath + "rock1.png"));
            rock2 = ImageIO.read(new File(filePath + "rock2.png"));
            rock3 = ImageIO.read(new File(filePath + "rock3.png"));


        } catch (IOException ex) {
            this.setBackground(Color.BLACK);
            ex.printStackTrace();
        }

        timer = new Timer(delay, this);
        timer.start();
        Arrays.fill(mouseMoveState, false);

        // instantiate vehicle, also makes it so that the rocks dont spawn in path
        // if Kitchen
        int n = 0;
        switch (n) {
            case 0:
                vehicle = new Car(this);
                for (int i = 0; i < ROCKLIMIT; i++) {
                    do {
                        rockX[i] = Math.abs(rand.nextInt() % 26);
                        rockY[i] = Math.abs(rand.nextInt() % 26);
                        rockType[i] = Math.abs(rand.nextInt() % 4);
                    } while ((rockY[i] < 18 && rockY[i] > 7));
                }
                break;
//                case 1: MovingMachine vehicle = new Vacuum(this);
        }

    }

    //multiplies x,y coords by 25 in order for painting
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

    private boolean spotIsValid(int x, int y) {
        if (x > -2 && x < 27 && y > -2 && y < 26) {
            for (int i = 2; i < snakeLength; i++) {
                if (x == snakeX[i] && y == snakeY[i]) {
                    return false;
                }
                if (rockAtLocation(x, y)) {
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
        if (snakeLength < SNAKELIMIT) {
            growSnake();
            mouseDespawn(i);
            if (mouseNum == 0) {
                mouseSpawn();
            }
            return true;
        }
        return false;
    }

    protected void mouseDespawn(int i) {
        mouseX.remove(i);
        mouseY.remove(i);
        mouseNum--;
    }

    protected void growSnake() {
        snakeX[snakeLength] = snakeX[snakeLength - 1];
        snakeY[snakeLength] = snakeY[snakeLength - 1];
        snakeLength++;
    }

    protected boolean rockAtLocation(int x, int y) {
        for (int i = 0; i < ROCKLIMIT; i++) {
            if (x == rockX[i] && y == rockY[i]) {
                return true;
            }
        }
        return false;
    }


    private void initPaint() {
        snakeX[0] = 6;
        snakeX[1] = 5;
        snakeX[2] = 4;

        snakeY[0] = 10;
        snakeY[1] = 10;
        snakeY[2] = 10;
        east = true;
    }

    private void mousePaint(Graphics g) {
        for (int i = 0; i < mouseNum; i++) {
            if (mouseMoveState[i] == true) {
                g.drawImage(mouseMove, calc('m', i), calc('n', i), this);
            } else {
                g.drawImage(mouseIdle, calc('m', i), calc('n', i), this);
            }
        }

    }

    private void warningSignPaint(Graphics g) {
        if (carWarning) {
            if (leftOrRight == 1) {
                g.drawImage(deathSign, vehicle.calc(4), vehicle.calc(12), vehicle.calc(6), vehicle.calc(14), 0, 0, 32, 32, this);
            } else {
                g.drawImage(deathSign, vehicle.calc(24), vehicle.calc(12), vehicle.calc(22), vehicle.calc(14), 0, 0, 32, 32, this);
            }
        }
    }

    private void vehiclePaint(Graphics g) {
        if (vehicle.exists) {
            if (vehicle.isVac) {
                if (vehicle.left) {
                    vehicle.update();
                    g.drawImage(vacuum, vehicle.x2, vehicle.y1, vehicle.x1, vehicle.y2, 0, 0, 100, 70, this);
                } else if (vehicle.right) {
                    vehicle.update();
                    g.drawImage(vacuum, vehicle.x1, vehicle.y1, vehicle.x2, vehicle.y2, 0, 0, 100, 70, this);
                } else {
                    vehicle.update();
                    g.drawImage(vacuum, vehicle.x1, vehicle.y1, vehicle.x2, vehicle.y2, 0, 0, 100, 70, this);
                }
            } else {
                if (vehicle.left) {
                    vehicle.update();
                    g.drawImage(car, vehicle.x2, vehicle.y1, vehicle.x1, vehicle.y2, 0, 0, 100, 70, this);
                } else if (vehicle.right) {
                    vehicle.update();
                    g.drawImage(car, vehicle.x1, vehicle.y1, vehicle.x2, vehicle.y2, 0, 0, 100, 70, this);
                } else {
                    vehicle.update();
                    g.drawImage(car, vehicle.x1, vehicle.y1, vehicle.x2, vehicle.y2, 0, 0, 100, 70, this);
                }
            }
        }

    }

    private void rockPaint(Graphics g) {
        for (int i = 0; i < ROCKLIMIT; i++) {
            g.drawImage(randRock(rockType[i]), (rockX[i] * 25) + 20, (rockY[i] * 25) + 20, this);
        }

    }


    private Image randRock(int rock) {
        switch (rock) {
            default:
                return rock1;
            case 1:
                return rock2;
            case 2:
                return rock3;
        }
    }


    private void endGame() {
        Arrays.fill(snakeX,7);
        Arrays.fill(snakeY,7);
        snakeX[0] = 8;
        snakeX[1] = 9;
        snakeY[0] = 8;
        snakeY[1] = 9;
        stopMovement();
        vehicle.despawn();
        carDelayTime = -2;
        setVisible(false);
        ExitWindow exit = new ExitWindow(game, world);
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draws bg depending on the number called
        //NEEDS UPDATING
        g.drawImage(bgGrass, 0, 0, this);

        //paints initial snake position
        if (moves == 0) {
            initPaint();
        }

        for (int i = 0; i < snakeLength; i++) {
            if (i == 0) {
                if (north) {
                    g.drawImage(northMouth, calc('x', i), calc('y', i), this);
                }
                if (east) {
                    g.drawImage(eastMouth, calc('x', i), calc('y', i), this);
                }
                if (west) {
                    g.drawImage(westMouth, calc('x', i), calc('y', i), this);
                }
                if (south) {
                    g.drawImage(southMouth, calc('x', i), calc('y', i), this);
                }
            }
            if (i != 0) {
                g.drawImage(snakeBody, calc('x', i), calc('y', i), this);
            }
        }
        mousePaint(g);
        warningSignPaint(g);
        vehiclePaint(g);
        rockPaint(g);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void actionPerformed(ActionEvent e) {
        //snake head and body move
        if (north) {
            for (int i = snakeLength - 1; i > -1; i--) {
                if (i == 0) {
                    snakeY[0]--;
                } else bodyLocationUpdate(i);
            }
        }
        if (south) {
            for (int i = snakeLength - 1; i > -1; i--) {
                if (i == 0) {
                    snakeY[0]++;
                } else bodyLocationUpdate(i);
            }
        }
        if (east) {
            for (int i = snakeLength - 1; i > -1; i--) {
                if (i == 0) {
                    snakeX[0]++;
                } else bodyLocationUpdate(i);
            }
        }
        if (west) {
            for (int i = snakeLength - 1; i > -1; i--) {
                if (i == 0) {
                    snakeX[0]--;
                } else bodyLocationUpdate(i);
            }
        }

        if (!spotIsValid(snakeX[1], snakeY[1]) && moves >0) {
            endGame();
        }

        if (mouseX.contains(snakeX[0])) {
            for (int i = 0; i < mouseNum; i++) {
                if (mouseX.get(i) == snakeX[0] && mouseY.get(i) == snakeY[0]) {
                    eatBehavior(i);

                }
            }
        }

        //mouse spawn and move
        for (int i = 0; i < mouseNum; i++) {
            if (!spotIsValid(mouseX.get(i), mouseY.get(i))) {
                mouseDespawn(i);
            }
        }
        mouseDelayTime++;
        if (mouseDelayTime == mouseDelay) {
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

        //car behavior

        if (carDelayTime > -1) {
            carDelayTime++;
        }
        if (carDelayTime == carWarningTime) {
            leftOrRight = Math.abs(rand.nextInt() % 2);
        }
        if (carDelayTime > carWarningTime && carDelayTime < 141) {
            carWarning = true;
        } else {
            carWarning = false;
        }

        if (carDelay == carDelayTime) {
            switch (leftOrRight) {
                case 0:
                    vehicle.spawnLeft();
                    break;
                case 1:
                    vehicle.spawnRight();
                    break;
            }
            carDelayTime = 0;
        }

        //car movement
        if (vehicle.left) {
            vehicle.x--;
        } else if (vehicle.right) {
            vehicle.x++;
        }

        //car despawn and hitboxing
        if (vehicle.exists) {
            if (vehicle.x == vehicle.spawn2 || vehicle.x == vehicle.spawn1) {
                vehicle.despawn();
            }
            for (int snakeUnit = 0; snakeUnit < snakeLength; snakeUnit++) {
                if (calc('x', snakeUnit) >= vehicle.x1 && calc('x', snakeUnit) < vehicle.x2) {
                    if (calc('y', snakeUnit) >= vehicle.y1 && calc('y', snakeUnit) < vehicle.y2) {
                        endGame();
                    }
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
