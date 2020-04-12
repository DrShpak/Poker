package cards;

public enum Suit {
    DIAMOND ("diamond"),
    CLUB ("club"),
    HEART ("heart"),
    SPADE ("spade");

    String suit;

    Suit(String suit) {
        this.suit = suit;
    }
}