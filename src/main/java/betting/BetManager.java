package betting;

import player.Player;
import table.CardTable;
import table.CardTableBase;

import java.util.Scanner;

public class BetManager {
    private int pot = 0;
    private Scanner input;
    protected int minBet = 50;
    int currBet = 0;
    protected boolean canBet = true;
    protected Betting lastBet;

    public BetManager() {
        lastBet = null;
        this.input = new Scanner(System.in);
    }

    boolean isPotEnough(Player player, int betSize) {
        return player.getStack() >= betSize;
    }

    public void makeSmallBlind(CardTable table) {
        makeBlind(table, table.getSmallBlind());
    }

    public void makeBigBlind(CardTable table) {
        makeBlind(table, table.getBigBlind());
    }

    private void makeBlind(CardTable table, int betSize) {
        var players = CardTableBase.getActivePlayers();
        assert players.peek() != null;
        var player = players.peek();
        player.setCurrBet(betSize);
        pot += betSize;
        player.setStack(player.getStack() - betSize);
        players.add(players.poll()); // вкинувшего блайнд игрока перекидываем в конец очереди
    }

    public void resetCurrBet() {
        currBet = 0;
    }

    public void bet(Player player) {
        var flag = true; // крутим свитч до тех пор, пока не будет выбран корректный ход
        while (flag) {
            switch (input.nextLine()) {
                case "bet" -> {
                    if (new Bet(player, this).bet()) { // если это дейсвтие удалось выполнить то выходим из цикла
                        flag = false;
                        lastBet = new Bet();
                    }
                }
                case "call" -> {
                    if (new Call(player, this).bet()) {
                        flag = false;
                        lastBet = new Call();
                    }
                }
                case "raise" -> {
                    if (new Raise(player, this).bet()) {
                        flag = false;
                        lastBet = new Raise();
                    }
                }
                case "check" -> {
                    if (new Check(player, this).bet()) {
                        flag = false;
                        lastBet = new Check();
                    }
                }
                case "fold" -> {
                    if (new Fold(player, this).bet()) {
                        flag = false;
                        player.setInGame(false);
                        lastBet = new Fold();
                    }
                }
                default -> System.out.print("Unknown command! Try again: ");
            }
        }
    }

    public void resetPlayersCurrBets(CardTable table) {
        CardTableBase.getActivePlayers().forEach(x -> x.setCurrBet(0));
    }

    public void setMinBet(int minBet) {
        this.minBet = minBet;
    }

    public int getCurrBet() {
        return currBet;
    }

    public void setCanBet(boolean canBet) {
        this.canBet = canBet;
    }

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public void setLastBet(Betting lastBet) {
        this.lastBet = lastBet;
    }

    public Betting getLastBet() {
        return lastBet;
    }
}