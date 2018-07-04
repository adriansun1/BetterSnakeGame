public class Car extends MovingMachine {
    protected boolean isVac = false;
    protected int uproad = 10;
    protected int downroad = y;

    public Car(SnakeLogic snake) {
        super(snake);
    }

    protected void spawnRight() {
        super.spawnRight();
        y = uproad;
    }

    protected void spawnLeft() {
        super.spawnLeft();
        y = downroad;
    }
}
