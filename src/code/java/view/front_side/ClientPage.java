package code.java.view.front_side;

import java.awt.*;

import static code.java.view.front_side.LogInPage.*;

public class ClientPage {

    public static final String HOST_LEFT = "Host has left";
    public static boolean waitHost = false;

    private final GameView mainView;
    private Panel pClient = new Panel();
    private Label lYourName;
    private Label lYourNameVal;
    private Label lOpponentName;
    private Label lOpponentNameVal;
    private Label lOopponentState;
    private Label lWaitingForOpponent;
    private Button bDisconnect;
    private GridBagConstraints gbc;
    private GridBagLayout lt;

    ClientPage(GameView mainView) {
        System.out.println("client");
        this.mainView = mainView;
        lt = new GridBagLayout();
        gbc = new GridBagConstraints();
    }

    Panel getClientPanel(String p1Name, String p2Name) {
        initParamsAsClientPanel(p1Name, p2Name);
        adjustParamsAsClientPanel();
        return pClient;
    }

    private void initParamsAsClientPanel(String p1Name, String p2Name) {
        lYourName = new Label("Your name:");
        lOpponentName = new Label("Your opponent:");
        lYourNameVal = new Label(p1Name);
        lOpponentNameVal = new Label(p2Name);
        lOopponentState = new Label("Your opponent is choosing parameters for the game, wait a bit");
        lWaitingForOpponent = new Label("Waiting for opponent");
        bDisconnect = new Button("Disconnect");

        pClient.setLayout(lt);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.25;
        pClient.add(lWaitingForOpponent, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.15;
        pClient.add(lYourName, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 0.15;
        pClient.add(lYourNameVal, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.15;
        pClient.add(lOpponentName, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weighty = 0.15;
        pClient.add(lOpponentNameVal, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.15;
        pClient.add(lOopponentState, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 0.15;
        pClient.add(bDisconnect, gbc);
    }

    private void adjustParamsAsClientPanel() {

        lWaitingForOpponent.setBackground(C_APP);
        lWaitingForOpponent.setForeground(Color.white);
        lWaitingForOpponent.setAlignment(Label.CENTER);
        lWaitingForOpponent.setFont(FT_USUAL_FIELD);

        lOpponentNameVal.setAlignment(Label.CENTER);
        lOpponentNameVal.setFont(FT_USUAL_LABEL);

        lOpponentName.setAlignment(Label.CENTER);
        lOpponentName.setFont(FT_USUAL_LABEL);

        lYourName.setAlignment(Label.CENTER);
        lYourName.setFont(FT_USUAL_LABEL);

        lYourNameVal.setAlignment(Label.CENTER);
        lYourNameVal.setFont(FT_USUAL_LABEL);

        lOopponentState.setBackground(C_APP);
        lOopponentState.setForeground(Color.white);
        lOopponentState.setAlignment(Label.CENTER);
        lOopponentState.setFont(FT_USUAL_FIELD);

        bDisconnect.setBackground(C_APP);
        bDisconnect.setFont(FT_USUAL_LABEL);
        bDisconnect.setForeground(Color.white);
        bDisconnect.addActionListener(e -> {
            waitHost = false;
            mainView.onDisconnectClientClick();
        });

        new Thread(() -> {
            boolean paramsSet = false;
            while(!paramsSet) {
                try {
                    Thread.sleep(REFRESH_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mainView.askOtherName() != null) {
                    lOpponentNameVal.setText(mainView.askOtherName());
                    paramsSet = true;
                }
            }
        }).start();

        new Thread(() -> {
            waitHost = true;
            while(waitHost) {
                mainView.checkHostStarted();
                try {
                    Thread.sleep(REFRESH_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
