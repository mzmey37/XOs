package code.java.view.front_side;

import code.java.controller.main_loop.GameController;
import sun.applet.Main;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static code.java.view.front_side.LogInPage.FT_USUAL_LABEL;

class MistakeDialog extends Dialog {

    MistakeDialog(Frame parent, String message, GameView view) {
        super(parent, true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width / 2 - 100, screenSize.height / 2 - 100, 500, 400);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                if (message.equals(MainPage.ENEMY_LEFT)) {
                    view.showInitPage();
                }
                dispose();
            }
        });
        TextArea ta = new TextArea(message);
        ta.setBackground(Color.white);
        ta.setFont(FT_USUAL_LABEL);
        gbc.fill = gbc.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.5;
        gbc.weightx = 1;
        add(ta, gbc);
        Button b = new Button("OK");
        b.setBackground(Color.white);
        b.addActionListener(e -> {
            if (message.equals(MainPage.ENEMY_LEFT) || message.equals(ClientPage.HOST_LEFT)) {
                view.showInitPage();
            }
            dispose();
        });
        gbc.fill = gbc.NONE;
        gbc.gridy = 1;
        add(b, gbc);
    }
}
