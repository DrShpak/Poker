package table;

import betting.BetManager;
import cards.Card;
import cards.CardValues;
import cards.Suit;
import combinations.Combination;
import player.Player;

import java.util.*;

public abstract class CardTableBase {
    protected final Queue<Card> deck;
    protected static Queue<Player> activePlayers;
    protected final List<Player> players;
    protected final List<Card> tableCards;
    protected Player winner;
    protected final BetManager betMngr;

    public CardTableBase() {
        activePlayers = new ArrayDeque<>();
        players = new ArrayList<>();
        tableCards = new ArrayList<>();
        deck = new ArrayDeque<>();
        betMngr = new BetManager();
//        players.add(Player.createBot("Антон Заварка", 1000, this, betMngr));
//        players.add(Player.createBot("Гей Турчинский", 1000, this, betMngr));
//        players.add(Player.createBot("Михаил Елдаков", 1000, this, betMngr));
        players.add(Player.createPlayer("Дмитрий \"Доктор Шпак\" Титов", 1000, this));
        players.add(Player.createPlayer("Анатолий Кринжовик", 1000, this));
        players.add(Player.createPlayer("Узколобый мещанин", 1000, this));
        initHand();
    }

    // инициализация раздачи
    public void initHand() {
        deck.clear();
        tableCards.clear();
        activePlayers.clear();
        initDeck();
        betMngr.setPot(0);
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
    private void initDeck() {
//        deck = new ArrayDeque<>();
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

    @SuppressWarnings("TextBlockMigration")
    void printCardsOnTheTable() {
        System.out.println("Карты на столе:");
        tableCards.forEach(x -> System.out.print(x.getCardValue() + " " + x.getSuit() + "; "));
        //noinspection TextBlockMigration
        System.out.println("\n\n");
    }

    //выстраиваме нормальный порядок для хода
    public void setOrder() {
        while (!Objects.requireNonNull(activePlayers.peek()).isDealer())
            activePlayers.add(activePlayers.poll());
    }

    public void showCombinations() {
        this.players.
            stream().
            map(x -> Map.entry(x, Combination.generate(x))).
            forEach(x -> System.out.println(x.getKey().getName() + ": " + x.getValue().getDesc().getName()));
    }

    public void whoIsWinner() {
        //noinspection OptionalGetWithoutIsPresent
        this.winner = this.players.
            stream().
            map(x -> Map.entry(x, Combination.generate(x))).
            max(Map.Entry.comparingByValue()).
            get().
            getKey();
    }

    @SuppressWarnings("unused")
    public abstract void preFlop();

    @SuppressWarnings("unused")
    public abstract void flop();

    @SuppressWarnings("unused")
    public abstract void turn();

    @SuppressWarnings("unused")
    public abstract void river();

    public static Queue<Player> getActivePlayers() {
        return activePlayers;
    }

    public Player getWinner() {
        return winner;
    }

    public BetManager getBetMngr() {
        return betMngr;
    }

    public void winnerTakePot(Player player, int pot) {
        player.setStack(player.getStack() + pot);
    }

    public List<Card> getTableCards() {
        return tableCards;
    }

    public List<Player> getPlayers() {
        return players;
    }
}