import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GameWindow extends JFrame implements Observer {

    private JTextField positionText;
    private Timer timer = null;
    protected SnakeLogic snakeFrame;
    private World world;

    GameWindow(World world) {
        this.world = world;
        world.addObserver(this);
        this.addKeyListener(world);
        initBoard();


    }

    private void initBoard() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("snek gem");
        setPreferredSize(new Dimension(700, 725));
        setMinimumSize(new Dimension(700, 725));
        setResizable(false);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        snakeFrame = new SnakeLogic(world);
        c.add(snakeFrame, BorderLayout.CENTER);

        //updates X,Y coords
        positionText = new JTextField();
        c.add(positionText, BorderLayout.SOUTH);
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                positionText.setText("position: (" + snakeFrame.calc('x', 0) + ", " + snakeFrame.calc('y', 0) + "" +
                        ")");
                repaint();
            }
        });
        timer.start();


        requestFocus();
        setVisible(true);
    }

    public void update(KeyEvent e) {
    }


    public static void main(String[] args) {
        World world = new World();
        GameWindow gem = new GameWindow(world);


    }
}
