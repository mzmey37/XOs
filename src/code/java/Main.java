package code.java;

import code.java.controller.main_loop.GameController;
import code.java.model.game.GameModel;
import code.java.view.front_side.GameView;

/**
 * Created by maxim on 4/25/17.
 */
public class Main {

    public static void main(String[] args) {
        GameModel model = new GameModel();
        GameView view = new GameView();
        GameController c = new GameController(model, view);
        c.startApplication();
    }
}
