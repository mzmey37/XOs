package code.java.controller.main_loop;

import code.java.model.game.GameModel;
import code.java.view.front_side.*;

public class GameController {

    private GameView view;
    private GameModel model;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        view.setController(this);
        model.getRecords();
    }

    public void startApplication() {
        view.showInitPage();
    }

    public void onHostGameClick(String name1) {
        model.hostGame(name1);
        if (model.getState() == GameModel.CHOOSING_PARAMS) {
            view.showHostPage(name1);
        }
        else {
            view.showMistakeDialog(LogInPage.MISTAKE_MESSAGE_FOR_HOST);
        }
    }

    public void onConnectClick(String name, String ip) {
        model.connect(name, ip);
        if (model.isConnected()) {
            view.showClientPage(name, model.getP2Name());
        }
        else {
            view.showMistakeDialog(LogInPage.MISTAKE_MESSAGE_FOR_CLIENT);
        }
    }

    public String askOtherName() {
        return model.getP2Name();
    }

    public void onStartHostGame(String width, String height, String winLen) {
        model.initGame(width, height, winLen);
        if (model.getState() == GameModel.PLAYING) {
            view.showMainPage(model.getP1Name(), model.getP2Name(), model.getWidth(), model.getHeight(),
                    model.getOrder(), model.getCells());
        }
        else {
            view.showMistakeDialog(HostPage.WRONG_PARAMS_MESSAGE);
        }
    }

    public void checkHostStarted() {
        if (model.getState() == GameModel.PLAYING) {
            ClientPage.waitHost = false;
            view.showMainPage(model.getP1Name(), model.getP2Name(), model.getWidth(), model.getHeight(),
                    model.getOrder(), model.getCells());
        }
        if (model.getState() == GameModel.INITIAL) {
            ClientPage.waitHost = false;
            view.showMistakeDialog(ClientPage.HOST_LEFT);
        }
    }

    public void onPushCell(int x, int y) {
        model.pushCell(x, y, true);
        //если игра не закончилась, то просто обновляем клетки
        if (model.getState() == GameModel.PLAYING) {
            view.updateCells(model.getCells(), model.getOrder(), model.getPlayerMarker());
        }
        else {
            showWinner(x, y);
        }
    }

    private void showWinner(int x, int y) {
        //проверка, что результаты сохранились
        //вызов окна с победителем
        if (model.getWinner() == GameModel.NOBODY_WINS) {
            view.showWinner("Nobody wins", x, y, model.getCells()[y][x], null, model.resultsSaved());
        }
        else {
            view.showWinner(model.getWinnerName() + " wins", x, y, model.getCells()[y][x],
                    model.getWinLine(), model.resultsSaved());
        }
    }

    public void onSuggestPlayAgainClick() {
        model.suggestPlayAgain();
        view.showWaitAcceptingPlayAgain();
    }

    public void onConsentWaitingCheck() {
        if (model.getState() == GameModel.PLAYING) {
            view.showMainPage(model.getP1Name(), model.getP2Name(), model.getWidth(),
                    model.getHeight(), model.getWinLen(), model.getCells());
        }
    }

    public void onStartNewGameClick() {
        model.dropGame();
        model.disconnect();
        view.showInitPage();
    }

    public void onSaveResultsAgainClick() {
        model.saveResults();
        showWinner(-1, -1);
    }

    public void onShowRecordsClick() {
        view.showRecords(model.getRecords());
    }

    public void onBackFromRecordsClick() {
        view.showInitPage();
    }

    public void onExitClick() {
        if (model.isConnected()) {
            model.disconnect();
        }

    }

    public void onDisconnectClientClick() {
        model.disconnect();
        view.showInitPage();
    }

    public void checkMainPageUpdate() {
        //System.out.println("in update " + (model.getState() == GameModel.PLAYING));
        if (model.getState() == GameModel.PLAYING) {
            view.updateCells(model.getCells(), model.getOrder(), model.getPlayerMarker());
        }
        else if (model.enemyConnected()){
            showWinner(model.getWinJ(), model.getWinI());
        }
        else {
            view.showMistakeDialog(MainPage.ENEMY_LEFT);
        }
    }

    public void checkInWinnerDialog() {
        if (!model.enemyConnected()) {
            view.showMistakeDialog(MainPage.ENEMY_LEFT);
        }
        else if (model.secondAsksPlayAgain()) {
            view.showSuggestionPlayAgain();
        }
    }

    public void onConsentClick() {
        model.consentToPlay();
        view.showMainPage(model.getP1Name(), model.getP2Name(), model.getWidth(), model.getHeight(),
                model.getWinLen(), model.getCells());
    }

    public void onRefuseOffer() {
        model.rejectPlayAgain();
        view.showInitPage();
    }

    public void checkOpponentWaitsConsent() {
        if (model.getState() == GameModel.INITIAL) {
            view.showMistakeDialog(MainPage.ENEMY_LEFT);
        }
        else if (model.getState() == GameModel.PLAYING) {
            view.showMainPage(model.getP1Name(), model.getP2Name(), model.getWidth(), model.getHeight(),
                    model.getOrder(), model.getCells());
        }
    }
}
