package test.model.game;

import code.java.model.game.GameModel;
import code.java.model.server.ClientNetWorker;
import code.java.model.server.ServerNetWorker;
import org.junit.Assert;
import org.junit.Test;

public class TestNetWorker extends Assert implements Runnable {

    private static final String LOCALHOST = "localhost";
    private volatile boolean runServer = true;
    private ClientNetWorker client = new ClientNetWorker(new GameModel());
    private ServerNetWorker server = new ServerNetWorker(new GameModel());

    @Override
    public void run() {
        if (runServer) {
            server.host();
        } else {
            client.connectTo(LOCALHOST);
        }
    }

    private void runServAndClient() {
        runServer = true;
        Thread tServer = new Thread(this, "server");
        tServer.start();
        //wait server to bound
        server.waitSignal();
        runServer = false;
        Thread tClient = new Thread(this, "client");
        tClient.start();
        //client connects
        client.waitSignal();
        //server accepts
        server.waitSignal();
    }

    @Test
    public void testConnectDisconnect() throws InterruptedException {
        runServer = true;
        Thread tServer = new Thread(this, "server");
        tServer.start();
        //wait server to bound
        server.waitSignal();
        assertTrue(server.isWorking());
        runServer = false;
        Thread tClient = new Thread(this, "client");
        tClient.start();
        //wait server to accept and client to connect
        server.waitSignal();
        client.waitSignal();
        assertTrue(client.hasConnection());
        assertTrue(server.hasConnection());
        client.disconnect();
        server.disconnect();
        assertFalse(client.hasConnection());
        assertFalse(server.hasConnection());
        assertFalse(server.isWorking());
    }

    @Test
    public void testMeeting() throws InterruptedException {
        runServAndClient();
        for (int i = 0; i < 5; i++) {
            client.sendName("ClIeNt" + (i + 1));
            server.sendName("SeRv" + (i + 1));
            Thread.sleep(500);
        }

        server.sendSuggestPlayAgain();

        Thread.sleep(500);

        server.disconnect();
        client.disconnect();
    }

    @Test
    public void testManyMessages() {
        runServAndClient();
        System.out.println("haha");
        server.sendPushCell(1, 1);
        server.sendPushCell(1, 2);
        server.sendPushCell(2, 1);
        client.sendPushCell(4,4);
        client.sendPushCell(4,5);
        client.sendPushCell(5,4);




        server.disconnect();
        client.disconnect();
    }

}
