package utiles;

public class Pause {

    public void pause(int timeToPause) {
        try {
            Thread.sleep(timeToPause);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}