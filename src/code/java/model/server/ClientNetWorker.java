package code.java.model.server;

import code.java.model.game.GameModel;

import java.io.IOException;
import java.net.Socket;

public class ClientNetWorker extends NetWorker{

    public ClientNetWorker(GameModel gm) {
        super(gm);
    }

    public void connectTo(String host) {
        try {
            cSocket = new Socket(host, PORT);
            prepareStreams();
            new Thread(this::runMessagingProcess).start();
            sendSignal();
        } catch (IOException e) {
            sendSignal();
            e.printStackTrace();
        }
    }

}
