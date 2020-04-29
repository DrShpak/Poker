package betting;

import org.reflections.Reflections;
import player.Player;
import table.CardTableBase;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class BetManager {
    private int pot = 0; // банк
    private final Scanner input;
    protected int minBet = 50;
    int currBet = 0; // текущая ставка текущих ранудов торгов
    protected boolean canBet = true; // можно ли сделать ставку?
    /*
     ненмого костыль
     (запомниаем последнюю ставку для проверки на допустимость сделать чек)
     то есть если последняя ставка была какая угодно кроме чека, то чек невозможен
     нужно либо коллировать, либо рейзить
     */
    protected Betting lastBet;
    private boolean isAllIn = false;

    private int smallBlind = 25;
    private int bigBlind = 50;

    public BetManager() {
        lastBet = null;
        this.input = new Scanner(System.in);
    }

    // достаточно ли банка у игрока чтобы сделать такую ставку
    boolean isPotEnough(Player player, int betSize) {
        return player.getStack() >= betSize;
    }

    public void makeSmallBlind() {
        makeBlind(smallBlind);
    }

    public void makeBigBlind() {
        makeBlind(bigBlind);
    }

    private void makeBlind(int betSize) {
        var players = CardTableBase.getActivePlayers();
        assert players.peek() != null;
        var player = players.peek();
        player.setCurrBet(betSize); // текущую ставку делаем равную блайнду (малому/большому)
        pot += betSize;
        player.setStack(player.getStack() - betSize);
        players.add(players.poll()); // вкинувшего блайнд игрока перекидываем в конец очереди
    }

    public void resetCurrBet() {
        currBet = 0;
    }

    public void betForReal(Player player) {
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
                    if (new Call(player, this).bet()) { // если это дейсвтие удалось выполнить то выходим из цикла
                        flag = false;
                        lastBet = new Call(); // запоминаем ласт ставку (для проверки на доупстимость чека, см. описание поля lastBet)
                    }
                }
                case "raise" -> {
                    if (new Raise(player, this).bet()) { // если это дейсвтие удалось выполнить то выходим из цикла
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
                        lastBet = new Fold();
                    }
                }
                case "all-in" -> {
                    if (new AllIn(player, this).bet()) {
                        flag = false;
                        lastBet = new AllIn();
                    }
                }
                default -> System.out.print("Unknown command! Try again: ");
            }
        }
    }

    public void bet(Player player) {
        if (player.getThread() == null) {
            betForReal(player);
        } else {
            player.notifyBot();
        }
    }

    public synchronized void betForBot(Player player) {
        Reflections allBets = new Reflections("betting");
        var availableBets = allBets.getSubTypesOf(Betting.class).stream().
            map(x -> {
                try {
                    return x.getDeclaredConstructor(Player.class, BetManager.class).newInstance(player, this);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }).
            filter(Objects::nonNull).
            filter(Betting::isAvailable).
            collect(Collectors.toList());
        var botBet = availableBets.get(new Random().nextInt(availableBets.size()));
        botBet.bet();
        lastBet = botBet;
    }

    public void takePot(Player winner) {
        winner.setStack(winner.getStack() + pot);
    }

    public void resetPlayersCurrBets() {
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

    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getMinBet() {
        return minBet;
    }

    public boolean isIsAllIn() {
        return isAllIn;
    }

    public void setIsAllIn(boolean isAllIn) {
        this.isAllIn = isAllIn;
    }
}