package cards;

public class Card implements Comparable<Card> {
    private CardValues cardValue;
    private Suit suit;

    public Card(CardValues cardValue, Suit suit) {
        this.cardValue = cardValue;
        this.suit = suit;
    }

    public CardValues getCardValue() {
        return cardValue;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(this.cardValue.value, o.cardValue.value);
    }
}
