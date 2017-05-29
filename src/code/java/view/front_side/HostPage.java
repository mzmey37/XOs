package code.java.view.front_side;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import static code.java.view.front_side.LogInPage.*;

public class HostPage {

    public static final String WRONG_PARAMS_MESSAGE = "\"Width and weight must be >= 3 and\nlength of line to win\n" +
            "must be less than one of them";

    private final GameView mainView;
    private Panel pHost;
    private Label lYourName;
    private Label lYourNameVal;
    private Label lOpponentName;
    private Label lOpponentNameVal;
    private Label lWidth;
    private Label lHeight;
    private Label lWinLen;
    private Label lPageName;
    private Label lIp;
    private TextField tfWidth;
    private TextField tfHeight;
    private TextField tfWinLen;
    private Button bHostGame;
    private Button bBack;
    private GridBagConstraints gbc;
    private GridBagLayout lt;
    static boolean paramsSet = false;

    HostPage(GameView mainView) {
        this.mainView = mainView;
        pHost = new Panel();
        lt = new GridBagLayout();
        gbc = new GridBagConstraints();
    }

    Panel getParamsAsHostPanel(String yourName) {
        initParamsPanelAsHost(yourName);
        adjustParamsPanelAsHost();
        return pHost;
    }

    private void initParamsPanelAsHost(String yourName) {
        bHostGame = new Button("Start game");
        bBack = new Button("Back");
        lWidth = new Label("Chose the width of field in cell");
        lHeight = new Label("Chose the height of field in cell");
        lWinLen = new Label("Chose the length of line to win in cell");
        lYourName = new Label("Your name:");
        lYourNameVal = new Label(yourName);
        lOpponentName = new Label("Your opponent:");
        lOpponentNameVal = new Label("nobody has connected");
        lPageName = new Label("Choose parameters, wait for the second player and start playing!");
        lIp = new Label("your ip: " + getCurrentIP());
        tfWidth = new TextField(10);
        tfHeight = new TextField(10);
        tfWinLen = new TextField(10);

        pHost.setLayout(lt);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        pHost.add(lPageName, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        pHost.add(lYourName, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        pHost.add(lYourNameVal, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        pHost.add(lOpponentName, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.1;
        gbc.gridx = 1;
        gbc.gridy = 2;
        pHost.add(lOpponentNameVal, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        pHost.add(lWidth, gbc);

        gbc.gridy++;
        pHost.add(lHeight, gbc);

        gbc.gridy++;
        pHost.add(lWinLen, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.1;
        gbc.gridx = 1;
        gbc.gridy = 3;
        pHost.add(tfWidth, gbc);

        gbc.gridy++;
        pHost.add(tfHeight, gbc);

        gbc.gridy++;
        pHost.add(tfWinLen, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        pHost.add(lIp, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 7;
        pHost.add(bHostGame, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 8;
        pHost.add(bBack, gbc);
    }

    private void adjustParamsPanelAsHost() {

        lPageName.setAlignment(Label.CENTER);
        lPageName.setFont(FT_USUAL_FIELD);
        lPageName.setBackground(C_APP);
        lPageName.setForeground(Color.white);

        lOpponentName.setAlignment(Label.CENTER);
        lOpponentName.setFont(FT_USUAL_LABEL);

        lOpponentNameVal.setAlignment(Label.CENTER);
        lOpponentNameVal.setFont(FT_USUAL_LABEL);

        lYourName.setAlignment(Label.CENTER);
        lYourName.setFont(FT_USUAL_LABEL);

        lYourNameVal.setAlignment(Label.CENTER);
        lYourNameVal.setFont(FT_USUAL_LABEL);

        lWidth.setAlignment(Label.CENTER);
        lWidth.setFont(FT_USUAL_LABEL);

        lHeight.setAlignment(Label.CENTER);
        lHeight.setFont(FT_USUAL_LABEL);

        lWinLen.setAlignment(Label.CENTER);
        lWinLen.setFont(FT_USUAL_LABEL);

        lIp.setAlignment(Label.CENTER);
        lIp.setFont(FT_USUAL_LABEL);
        lIp.setAlignment(Label.CENTER);

        bHostGame.setBackground(C_APP);
        bHostGame.setFont(FT_USUAL_LABEL);
        bHostGame.setForeground(Color.white);
        bHostGame.addActionListener(e -> {
            mainView.onHostStartGameClick(tfWidth.getText(), tfHeight.getText(), tfWinLen.getText());
        });
        bHostGame.setEnabled(false);

        bBack.setBackground(C_APP);
        bBack.setFont(FT_USUAL_LABEL);
        bBack.setForeground(Color.white);
        bBack.addActionListener(e -> {
            mainView.onStartNewGameClick();
        });

        tfWidth.requestFocus();
        tfWidth.addActionListener(e -> {
            tfHeight.requestFocus();
        });
        tfHeight.addActionListener(e -> {
            tfWinLen.requestFocus();
        });
        tfWinLen.addActionListener(e -> {
            mainView.onHostStartGameClick(tfWidth.getText(), tfHeight.getText(), tfWinLen.getText());
        });

        new Thread(() -> {
            boolean hadClient = false;
            paramsSet = false;
            while (!paramsSet) {
                try {
                    Thread.sleep(REFRESH_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mainView.askOtherName() != null) {
                    lOpponentNameVal.setText(mainView.askOtherName());
                    hadClient = true;
                    bHostGame.setEnabled(true);
                } else if (mainView.askOtherName() == null && hadClient) {
                    lOpponentNameVal.setText("Opponent disconnected");
                    bHostGame.setEnabled(false);
                }
            }
        }).start();
    }

    private String getCurrentIP() {
        String result = null;
        try {
            BufferedReader reader = null;
            try {
                URL url = new URL("https://myip.by/");
                InputStream inputStream = null;
                inputStream = url.openStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder allText = new StringBuilder();
                char[] buff = new char[1024];

                int count = 0;
                while ((count = reader.read(buff)) != -1) {
                    allText.append(buff, 0, count);
                }

                Integer indStart = allText.indexOf("\">whois ");
                Integer indEnd = allText.indexOf("</a>", indStart);

                String ipAddress = new String(allText.substring(indStart + 8, indEnd));
                if (ipAddress.split("\\.").length == 4) { 
                    result = ipAddress;
                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
