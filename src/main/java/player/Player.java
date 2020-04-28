package player;

import betting.BetManager;
import cards.Card;
import combinations.Combination;
import table.CardTableBase;

import java.util.Arrays;
import java.util.List;

public class Player implements Runnable {
    @SuppressWarnings("CanBeFinal")
    private String name;
    private List<Card> cards;
    private PlayerCards playerCards;
    private int stack; // общий банк игрока
    private int currBet; // текущая ставка текущего РАУНДА
    private boolean isDealer;
    private boolean isInGame;
    private Combination combination;
    private Thread thread;
    private boolean suspend;
    private boolean gameContinue;
    private BetManager mngr;

    public Player(String name, int overallPot, BetManager mngr) {
        this.name = name;
        this.stack = overallPot;
        this.mngr = mngr;
        thread = new Thread(this, name);
        suspend = true;
        thread.start();
        mySuspend();
        gameContinue = true;
    }

    public static Player createBot(String name, int overallPot, CardTableBase table, BetManager mngr) {
        var player = new Player(name, overallPot, mngr);
        player.playerCards = new PlayerCards(player, table);
        return player;
    }

    public static Player createPlayer(String name, int overallPot, CardTableBase table) {
        var player = new Player(name, overallPot);
        player.playerCards = new PlayerCards(player, table);
        return player;
    }

    private Player(String name, int overallPot) {
        this.name = name;
        this.stack = overallPot;
    }

    public PlayerCards getPlayerCards() {
        return playerCards;
    }

    public List<Card> getCards() {
        return cards;
    }

    // показать карты игрока
    @SuppressWarnings("unused")
    public String showCards() {
        return cards.get(0).getCardValue() + " " + cards.get(0).getSuit()
            + "; " + cards.get(1).getCardValue() + " " + cards.get(1).getSuit();
    }

    public void setCards(Card card1, Card card2) {
        this.cards = Arrays.asList(card1, card2);
    }

    public String getName() {
        return name;
    }

    public int getStack() {
        return stack;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }

    public int getCurrBet() {
        return currBet;
    }

    public void setCurrBet(int currBet) {
        this.currBet = currBet;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void setCombination(Combination combination) {
        this.combination = combination;
    }

    public Combination getCombination() {
        return combination;
    }

    @Override
    public void run() {
        try {
            while (gameContinue) {
                synchronized (this) {
                    while (suspend) {
                        wait();
                    }
                    System.out.println(name + " Проснулся!!");
                    mngr.betForBot(this);
                    mySuspend();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + "завершился");
    }

    public synchronized void mySuspend() {
        suspend = true;
    }

    public synchronized void notifyBot() {
        suspend = false;
        System.out.println("будим " + name);
        notify();
    }

    public Thread getThread() {
        return thread;
    }

    public void setGameContinue(boolean gameContinue) {
        this.gameContinue = gameContinue;
    }
}