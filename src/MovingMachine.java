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

    protected int calc(int n){
        return (n*25);
    }
    protected int calcY(int n){
        return (n*25)+10;
    }


}
