package main;

public class GameManager { // collision/clipping system a bit wonky, you can clip through concave corners

    private UI ui = new UI(this);

    public static void main(String[] args) {
        new GameManager();
    }

    public GameManager() {

    }
}
