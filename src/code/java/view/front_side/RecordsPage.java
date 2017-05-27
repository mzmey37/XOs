package code.java.view.front_side;

//import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import static code.java.view.front_side.GameView.shorterName;

class RecordsPage {

    private static final Color C_APP = new Color(81, 202, 156);
    private static final Font FT_USUAL_LABEL = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    private static final int MAX_NAME_LEN = 14;

    private final GameView mainView;
    private Label l1Player;
    private Label l2Player;
    private Label lWinner;
    private Label lData;
    private Label[][] lRecords;
    private Button bBack;
    private Panel pMain;
    private Panel pRecords;
    private ScrollPane spRecords;
    private GridBagLayout ltMain;
    private GridLayout ltRecords;
    private GridBagConstraints gbc;

    RecordsPage(GameView gameView) {
        this.mainView = gameView;
    }

    Panel getRecordsPanel(String[][] records) {
        createRecordsPanel(records);
        adjustRecordsPanel();
        return pMain;
    }

    private String[][] reverse(String[][] records) {
        int last = records.length - 1;
        for (int i = 0; i < (last + 1) / 2; i++) {
            String[] tmp = records[i];
            records[i] = records[last - i];
            records[last - i] = tmp;
        }
        return records;
    }

    private void createRecordsPanel(String[][] records) {
        pMain = new Panel();
        ltMain = new GridBagLayout();
        gbc = new GridBagConstraints();

        pRecords = new Panel();
        spRecords = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        ltRecords = new GridLayout(records.length + 1, 4, 1, 1);

        bBack = new Button("Back");

        records = reverse(records);

        spRecords.add(pRecords);
        pRecords.setLayout(ltRecords);
        pMain.setLayout(ltMain);

        gbc.fill = gbc.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.95;
        pMain.add(spRecords, gbc);

        gbc.fill = gbc.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.05;
        pMain.add(bBack, gbc);

        spRecords.add(pRecords);


        pRecords.setLayout(ltRecords);

        l1Player = new Label("Player one");
        l2Player = new Label("Player two");
        lWinner = new Label("Winner");
        lData = new Label("Date of game");
        lRecords = new Label[records.length][4];

        pRecords.add(l1Player);
        pRecords.add(l2Player);
        pRecords.add(lWinner);
        pRecords.add(lData);

        if (records[0].length < 4) {
            pRecords.add(new Label("nobody"));
            pRecords.add(new Label("has"));
            pRecords.add(new Label("played"));
            pRecords.add(new Label("yet"));
            return;
        }

        for (int i = 0; i < records.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 2) {
                    if (records[i][j].equals("0")) {
                        lRecords[i][j] = new Label("Nobody");
                    }
                    else {
                        lRecords[i][j] = new Label(records[i][j].equals("1") ? shorterName(records[i][0]) :
                                shorterName(records[i][1]));
                    }
                }
                else {
                    lRecords[i][j] = new Label(shorterName(records[i][j]));
                }
                pRecords.add(lRecords[i][j]);
            }
        }
    }

    private void adjustRecordsPanel() {
        pRecords.setBackground(C_APP);

        l1Player.setAlignment(Label.CENTER);
        l1Player.setFont(FT_USUAL_LABEL);
        l1Player.setBackground(Color.white);

        l2Player.setAlignment(Label.CENTER);
        l2Player.setFont(FT_USUAL_LABEL);
        l2Player.setBackground(Color.white);

        lWinner.setAlignment(Label.CENTER);
        lWinner.setFont(FT_USUAL_LABEL);
        lWinner.setBackground(Color.white);

        lData.setAlignment(Label.CENTER);
        lData.setFont(FT_USUAL_LABEL);
        lData.setBackground(Color.white);
        if (lRecords[0][0] != null) {
            for (int i = 0; i < lRecords.length; i++) {
                for (int j = 0; j < 4; j++) {
                    lRecords[i][j].setAlignment(Label.CENTER);
                    lRecords[i][j].setFont(FT_USUAL_LABEL);
                    lRecords[i][j].setBackground(Color.white);
                }
            }
        }

        bBack.setBackground(C_APP);
        bBack.setFont(FT_USUAL_LABEL);
        bBack.setForeground(Color.white);
        bBack.addActionListener(e -> {
            mainView.onBackFromRecordsClick();
        });

        pRecords.setMinimumSize(new Dimension(10000, (lRecords.length + 1) * 140));
    }
}
