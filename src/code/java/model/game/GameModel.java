package code.java.model.game;


import code.java.model.server.ClientNetWorker;
import code.java.model.server.NetWorker;
import code.java.model.server.ServerNetWorker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameModel {

    private static final String DELIMETR = "~";
    public static final int INITIAL = 0;
    public static final int CHOOSING_PARAMS = 1;
    public static final int PLAYING = 2;
    public static final int AFTER_GAME = 3;
    private static final String recordsPath = "./records.txt";
    public static final int FREE_CELL = 0;
    public static final int X_CELL = 1;
    public static final int O_CELL = 2;
    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2;
    public static final int NOBODY_WINS = 0;
    private static final int[][][] directions = {{{-1, 0}, {1, 0}}, {{0, 1}, {0, -1}}, {{-1, -1}, {1, 1}}, {{-1, 1}, {1, -1}}};
    //длина по горизонтали
    private int width;
    //длина по вертикали
    private int height;
    //длина линии для победы
    private int winLen;
    //ход первого, если true
    private boolean firstOrder = true;
    private int[][] cells;
    private int winnerIs = 0;
    private boolean inGame = false;
    private String p1Name;
    private String p2Name;
    private boolean savedResults = true;
    private NetWorker netWorker;
    private int[][] winLine;
    private int playerMarker;
    private int opponentMarker;
    private int winI;
    private int winJ;
    private boolean secondAsksPlayAgain = false;
    private int state = INITIAL;

    public int[][] getWinLine() {
        return winLine;
    }

    /**
     * так стартует сервер
     * @param width ширина
     * @param height высота
     * @param winLen длина для выигрыша
     */
    public void initGame(String width, String height, String winLen) {
        state = PLAYING;
        playerMarker = X_CELL;
        opponentMarker = O_CELL;
        int w;
        int h;
        int l;
        try {
            w = Integer.valueOf(width);
            h = Integer.valueOf(height);
            l = Integer.valueOf(winLen);
        } catch (NumberFormatException e) {
            this.inGame = false;
            return;
        }

        if (w > 2 && h > 2 && l > 2 && (l <= w || l <= h)) {
            this.width = w;
            this.height = h;
            this.cells = new int[h][w];
            this.winLen = l;
            this.inGame = true;
            this.firstOrder = true;
            netWorker.sendInitGame();
        }
    }

    /**
     * так стартует клиент
     * @param width ширина
     * @param height высота
     * @param winLen длина для выигрыша
     */
    public void initGame(int width, int height, int winLen, int cell) {
        state = PLAYING;
        playerMarker = cell;
        opponentMarker = cell == X_CELL ? O_CELL : X_CELL;
        this.width = width;
        this.height = height;
        this.cells = new int[height][width];
        this.winLen = winLen;
        this.inGame = true;
        this.firstOrder = false;
    }

    /**
     * нажатие кнопки
     * @param x - координата столбца
     * @param y - координата строки
     */
    public void pushCell(int x, int y, boolean byView) {
        if (x >= 0 && y >= 0 && x < this.width && y < this.height && cells[y][x] == FREE_CELL) {
            if (byView) {
                netWorker.sendPushCell(y, x);
            }
            cells[y][x] = firstOrder ? playerMarker : opponentMarker;

            if (hasWinner(x, y)) {
                state = AFTER_GAME;
                dropGame();
                saveResults();
                return;
            }
            firstOrder = !firstOrder;
        }
    }

    /**
     * проверяет, выиграл ли кто-то, после нажатия на клетку [x][y]
     * @param x - координата столбца
     * @param y - координата строки
     * @return true, если выиграл
     */
    private boolean hasWinner(int x, int y) {
        int checkedInt = cells[y][x];

        for (int[][] direction: directions) {
            if (1 + lenInDirection(x, y, direction[0][0], direction[0][1], checkedInt) +
                    lenInDirection(x, y, direction[1][0], direction[1][1], checkedInt) >= winLen) {
                winnerIs = firstOrder ? PLAYER_ONE : PLAYER_TWO;
                this.winLine = new int[2][2];
                this.winLine[0][0] = x + direction[0][0] * lenInDirection(x, y, direction[0][0], direction[0][1], checkedInt);
                this.winLine[0][1] = y + direction[0][1] * lenInDirection(x, y, direction[0][0], direction[0][1], checkedInt);
                this.winLine[1][0] = x + direction[1][0] * lenInDirection(x, y, direction[1][0], direction[1][1], checkedInt);
                this.winLine[1][1] = y + direction[1][1] * lenInDirection(x, y, direction[1][0], direction[1][1], checkedInt);
                return true;
            }
        }

        int mult = 1;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                mult *= cells[i][j];
            }
        }
        if (mult > 0) {
            winnerIs = NOBODY_WINS;
            return true;
        }

        return false;
    }

    /**
     * длина одинаковых нажатий в данном направлении
     * @param x - координата столбца клетки
     * @param y - координата строки клетки
     * @param g - направление по горизонтали
     * @param v - напраавление по вертикали
     * @param checkedInt - в зависимости от того, линию из крестиков или ноликов мы ищем
     * @return - длину такой линии
     */
    private int lenInDirection(int x, int y, int g, int v, int checkedInt) {
        int length = 0;
        int i = 1;
        //индикатор того, что можно двинуться дальше
        boolean mayContinue;
        //проверка, не вышли ли мы за границу
        if(x + g >= 0 && y + v >= 0 && x + g < this.width && y + v < this.height) {
            mayContinue = true;
        }
        else {
            return 0;
        }
        while (mayContinue) {
            if(cells[y + v * i][x  + g * i] == checkedInt) {
                length++;
                i++;
                //не вышли ли за границу
                mayContinue = x + i * g >= 0 && y + i * v >= 0 && x + i * g < this.width && y + i * v < this.height;
            }
            else {
                mayContinue = false;
            }
        }
        return length;
    }

    public void dropGame() {
        inGame = false;
    }

    public boolean isInGame() {
        return inGame;
    }

    public int getWidth() {
        return this.width;
    }

    public int[][] getCells() {
        return cells;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWinLen() {
        return this.winLen;
    }

    public int getWinner() {
        return winnerIs;
    }

    public int getOrder() {
        return firstOrder ? PLAYER_ONE : PLAYER_TWO;
    }

    public String getP1Name() {
        return this.p1Name;
    }

    public String getP2Name() {
        return this.p2Name;
    }

    public String getWinnerName() {
        return this.winnerIs == PLAYER_ONE ? this.p1Name : p2Name;
    }

    public void saveResults() {
        File fResults = new File(recordsPath);
        try {
        if (!fResults.exists()) {
            fResults.createNewFile();
        }
        DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yy");
        Date date = new Date();
        Files.write(Paths.get(recordsPath),
                (p1Name + DELIMETR + p2Name + DELIMETR + winnerIs +
                        DELIMETR + dateFormat.format(date) + "\n").getBytes(),
                StandardOpenOption.APPEND);
        savedResults = true;
        } catch (IOException e) {
            savedResults = false;
        }
    }

    public String[][] getRecords() {
        try {
            String[] lines =  new String(Files.readAllBytes(Paths.get(recordsPath))).split("\n");
            String[][] records = new String[lines.length][4];
            for (int i = 0; i < lines.length; i++) {
                records[i] = lines[i].split(DELIMETR);
            }
            return records;
        } catch (IOException e) {
            return null;
        }
    }

    public boolean resultsSaved() {
        return this.savedResults;
    }

    public void connect(String name, String ip, int port) {
        p2Name = null;
        if (name.length() > 0 && !name.contains("~")) {
            setP1Name(name);
            runClient(ip, port);
        } else {
            netWorker = null;
        }
    }

    private void runClient(String ip, int port) {
        ClientNetWorker client = new ClientNetWorker(this, port);
        client.connectTo(ip);
        client.waitSignal();
        if (client.hasConnection()) {
            netWorker = client;
            netWorker.sendName(p1Name);
        }
        state = CHOOSING_PARAMS;
    }

    public boolean isConnected() {
        return netWorker != null && netWorker.hasConnection();
    }

    public void hostGame(String name, int port) {
        p2Name = null;
        if (name.length() > 0 && !name.contains("~")) {
            setP1Name(name);
            runServer(port);
        }
        else {
            state = INITIAL;
            netWorker = null;
        }
    }

    private void runServer(int port) {
        ServerNetWorker server = new ServerNetWorker(this, port);
        server.host();
        server.waitSignal();
        if (server.hosted()) {
            state = CHOOSING_PARAMS;
            netWorker = server;
            acceptClient();
        }
        else {
            state = INITIAL;
        }

    }

    public void acceptClient() {
        new Thread(() -> {
            ((ServerNetWorker) netWorker).startAccepting();
            netWorker.waitSignal();
            if (netWorker.hasConnection())
                netWorker.sendName(p1Name);
        }).start();
    }

    private void setP1Name(String name) {
        p1Name = name;
    }

    public void setP2Name(String name) {
        p2Name = name;
    }

    public void disconnect() {
        if (netWorker != null) {
            netWorker.disconnect();
        }
    }

    public int getWinI() {
        return winI;
    }

    public void setWinI(int winI) {
        this.winI = winI;
    }

    public int getWinJ() {
        return winJ;
    }

    public void setWinJ(int winJ) {
        this.winJ = winJ;
    }

    public boolean enemyConnected() {
        return netWorker.enemyConnected();
    }

    public void suggestPlayAgain() {
        netWorker.sendSuggestPlayAgain();
    }

    public void askConfirmationToPlayAgain() {
        secondAsksPlayAgain = true;
    }

    public boolean secondAsksPlayAgain() {
        return secondAsksPlayAgain;
    }

    public void rejectPlayAgain() {
        secondAsksPlayAgain = false;
        dropGame();
        netWorker.disconnect();
    }

    public void consentToPlay() {
        state = PLAYING;
        secondAsksPlayAgain = false;
        dropGame();
        netWorker.sendConsentToOffer();
    }

    public void setFirstOrder() {
        firstOrder = true;
    }

    public int getState() {
        return state;
    }

    public void setInitialState() {
        state = INITIAL;
    }

    public int getPlayerMarker() {
        return playerMarker;
    }
}
