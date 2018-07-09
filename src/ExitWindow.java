import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class ExitWindow extends JFrame implements Observer {

    private JButton exitButton;
    private JButton restartButton;
    private JTextField messageTextField;

    ExitWindow(GameWindow game, World world) {
        initBoard(game);
        world.addObserver(this);
        this.addKeyListener(world);
        game.dispose();
    }

    //IN THE FUTURE
    //USE A TEMPLATE ABSTRACT FOR THIS
    private void initBoard(GameWindow game) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("tanks for playin");
        setPreferredSize(new Dimension(400, 200));
        setMinimumSize(new Dimension(400, 200));
        setResizable(false);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        exitButton = new JButton("EXIT");
        c.add(exitButton, BorderLayout.SOUTH);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        restartButton = new JButton("AGAIN");
        c.add(restartButton, BorderLayout.EAST);
        restartButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                World world = new World();
                GameWindow game = new GameWindow(world);
                dispose();
            }
        });

        messageTextField = new JTextField("THANKS FOR PLAYING. YOUR SCORE IS: " + game.snakeFrame.snakeLength);
        messageTextField.setEditable(false);
        c.add(messageTextField, BorderLayout.CENTER);

        requestFocus();
        setVisible(true);
    }

    public void update(KeyEvent e) {


    }


}

