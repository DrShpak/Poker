package player;

import cards.Card;

import java.util.Arrays;
import java.util.List;

public class Player {
    private String name;
    private List<Card> cards;
    private int chips;
    private int overallPot;
    private int currentBet;
    private boolean isDealer;

    public Player(String name, int overallPot) {
        this.name = name;
        this.overallPot = overallPot;
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

    public int getOverallPot() {
        return overallPot;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setOverallPot(int overallPot) {
        this.overallPot = overallPot;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }

    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }
}
