import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public interface Observer {
    public void update(KeyEvent keyEvent);
}

interface Observable {
    public void NotifyObservers(KeyEvent keyEvent);
}

class World implements KeyListener, Observable {
    private ArrayList<Observer> obsList;

    public World() {
        obsList = new ArrayList<>();
    }

    public void keyPressed(KeyEvent e) {
        NotifyObservers(e);
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void NotifyObservers(KeyEvent keyEvent) {
        for (Observer obs : obsList) {

            obs.update(keyEvent);
        }
    }

    public void addObserver(Observer obs) {
        if (obs != null) {
            obsList.add(obs);
        }
    }

    public void removeObserver(Observer obs){
        if(obs !=null){
            obsList.remove(obs);
        }
    }

}
