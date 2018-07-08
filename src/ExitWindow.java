import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ExitWindow extends JFrame implements Observer {

    private JPanel buttonPanel;
    private JButton againButton;
    private JButton exitButton;
    private JTextField messageTextField;

    ExitWindow(SnakeLogic snake, World world) {
        initBoard(snake);
        world.addObserver(this);
        this.addKeyListener(world);

    }

    //IN THE FUTURE
    //USE A TEMPLATE ABSTRACT FOR THIS
    private void initBoard(SnakeLogic snake) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("tanks for playin");
        setPreferredSize(new Dimension(400, 200));
        setMinimumSize(new Dimension(400, 200));
        setResizable(false);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        buttonPanel = new JPanel();
        c.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new BorderLayout());

        againButton = new JButton("AGAIN");
        exitButton = new JButton("AGAIN");
        buttonPanel.add(againButton, BorderLayout.WEST);
        buttonPanel.add(exitButton, BorderLayout.EAST);

        messageTextField = new JTextField("THANKS FOR PLAYING. YOUR SCORE IS: " + snake.snakeLength);
        messageTextField.setEditable(false);
        c.add(messageTextField, BorderLayout.CENTER);

        requestFocus();
        setVisible(true);
    }

    public void update(KeyEvent e) {


    }


}

