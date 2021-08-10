package game;


public enum Result {
    WIN, DRAW, UNKNOWN, CHEAT;

    private int who = -1;

    public void setWho(int who) {
        this.who = who;
    }
    public int getWho() {
        return who;
    }
}