package code.java.model.server;

public class Message {

    private int type;
    private int i;
    private int winLen;
    private int j;
    private String name;

    Message(int type, int i, int j) {
        this.type = type;
        this.i = i;
        this.j = j;
    }

    Message(int type) {
        this.type = type;
    }

    Message(int type, String name) {
        this.type = type;
        this.name = name;
    }

    Message(int type, int i, int j, int winLen) {
        this.type = type;
        this.i = i;
        this.j = j;
        this.winLen = winLen;
    }

    int getType() {
        return type;
    }

    int getI() {
        return i;
    }

    int getJ() {
        return j;
    }

    int getWidth() {
        return i;
    }

    int getHeight() {
        return j;
    }

    int getWinLen() {
        return winLen;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (type == NetWorker.PUSH_CELL) {
            return "update (" + i + ", " + j + ")";
        }
        else if (type == NetWorker.SAY_GOOD_BYE) {
            return "good bye";
        }
        else if (type == NetWorker.SEND_NAME) {
            return "my name is " + name;
        }
        else {
            return "suggestion to play again";
        }
    }
}
