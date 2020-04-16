package betting;

import player.Player;
import table.CardTable;

import java.util.Scanner;

public class BetManager {
    private int pot = 0;
    private Scanner input;
    private int minBet = 50;
    private int currBet = 0;
    private boolean canCheck;
    private boolean canBet = true;

    public BetManager() {
        this.input = new Scanner(System.in);
    }

    public boolean makeBet(Player player) {
        if (canBet) {
            System.out.print("Input your bet: ");
            var betSize = Integer.parseInt(input.nextLine());
            if (isPotEnough(player, betSize) && betSize >= minBet) {
                takeMoney(player, betSize);
                increasePot(betSize);
                currBet = betSize;
                printGap();
                canBet = false; //больше ставить нельзя (только колл, рейз, фолд)
                return true;
            }
        }
        printMessageerror("bet");
        return false;
//        return false;
    }

    public boolean makeRaise(Player player) {
        System.out.print("Input your bet: ");
        var betSize = Integer.parseInt(input.nextLine());
        if (isPotEnough(player, betSize) && betSize >= minBet && betSize >= currBet) {
            System.out.print("Input your bet: ");
            takeMoney(player, betSize);
            increasePot(betSize);
            currBet = betSize;
            printGap();
            return true;
        } else {
            printMessageerror("raise");
            return false;
        }
    }

    public boolean makeCall(Player player) {
        if (isPotEnough(player, currBet - player.getCurrBet()) && currBet != 0) {
            takeMoney(player, currBet - player.getCurrBet());
            increasePot(currBet);
            printGap();
            return true;
        } else {
            printMessageerror("call");
            return false;
        }
    }

    public boolean makeCheck(CardTable table) {
        if (pot != 0 && table.isTradeFinished()) {
            printGap();
            return true;
        }
        printMessageerror("check");
        return false;
    }

    public void makeFold(Player player) {
        player.setCurrentPot(0);
        printGap();
    }

    private boolean isPotEnough(Player player, int betSize) {
        return player.getStack() >= betSize;
    }

    private void takeMoney(Player player, int betSize) {
        player.setStack(player.getStack() - betSize);
//        player.setCurrentPot(player.getCurrentPot() + betSize);
        player.setCurrBet(player.getCurrBet() + betSize);
    }

    private void increasePot(int betSize) {
        this.pot += betSize;
    }

    public void makeSmallBlind(CardTable table) {
        makeBlind(table, table.getSmallBlind());
    }

    public void makeBigBlind(CardTable table) {
        makeBlind(table, table.getBigBlind());
    }

    private void makeBlind(CardTable table, int betSize) {
        var players = table.getPlayers();
        assert players.peek() != null;
        var player = players.peek();
        player.setCurrBet(betSize);
        players.add(players.poll()); // вкинувшего малый блайнд игрока перекидываем в конец очереди
    }

    private void printMessageerror(String act) {
        System.out.println("You cannot make \"" + act);
        System.out.print("Make another choice: ");
    }

    private void printGap() {
        System.out.print("\n\n");
    }

    public void setCanCheck(boolean canCheck) {
        this.canCheck = canCheck;
    }

    public void resetCurrBet() {
        currBet = 0;
    }

    public void bet(Player player, CardTable table) {
        var flag = true; // крутим свитч до тех пор, пока не будет выбран корректный ход
        while (flag) {
            switch (input.nextLine()) {
                case "bet" -> {
                    if (makeBet(player)) // если это дейсвтие удалось выполнить то выходим из цикла
                        flag = false;
                }
                case "call" -> {
                    if (makeCall(player))
                        flag = false;
                }
                case "raise" -> {
                    if (makeRaise(player))
                        flag = false;
                }
                case "check" -> {
                    if (makeCheck(table))
                        flag = false;
                }
                case "fold" -> {
                    makeFold(player);
                    flag = false;
                    player.setInGame(false);
                }
            }
        }
    }

    public void resetPlayersCurrBets(CardTable table) {
        table.getPlayers().forEach(x -> x.setCurrBet(0));
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
}