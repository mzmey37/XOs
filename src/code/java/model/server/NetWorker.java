package code.java.model.server;

import code.java.model.game.GameModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class NetWorker {

    private static final long TIME_DELAY = 250;
    static final int PORT = 6060;
    static final int PUSH_CELL = 0;
    static final int SAY_GOOD_BYE = 1;
    private static final int SUGGEST_PLAY_AGAIN = 2;
    static final int SEND_NAME = 3;
    private static final int INIT_GAME = 4;
    private static final int CONSENT_TO_PLAY = 5;

    private boolean enemyConnected = false;
    private volatile boolean wasSignal = false;
    private final GameModel gameModel;
    Socket cSocket;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    NetWorker(GameModel gm) {
        gameModel = gm;
    }

    void runMessagingProcess() {
        while (hasConnection()) {
            processMessage(getNextMessage());
        }
    }

    private void processMessage(Message message) {
        if (message == null) {
            return;
        }
        switch (message.getType()) {
            case SEND_NAME:
                gameModel.setP2Name(message.getName());
                break;
            case PUSH_CELL:
                gameModel.pushCell(message.getJ(), message.getI(), false);
                //если кто-то победил
                if (!gameModel.isInGame()) {
                    gameModel.setWinI(message.getI());
                    gameModel.setWinJ(message.getJ());
                }
                break;
            case SUGGEST_PLAY_AGAIN:
                gameModel.askConfirmationToPlayAgain();
                break;
            case INIT_GAME:
                gameModel.initGame(message.getWidth(), message.getHeight(), message.getWinLen(), GameModel.O_CELL);
                break;
            case SAY_GOOD_BYE:
                onEnemyDisconnect();
                break;
            case CONSENT_TO_PLAY:
                gameModel.initGame(gameModel.getWidth(), gameModel.getHeight(), gameModel.getWinLen(), GameModel.X_CELL);
                sendInitGame();
                break;
        }
    }

    public synchronized void waitSignal() {
        if (!wasSignal) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        wasSignal = false;
    }

    synchronized void sendSignal() {
        wasSignal = true;
        notify();
    }

    public void disconnect() {
        if (cSocket == null) {
            return;
        }
        try {
            if (enemyConnected) {
                dos.writeInt(SAY_GOOD_BYE);
            }
            dos.close();
            dis.close();
            cSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasConnection() {
        return cSocket != null && !cSocket.isClosed();
    }

    public void sendPushCell(int i, int j) {
        try {
            dos.writeInt(PUSH_CELL);
            dos.writeInt(i);
            dos.writeInt(j);
        } catch (IOException e){
            onEnemyDisconnect();
            e.printStackTrace();
        }
    }

    public void sendSuggestPlayAgain() {
        try {
            dos.writeInt(SUGGEST_PLAY_AGAIN);
        } catch (IOException e){
            onEnemyDisconnect();
            e.printStackTrace();
        }
    }

    public void sendInitGame() {
        try {
            dos.writeInt(INIT_GAME);
            dos.writeInt(gameModel.getWidth());
            dos.writeInt(gameModel.getHeight());
            dos.writeInt(gameModel.getWinLen());
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameModel.setFirstOrder();
    }

    private Message getNextMessage() {
        int type;
        try {
            while (dis.available() == 0) {
                Thread.sleep(TIME_DELAY);
            }
            type = dis.readInt();
            switch (type) {
                case SEND_NAME:
                    int length = dis.readInt();
                    byte[] b = new byte[length];
                    dis.read(b, 0, length);
                    return new Message(type, new String(b));
                case PUSH_CELL:
                    int i = dis.readInt();
                    int j = dis.readInt();
                    return new Message(type, i, j);
                case INIT_GAME:
                    int width = dis.readInt();
                    int height = dis.readInt();
                    int winLen = dis.readInt();
                    return new Message(type, width, height, winLen);
                default:
                    return new Message(type);
            }
        } catch (IOException | InterruptedException e) {
            onEnemyDisconnect();
            return null;
        }
    }

    private void onEnemyDisconnect() {
        enemyConnected = false;
        gameModel.setP2Name(null);
        if (gameModel.getState() == GameModel.CHOOSING_PARAMS && this instanceof ServerNetWorker) {
            gameModel.acceptClient();
        }
        else {
            gameModel.setInitialState();
            gameModel.dropGame();
            disconnect();
        }
    }

    void prepareStreams() {
        enemyConnected = true;
        try {
            dis = new DataInputStream(cSocket.getInputStream());
            dos = new DataOutputStream(cSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendName(String name) {
        try {
            dos.writeInt(SEND_NAME);
            dos.writeInt(name.length());
            dos.write(name.getBytes());
        } catch (IOException e) {
            onEnemyDisconnect();
            e.printStackTrace();
        }
    }

    public boolean enemyConnected() {
        return enemyConnected;
    }

    public void sendConsentToOffer() {
        try {
            dos.writeInt(CONSENT_TO_PLAY);
            System.out.println("sent consent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
