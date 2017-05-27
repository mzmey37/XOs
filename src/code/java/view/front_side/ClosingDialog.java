package code.java.view.front_side;

import code.java.controller.main_loop.GameController;
import code.java.model.game.GameModel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClosingDialog extends Dialog {

    private Button bYes = new Button("Yes, close game");
    private Button bNo = new Button("No, don't close");

    public ClosingDialog(Frame owner, GameController controller) {
        super(owner, "Are you sure?", true);
        setLayout(new GridLayout(5, 3));
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                dispose();
            }
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width / 2 - 100, screenSize.height / 2 - 100, 200,200);
        bYes.addActionListener(e -> {
            controller.onExitClick();
            System.exit(0);
        });
        bNo.addActionListener(e -> {
            dispose();
        });
        add(bYes);
        add(bNo);


    }
}
