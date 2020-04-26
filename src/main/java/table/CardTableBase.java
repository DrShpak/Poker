package table;

import cards.Card;
import cards.CardValues;
import cards.Suit;
import player.Player;

import java.util.*;

public abstract class CardTableBase {
    protected Queue<Card> deck;
    protected static Queue<Player> players;
    protected List<Card> tableCards;

    protected int smallBlind = 25;
    protected int bigBlind = 50;

    public CardTableBase() {
        players = new ArrayDeque<>();
        tableCards = new ArrayList<>();
        players.add(new Player("Антон Заварка", 1000));
        players.add(new Player("Гей Турчинский", 1000));
        players.add(new Player("Михаил Елдаков", 1000));
        initDeck();
    }

    // инициализация раздачи
    public void initHand() {

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

    public static boolean isTradeFinished() {
        var somePlayer = players.peek();
        return players.stream().
            noneMatch(x -> somePlayer.getCurrBet() != x.getCurrBet());
    }

    void printCardsOnTheTable() {
        System.out.println("Карты на столе:");
        tableCards.forEach(x -> System.out.print(x.getCardValue() + " " + x.getSuit() + "; "));
        System.out.println("\n\n");
    }

    //выстраиваме нормальный порядок для хода
    public void setOrder() {
        while (!Objects.requireNonNull(players.peek()).isDealer())
            players.add(players.poll());
        players.add(players.poll());
    }

    public static Queue<Player> getPlayers() {
        return players;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }
}