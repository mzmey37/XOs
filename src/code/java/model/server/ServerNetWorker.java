package code.java.model.server;

import code.java.model.game.GameModel;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerNetWorker extends NetWorker {

    private ServerSocket sSocket;
    private boolean hosted = false;

    private void tryAcceptingClient() {
        try {
            cSocket = sSocket.accept();
            prepareStreams();
            new Thread(this::runMessagingProcess).start();
            sendSignal();
        } catch (IOException e) {
            sendSignal();
        }
    }

    public ServerNetWorker(GameModel gm, int port) {
        super(gm, port);
    }

    public void host() {
        try {
            sSocket = new ServerSocket(port);
            hosted = true;
            sendSignal();
        } catch (IOException e) {
            hosted = false;
            sendSignal();
            e.printStackTrace();
        }
    }

    public void startAccepting() {
        Thread acceptor = new Thread(this::tryAcceptingClient);
        acceptor.start();
    }

    private void dropServer() {
        if (sSocket != null) {
            try {
                sSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void disconnect() {
        super.disconnect();
        dropServer();
    }

    public boolean isWorking() {
        return sSocket != null && !sSocket.isClosed();
    }

    public boolean hosted() {
        return hosted;
    }
}
