package code.java.view.front_side;


import code.java.model.game.GameModel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static code.java.view.front_side.GameView.shorterName;
import static code.java.view.front_side.LogInPage.REFRESH_DELAY;

public class MainPage {

    public static final String ENEMY_LEFT = "Your opponent has left the game.";
    private static final int VERTICAL_GAP = 0;
    private static final int HORIZONTAL_GAP = 0;
    private static final String YOUR_TURN = "Your turn";
    private static final String ENEMY_TURN = "Opponent's turn";
    static boolean waitOffer = false;
    static boolean waitConsent = false;
    static boolean gameIsGoing = true;

    private static WinnerShowerDialog winnerDialog;
    private Label lTop;
    private final GameView mainView;
    private Panel mainPanel;
    private Label lYouBot;
    private Label lYouTop;
    private Label lEnemyTop;
    private Label lEnemyBot;
    private GridLayout ltGrid;
    private Button[][] cells;
    private GridBagLayout lt;
    private Label lTurn;
    private int width;
    private int height;
    private int currentPlayerMarker;

    MainPage(GameView gameView) {
        this.mainView = gameView;
    }

    Panel getMainPanel(String p1Name, String p2Name, int width, int height, int order, int[][] cells) {
        gameIsGoing = true;
        waitConsent = false;
        waitOffer = false;
        HostPage.paramsSet = true;
        this.width = width;
        this.height = height;
        createMainPanel(p1Name, p2Name, order, cells);
        adjustMainPanel();
        return mainPanel;
    }

    private void createMainPanel(String p1Name, String p2Name, int order, int[][] cells) {
        lTop = new Label("Enjoy X0s");
        mainPanel = new Panel();
        Panel pInside = new Panel();
        ltGrid = new GridLayout(height, width);
        mainPanel.setLayout(lt);
        lYouTop = new Label("You");
        lYouBot = new Label(shorterName(p1Name));
        lEnemyTop = new Label("Opponent:");
        lEnemyBot = new Label(shorterName(p2Name));
        lt = new GridBagLayout();
        GridBagConstraints gbcMain = new GridBagConstraints();
        lTurn = new Label(order == GameModel.PLAYER_ONE ? YOUR_TURN : ENEMY_TURN);

        mainPanel.setLayout(lt);

        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.gridheight = 1;
        gbcMain.gridwidth = 3;
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.weightx = 1;
        gbcMain.weighty = 0.1;
        mainPanel.add(lTop, gbcMain);

        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.gridheight = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        gbcMain.weightx = 0.15;
        gbcMain.weighty = 0.2;
        mainPanel.add(lYouTop, gbcMain);

        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.gridheight = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridx = 0;
        gbcMain.gridy = 2;
        gbcMain.weightx = 0.15;
        gbcMain.weighty = 0.6;
        mainPanel.add(lYouBot, gbcMain);

        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.gridheight = 2;
        gbcMain.gridwidth = 1;
        gbcMain.gridx = 1;
        gbcMain.gridy = 1;
        gbcMain.weightx = 0.7;
        gbcMain.weighty = 0.8;
        mainPanel.add(pInside, gbcMain);

        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.gridheight = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridx = 2;
        gbcMain.gridy = 1;
        gbcMain.weightx = 0.15;
        gbcMain.weighty = 0.2;
        mainPanel.add(lEnemyTop, gbcMain);

        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.gridheight = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridx = 2;
        gbcMain.gridy = 2;
        gbcMain.weightx = 0.15;
        gbcMain.weighty = 0.6;
        mainPanel.add(lEnemyBot, gbcMain);

        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.gridwidth = 3;
        gbcMain.gridheight = 1;
        gbcMain.gridx = 0;
        gbcMain.gridy = 3;
        gbcMain.weightx = 1;
        gbcMain.weighty = 0.1;
        mainPanel.add(lTurn, gbcMain);

        pInside.setLayout(ltGrid);

        this.cells = new Button[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.cells[i][j] = new Button(getLabelFromCell(cells[i][j]));
                this.cells[i][j].setBackground(Color.white);
                this.cells[i][j].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
                int finalI = i;
                int finalJ = j;
                this.cells[i][j].addActionListener(e -> {
                    mainView.onPushCell(finalJ, finalI);
                });
                if (order == GameModel.PLAYER_TWO) {
                    this.cells[i][j].setEnabled(false);
                }
                pInside.add(this.cells[i][j]);
            }
        }
    }

    private void adjustMainPanel() {
        ltGrid.setVgap(VERTICAL_GAP);
        ltGrid.setHgap(HORIZONTAL_GAP);

        lTop.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        lTop.setAlignment(Label.CENTER);
        lTop.setBackground(LogInPage.C_APP);

        lTurn.setBackground(Color.white);
        lTurn.setAlignment(Label.CENTER);

        lYouTop.setBackground(Color.white);
        lYouTop.setAlignment(Label.CENTER);
        lYouBot.setBackground(Color.white);
        lYouBot.setAlignment(Label.CENTER);

        lEnemyTop.setBackground(Color.white);
        lEnemyTop.setAlignment(Label.CENTER);
        lEnemyBot.setBackground(Color.white);
        lEnemyBot.setAlignment(Label.CENTER);

        new Thread(() -> {
            while (gameIsGoing) {
                mainView.checkMainPageUpdates();
                try {
                    Thread.sleep(REFRESH_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //System.out.println("stop game going thread");
        }).start();
    }

    void showWinner(String message, int x, int y, int cell, int[][] winDirection, boolean resultsSaved) {
        gameIsGoing = false;
        for (Button[] bLine: cells) {
            for (Button b: bLine) {
                b.setEnabled(false);
            }
        }
        waitOffer = true;
        new Thread(() -> {
            while (waitOffer) {
                mainView.checkInWinnerDialog();
                try {
                    Thread.sleep(REFRESH_DELAY * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        if (!message.equals("Nobody wins") && x >= 0) {
            endLine(x, y, cell, winDirection);
        }
        else {
            cells[y][x].setLabel(getLabelFromCell(cell));
        }
        if (!resultsSaved) {
            new ResultsSavingDialog(mainView.getMainFrame()).setVisible(true);
        }
        if (x >= 0) {
            winnerDialog = new WinnerShowerDialog(mainView.getMainFrame(), message);
            winnerDialog.setVisible(true);
        }
    }

    private void endLine(int x, int y, int cell, int[][] winLine) {
        cells[y][x].setLabel(getLabelFromCell(cell));
        int[] dir = new int[2];
        for (int i = 0; i < 2; i++) {
            if (winLine[1][i] - winLine[0][i] > 0) {
                dir[i] = 1;
            } else if (winLine[1][i] - winLine[0][i] == 0) {
                dir[i] = 0;
            } else {
                dir[i] = -1;
            }
        }
        int i = 0;
        while(winLine[0][0] + i * dir[0] != winLine[1][0] || winLine[0][1] + i * dir[1] != winLine[1][1]) {
            markCell(winLine[0][0] + i * dir[0], winLine[0][1] + i * dir[1]);
            i++;
        }
        markCell(winLine[1][0], winLine[1][1]);
    }

    private void markCell(int x, int y) {
        cells[y][x].setBackground(Color.black);
        cells[y][x].setForeground(Color.white);
    }

    private String getLabelFromCell(int cell) {
        if (cell != GameModel.FREE_CELL) {
            if (cell == currentPlayerMarker) {
                return currentPlayerMarker == GameModel.X_CELL ? "X" : "0";
            }
            else {
                return currentPlayerMarker == GameModel.X_CELL ? "0" : "X";
            }
        }
        return "";
    }

    void updateCells(int[][] cells, int order, int playerMarker) {
        currentPlayerMarker = playerMarker;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!this.cells[i][j].getLabel().equals(getLabelFromCell(cells[i][j]))) {
                    this.cells[i][j].setLabel(getLabelFromCell(cells[i][j]));
                    System.out.println("Updated " + i + " " + j);
                }
                if (cells[i][j] > 0) {
                    this.cells[i][j].setEnabled(false);
                }
            }
        }
        updateTurn(order);
    }

    private void updateTurn(int order) {
        if (order == GameModel.PLAYER_TWO) {
            lTurn.setText(ENEMY_TURN);
            for (Button[] bs: cells) {
                for (Button b: bs) {
                    b.setEnabled(false);
                }
            }
        }
        else {
            lTurn.setText(YOUR_TURN);
            for (Button[] bs: cells) {
                for (Button b: bs) {
                    b.setEnabled(true);
                }
            }
        }
    }

    void showConsentWaiting() {
        waitOffer = false;
        winnerDialog.showConsentWaiting();
        waitConsent = true;
        new Thread(() -> {
            while (waitConsent) {
                mainView.waitAcception();
                try {
                    Thread.sleep(REFRESH_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            winnerDialog.dispose();
        }).start();
    }

    void showOfferPlayAgain() {
        waitOffer = false;
        winnerDialog.showOfferPlayAgain();
        waitConsent = true;
        new Thread(() -> {
            while (waitConsent) {
                mainView.checkOpponentWaitsConsent();
                try {
                    Thread.sleep(REFRESH_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            winnerDialog.dispose();
        }).start();
    }

    static void closeDialog() {
        if (winnerDialog != null) {
            winnerDialog.dispose();
        }
    }

    private class WinnerShowerDialog extends Dialog {

        private Label lMessage;
        private Button bNewGame = new Button("Leave and start new game");
        private Button bPlayAgain = new Button("Offer one more game");
        private Button bConsentToOffer = new Button("Consent");
        private Button bDislineSuggestoin = new Button("Decline ");
        private Label lWaitingForConsent1 = new Label("Waiting for opponent to");
        private Label lWaitingForConsent2 = new Label("consent to the new game");


        WinnerShowerDialog(Frame parent, String message){
            super(parent, true);

            lMessage = new Label(message);
            lMessage.setSize(getWidth(), getHeight() / 2);
            lMessage.setAlignment(Label.CENTER);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(screenSize.width / 2 - 100, screenSize.height / 2 - 100, 200,200);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent windowEvent){
                    waitConsent = false;
                    waitOffer = false;
                    gameIsGoing = false;
                    mainView.onStartNewGameClick();
                    dispose();
                }
            });

            bConsentToOffer.addActionListener(e -> {
                mainView.onConsentClick();
                dispose();
            });

            bDislineSuggestoin.addActionListener(e -> {
                mainView.onDislineSuggestion();
                dispose();
            });

            bNewGame.addActionListener(e -> {
                waitConsent = false;
                waitOffer = false;
                gameIsGoing = false;
                mainView.onStartNewGameClick();;
                dispose();
            });

            bPlayAgain.addActionListener(e -> {
                mainView.onSuggestPlayAgainClick();
            });
            setLayout(new BorderLayout());
            add(lMessage, BorderLayout.CENTER);
            add(bPlayAgain, BorderLayout.PAGE_START);
            add(bNewGame, BorderLayout.PAGE_END);

        }

        void showOfferPlayAgain() {
            remove(bNewGame);
            remove(bPlayAgain);
            setLayout(new BorderLayout());
            add(bConsentToOffer, BorderLayout.PAGE_START);
            add(bDislineSuggestoin, BorderLayout.PAGE_END);
            lMessage.setText("Opponent offers to play again");
            setVisible(true);
        }

        void showConsentWaiting() {
            remove(bPlayAgain);
            remove(bNewGame);
            remove(lMessage);
            setLayout(new GridLayout(2, 1));
            lWaitingForConsent1.setAlignment(Label.CENTER);
            lWaitingForConsent2.setAlignment(Label.CENTER);
            add(lWaitingForConsent1);
            add(lWaitingForConsent2);
            setVisible(true);
        }
    }

    private class ResultsSavingDialog extends Dialog {

        private Button bTryAgain = new Button("Try again");
        private Button bContinue = new Button("Continue");

        ResultsSavingDialog(Frame owner) {
            super(owner, "Can't save results", true);
            setLayout(new GridLayout(5, 3));
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent windowEvent){
                    mainView.onStartNewGameClick();
                    dispose();
                }
            });
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(screenSize.width / 2 - 100, screenSize.height / 2 - 100, 200,200);
            bTryAgain.addActionListener(e -> {
                dispose();
                mainView.onSaveResultsAgainClick();
            });
            bContinue.addActionListener(e -> {
                dispose();
            });
            add(bTryAgain);
            add(bContinue);


        }
    }
}
