package table;

import cards.Card;
import cards.CardValues;
import cards.Suit;
import combinations.CombinationsValues;
import player.Player;

import java.util.*;

public abstract class CardTableBase {
    protected Queue<Card> deck;
    protected static Queue<Player> activePlayers;
    protected List<Player> players;
    protected List<Card> tableCards;
    protected Player winner;

    protected int smallBlind = 25;
    protected int bigBlind = 50;

    public CardTableBase() {
        activePlayers = new ArrayDeque<>();
        players = new ArrayList<>();
        tableCards = new ArrayList<>();
        players.add(new Player("Антон Заварка", 1000));
        players.add(new Player("Гей Турчинский", 1000));
        players.add(new Player("Михаил Елдаков", 1000));
        initHand();
    }

    // инициализация раздачи
    public void initHand() {
        initDeck();
        activePlayers.clear();
        activePlayers.addAll(players);
        activePlayers.forEach(x -> x.setInGame(true));
        winner = null;
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

    // установка диллера
    public void setDealer() {
        assert activePlayers.peek() != null;
        activePlayers.peek().setDealer(true);
        activePlayers.add(activePlayers.poll());
    }

    // проверка окончания торгов
    public static boolean isTradeFinished() {
        var somePlayer = activePlayers.peek();
        return activePlayers.stream().
            noneMatch(x -> somePlayer.getCurrBet() != x.getCurrBet());
    }

    void printCardsOnTheTable() {
        System.out.println("Карты на столе:");
        tableCards.forEach(x -> System.out.print(x.getCardValue() + " " + x.getSuit() + "; "));
        System.out.println("\n\n");
    }

    //выстраиваме нормальный порядок для хода
    public void setOrder() {
        while (!Objects.requireNonNull(activePlayers.peek()).isDealer())
            activePlayers.add(activePlayers.poll());
//        activePlayers.add(activePlayers.poll());
    }

    private void setPlayersCombinations() {
        for (Player player : activePlayers) {
            player.setCombination(CombinationsValues.getCombination(player.getCards(), tableCards));
        }
    }

    public void whoIsWinner() {
        setPlayersCombinations();
//        activePlayers.forEach(x -> {
//            x.getCombination()
//        });
        var winner = activePlayers.poll();
        assert winner != null;
        for (Player player : activePlayers) {
            if (player.getCombination().getValue() > winner.getCombination().getValue())
                winner = player;
            if (player.getCombination().getValue() == winner.getCombination().getValue()
                && player.getCombination().getKicker() > winner.getCombination().getKicker())
                winner = player;
        }
    }

    public Player createPlayer() {
        return new Player("Name", new Random().nextInt() + 1000);
    }

    public static Queue<Player> getActivePlayers() {
        return activePlayers;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}