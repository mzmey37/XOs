package code.java.view.front_side;

import code.java.controller.main_loop.GameController;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameView {

    private static final String GAME_NAME = "X0s";
    private static final Dimension MIN_SIZE = new Dimension(400, 400);
    private static final int MAX_NAME_LEN = 14;

    private GameController controller;
    private Frame mainFrame;
    private Panel currentPanel;
    private MainPage mainPage;

    public GameView() {
        mainFrame = new Frame(GAME_NAME);
        mainPage = new MainPage(this);
        adjustMainFrame();
    }

    private void adjustMainFrame() {
        currentPanel = new Panel();
        mainFrame.add(this.currentPanel);
        mainFrame.setSize(800, 600);
        mainFrame.setMinimumSize(MIN_SIZE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                askBeforeClosing();
            }
        });
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void showInitPage() {
        MainPage.closeDialog();
        mainFrame.remove(currentPanel);
        LogInPage logInPage = new LogInPage(this);
        currentPanel = logInPage.getLogInPanel();
        mainFrame.add(currentPanel);
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //mainFrame.setBounds(screenSize.width / 2 - WIDTH / 2, screenSize.height / 2 - HEIGHT / 2, WIDTH, HEIGHT);
        mainFrame.setVisible(true);
    }

    public void showHostPage(String yourName) {
        mainFrame.remove(currentPanel);
        HostPage hostPage = new HostPage(this);
        currentPanel = hostPage.getParamsAsHostPanel(yourName);
        mainFrame.add(currentPanel);
        mainFrame.setVisible(true);
    }

    public void showClientPage(String yourName, String opponentName) {
        mainFrame.remove(currentPanel);
        ClientPage clientPage = new ClientPage(this);
        currentPanel = clientPage.getClientPanel(yourName, opponentName);
        mainFrame.add(currentPanel);
        mainFrame.setVisible(true);
    }

    public void showMainPage(String p1Name, String p2Name, int width, int height, int order, int[][] cells) {
        MainPage.waitConsent = false;
        HostPage.paramsSet = true;
        mainFrame.remove(currentPanel);
        currentPanel = mainPage.getMainPanel(p1Name, p2Name, width, height, order, cells);
        mainFrame.add(currentPanel);
        mainFrame.setVisible(true);
    }

    public void updateCells(int[][] cells, int order, int playerMarker) {
        mainPage.updateCells(cells, order, playerMarker);
    }

    public void showWinner(String message, int x, int y, int cell, int[][] winLine, boolean resultsSaved) {
        mainPage.showWinner(message, x, y, cell, winLine, resultsSaved);
    }

    public void showRecords(String[][] records) {
        mainFrame.remove(currentPanel);
        RecordsPage recordsPage = new RecordsPage(this);
        currentPanel = recordsPage.getRecordsPanel(records);
        mainFrame.add(currentPanel);
        mainFrame.setVisible(true);
    }

    public void showMistakeDialog(String mistakeMessage) {
        MainPage.gameIsGoing = false;
        MainPage.waitOffer = false;
        MainPage.waitConsent = false;
        MainPage.closeDialog();
        new MistakeDialog(mainFrame, mistakeMessage, this).setVisible(true);
    }

    Frame getMainFrame() {
        return mainFrame;
    }

    void onPushCell(int x, int y) {
        this.controller.onPushCell(x, y);
    }

    void onStartNewGameClick() {
        this.controller.onStartNewGameClick();
    }

    void onSaveResultsAgainClick() {
        controller.onSaveResultsAgainClick();
    }

    void onBackFromRecordsClick() {
        controller.onBackFromRecordsClick();
    }

    void onShowRecordsClick() {
        controller.onShowRecordsClick();
    }

    static String shorterName(String name) {
        if (name.length() <= MAX_NAME_LEN) {
            return name;
        }
        else{
            return name.substring(0, MAX_NAME_LEN - 1) + "..";
        }
    }

    private void askBeforeClosing() {
        new ClosingDialog(getMainFrame(), controller).setVisible(true);
    }

    void onHostGameClick(String name, int port) {
        controller.onHostGameClick(name, port);
    }

    void onConnectClick(String name, String ip, int port) {
        controller.onConnectClick(name, ip, port);
    }

    void onHostStartGameClick(String width, String height, String winLen) {
        controller.onStartHostGame(width, height, winLen);
    }

    void onDisconnectClientClick() {
        controller.onDisconnectClientClick();
    }

    String askOtherName() {
        return controller.askOtherName();
    }

    void checkHostStarted() {
        controller.checkHostStarted();
    }

    void checkMainPageUpdates() {
        controller.checkMainPageUpdate();
    }

    public void showWaitAcceptingPlayAgain() {
        mainPage.showConsentWaiting();
    }

    void waitAcception() {
        controller.onConsentWaitingCheck();
    }

    void checkInWinnerDialog() {
        controller.checkInWinnerDialog();
    }

    public void showSuggestionPlayAgain() {
        MainPage.waitOffer = false;
        mainPage.showOfferPlayAgain();
    }

    void onConsentClick() {
        controller.onConsentClick();
    }

    void onDislineSuggestion() {
        controller.onRefuseOffer();
    }

    void onSuggestPlayAgainClick() {
        controller.onSuggestPlayAgainClick();
    }

    void checkOpponentWaitsConsent() {
        controller.checkOpponentWaitsConsent();
    }
}
