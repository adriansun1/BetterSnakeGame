import java.util.Random;

public abstract class MovingMachine {


    protected int x;
    protected int y = 13;
    protected int spawn1 = 30;
    protected int spawn2 = -4;
    Random rand = new Random();
    protected boolean left = false;
    protected boolean right = false;
    SnakeLogic snake;
    protected boolean exists = false;
    protected boolean isVac;
    //used for hitboxing and for painting
    protected int x1;
    protected int x2;
    protected int y1;
    protected int y2;

    public MovingMachine(SnakeLogic snake) {
        this.snake = snake;
    }

    protected void spawnLeft() {
        x = spawn1;
        left = true;
        exists = true;
        right = false;
    }

    protected void spawnRight() {
        x = spawn2;
        right = true;
        exists = true;
        left = false;
    }

    protected void despawn() {

        right = false;
        left = false;
        exists = false;
    }

    protected int calc(int n) {
        return (n * 25);
    }

    protected void update() {
            x1 = calc(x);
            x2 = calc(x)+100;
            y1 = calc(y);
            y2 = calc(y) + 70;
    }


}
