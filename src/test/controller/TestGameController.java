package test.controller;

import code.java.controller.main_loop.GameController;
import code.java.model.game.GameModel;
import code.java.view.front_side.GameView;
import org.junit.Test;

public class TestGameController {

    boolean loadSecond = true;

    @Test
    public void testIt() throws InterruptedException {
        GameModel model = new GameModel();
        GameView view = new GameView();
        GameController c = new GameController(model, view);
        c.startApplication();/*
        if (loadSecond) {
            new Thread(() -> {
                GameModel model2 = new GameModel();
                GameView view2 = new GameView();
                GameController c2 = new GameController(model2, view2);
                c2.startApplication();
            }).start();
        }*/
        Thread.sleep(1000000);
    }
}
