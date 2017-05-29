package code.java.view.front_side;

import java.awt.*;

public class LogInPage {

    static final int REFRESH_DELAY = 150;
    private static final Color C_MAIN_BACK = Color.white;
    private static final Color C_MAIN_FORE = Color.black;
    static final Font FT_USUAL_LABEL = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    static final Font FT_USUAL_FIELD = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
    public static final String MISTAKE_MESSAGE_FOR_HOST = "Wrong name format or" +
            "\nserver on this ip is already started." +
            "\nEnter name without '~' simbol.";
    public static final String MISTAKE_MESSAGE_FOR_CLIENT = "Wrong name format or IP. Enter name without '~' simbol.";
    static final Color C_APP = new Color(81, 202, 156);

    private Panel pLogIn = new Panel();
    private final GameView mainView;
    private Label lHello;
    private Label lYourName;
    private Label lIp;
    private Label lTopEmpty;
    private Label lBottomEmpty;
    private TextField tfYourName;
    private TextField tfIp;
    private TextField tfPort;
    private Button bConnect;
    private Button bShowRecords;
    private Button bHostGame;
    private GridBagLayout lt;
    private GridBagConstraints gbc;

    LogInPage(GameView gameView) {
        mainView = gameView;
    }

    Panel getLogInPanel() {
        createLogInPanel();
        adjustLogInPanel();
        return pLogIn;
    }

    private void createLogInPanel() {

        lTopEmpty = new Label();
        lBottomEmpty = new Label();
        lHello = new Label("Hello from X0s");
        lYourName = new Label("Enter your name");
        lIp = new Label("Enter your opponent's ip");
        lBottomEmpty = new Label();
        tfYourName = new TextField(10);
        tfIp = new TextField(30);
        tfPort = new TextField(30);
        bConnect = new Button("Connect to opponent");
        bShowRecords = new Button("Show records");
        bHostGame = new Button("Host game");
        gbc = new GridBagConstraints();
        lt = new GridBagLayout();

        pLogIn.setLayout(lt);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.ipady = 50;
        gbc.gridy = 0;
        gbc.weighty = 0.2;
        pLogIn.add(lHello, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 0;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.15;
        pLogIn.add(lTopEmpty, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pLogIn.add(lYourName, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        pLogIn.add(tfYourName, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        pLogIn.add(lIp, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 5;
        pLogIn.add(tfIp, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.weighty = 0.1;
        gbc.gridx = 1;
        gbc.gridy = 5;
        pLogIn.add(tfPort, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        pLogIn.add(bConnect, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 2;
        gbc.weighty = 0.15;
        gbc.gridx = 0;
        gbc.gridy = 7;
        pLogIn.add(bHostGame, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 2;
        gbc.weighty = 0.15;
        gbc.gridx = 0;
        gbc.gridy = 8;
        pLogIn.add(bShowRecords, gbc);
    }

    private void adjustLogInPanel() {
        pLogIn.setBackground(C_MAIN_BACK);

        lHello.setAlignment(Label.CENTER);
        lHello.setForeground(Color.white);
        lHello.setFont(FT_USUAL_FIELD);
        lHello.setBackground(C_APP);

        lYourName.setBackground(C_MAIN_BACK);
        lYourName.setFont(FT_USUAL_LABEL);
        lYourName.setForeground(C_MAIN_FORE);
        lYourName.setAlignment(Label.CENTER);

        tfYourName.setFont(FT_USUAL_FIELD);
        tfYourName.addActionListener(e -> {
            tfIp.requestFocus();
        });

        lIp.setBackground(C_MAIN_BACK);
        lIp.setAlignment(Label.CENTER);
        lIp.setFont(FT_USUAL_LABEL);
        lIp.setForeground(C_MAIN_FORE);

        tfIp.setFont(FT_USUAL_FIELD);
        tfIp.addActionListener(e -> {
            mainView.onConnectClick(tfYourName.getText(), tfIp.getText(), Integer.valueOf(tfPort.getText()));
        });

        tfPort.setFont(FT_USUAL_FIELD);

        lTopEmpty.setBackground(C_MAIN_BACK);

        lBottomEmpty.setBackground(C_MAIN_BACK);

        bConnect.setBackground(C_APP);
        bConnect.setFont(FT_USUAL_LABEL);
        bConnect.setForeground(Color.white);
        bConnect.addActionListener(e -> {
            mainView.onConnectClick(tfYourName.getText(), tfIp.getText(), Integer.valueOf(tfPort.getText()));
        });

        bHostGame.setBackground(C_APP);
        bHostGame.setFont(FT_USUAL_LABEL);
        bHostGame.setForeground(Color.white);
        bHostGame.addActionListener(e -> {
            mainView.onHostGameClick(tfYourName.getText(), Integer.valueOf(tfPort.getText()));
        });

        bShowRecords.setBackground(C_APP);
        bShowRecords.setFont(FT_USUAL_LABEL);
        bShowRecords.setForeground(Color.white);
        bShowRecords.addActionListener(e -> {
            mainView.onShowRecordsClick();
        });

    }

}
