package cards;

public class Card {
    private CardValues cardValue;
    private Suit suit;

    public Card(CardValues cardValue, Suit suit) {
        this.cardValue = cardValue;
        this.suit = suit;
    }

    public CardValues getCardValue() {
        return cardValue;
    }

    public void setCardValue(CardValues cardValue) {
        this.cardValue = cardValue;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }
}
