package table;

import cards.Card;
import cards.CardValues;
import cards.Suit;
import player.Player;

import java.util.*;

public class CardTableBase {
    protected Queue<Card> deck;
    protected Queue<Player> players;

    protected int smallBlind = 25;
    protected int bigBlind = 50;

    public CardTableBase() {
        players = new ArrayDeque<>();
        players.add(new Player("Антон Заварка", 1000));
        players.add(new Player("Гей Турчинский", 1000));
        players.add(new Player("Михаил Елдаков", 1000));
        initDeck();
    }

    //инициализация карт
    private List<Card> initCards() {
        Set<CardValues> cardValues = EnumSet.allOf(CardValues.class);
        Set<Suit> suits = EnumSet.allOf(Suit.class);

        var cards = new ArrayList<Card>();
        for (CardValues cardValue : cardValues) {
            for (Suit suit : suits) {
                cards.add(new Card(cardValue, suit));
            }
        }
        return cards;
    }

    //инициализция колоды
    void initDeck() {
        deck = new ArrayDeque<>();
        var cards = initCards();
        var random = new Random();
        var index = 0;
        while (!cards.isEmpty()) {
            index = random.nextInt(cards.size());
            deck.add(cards.get(index));
            cards.remove(index);
        }
    }

    public void setDealer() {
        assert players.peek() != null;
        players.peek().setDealer(true);
        players.add(players.poll());
    }

    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    protected boolean isTradeFinished() {
        var somePlayer = players.peek();
//        var isTradeFinished = true;
//        for (Player player : players) {
//            if (somePlayer.getCurrentBet() != player.getCurrentBet()) {
//                isTradeFinished = false;
//                break;
//            }
//        }
//        return isTradeFinished;
        return !(players.stream().
            anyMatch(x -> somePlayer.getCurrentBet() != x.getCurrentBet()));
    }
}