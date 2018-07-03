import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Random;

public class TesterWindow extends JFrame implements ActionListener, Observer {
    public GameWindow game;
    public SnakeLogic snake;

    public TesterWindow(GameWindow game, SnakeLogic snake, World world) {

        world.addObserver(this);
        this.game = game;
        this.snake = snake;


        initBoard(snake);


    }

    public void initBoard(SnakeLogic snake) {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 500));
        setMinimumSize(new Dimension(500, 500));
        setTitle("Testing Window");

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        JPanel coordPanel = new JPanel();
        coordPanel.setMinimumSize(new Dimension(100, 500));
        coordPanel.setPreferredSize(new Dimension(100, 500));

        c.add(coordPanel, BorderLayout.EAST);
        coordPanel.setLayout(new BoxLayout(coordPanel, BoxLayout.PAGE_AXIS));
        JTextArea coordField = new JTextArea("");
        coordPanel.add(coordField);
        coordField.setEditable(false);

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                coordField.setText("Coordinates: \n");
                coordField.append("Snake:  (" + snake.snakeX[0] + ", " + snake.snakeY[0] + ")\n");
                for (int i = 0; i < snake.mouseNum; i++) {
                    coordField.append("Mouse" + (i + 1) + ": (" + snake.mouseX.get(i) + ", " + snake.mouseY.get(i) + ")\n");
                }

            }
        });
        timer.start();
        setVisible(true);


    }

    public static void main(String[] args) {
        World world = new World();
        GameWindow game = new GameWindow(world);
        TesterWindow test = new TesterWindow(game,game.snakeFrame,world);
    }

    public void update(KeyEvent e) {
        //BASICALLY CHEATCODES. for testing and fun
        if (e.getKeyCode() == KeyEvent.VK_1) {
            //STOP MOVEMENT
            snake.stopMovement();
        }
        if (e.getKeyCode() == KeyEvent.VK_2) {
            //SPAWN MOUSE
            if (snake.mouseNum < snake.MOUSELIMIT) {
                snake.mouseSpawn();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_3) {
            //GROW SNAKE
            snake.growSnake();
        }
        if (e.getKeyCode() == KeyEvent.VK_4) {
            //FILL SNAKE
            for(int i = snake.snakeLength; i<snake.SNAKELIMIT; i++){
                snake.growSnake();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_5) {
            //SUMMON CAR/VACUUM LEFT
            snake.vehicle.spawnLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_6) {
            //SUMMON CAR/VACUUM RIGHT
            snake.vehicle.spawnRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_7) {

        }
        if (e.getKeyCode() == KeyEvent.VK_8) {

        }
        if (e.getKeyCode() == KeyEvent.VK_9) {
            //KILLS WINDOWS
            game.dispose();

        }


    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
