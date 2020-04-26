package player;

import betting.Betting;
import cards.Card;
import combinations.CombinationUtils;

import java.util.Arrays;
import java.util.List;

public class Player {
    private String name;
    private List<Card> cards;
    private int chips;
    private int stack; // общий банк игрока
    private int currBet; // текущая ставка текущего РАУНДА
    private boolean isDealer;
    private boolean isInGame;
    private CombinationUtils combinationUtils;

    public Player(String name, int overallPot) {
        this.name = name;
        this.stack = overallPot;
        this.isInGame = true;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(Card card1, Card card2) {
        this.cards = Arrays.asList(card1, card2);
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
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

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public CombinationUtils getCombinationUtils() {
        return combinationUtils;
    }

    public void setCombinationUtils(CombinationUtils combinationUtils) {
        this.combinationUtils = combinationUtils;
    }
}
