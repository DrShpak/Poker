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
        players.add(Player.createBot("Антон Заварка", 1000, this, betMngr));
        players.add(Player.createBot("Гей Турчинский", 1000, this, betMngr));
        players.add(Player.createBot("Михаил Елдаков", 1000, this, betMngr));
//        players.add(Player.createPlayer("Дмитрий \"Доктор Шпак\" Титов", 2000, this));
//        players.add(Player.createPlayer("Анатолий Кринжовик", 3000, this));
//        players.add(Player.createPlayer("Узколобый мещанин", 1000, this));
//        players.add(Player.createPlayer("Гей Турчинский", 1000, this));
        initHand();
    }

    // инициализация раздачи
    public void initHand() {
        deck.clear();
        tableCards.clear();
        activePlayers.clear();
        addMoney();
        initDeck();
        betMngr.setPot(0);
        activePlayers.addAll(players);
        activePlayers.forEach(x -> x.setInGame(true));
        winner = null;
        betMngr.setIsAllIn(false);
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
        var temp = activePlayers.poll();
        assert temp != null;
        temp.setDealer(true);
        temp.setHasButton(true);
        activePlayers.add(temp);
    }

    // проверка окончания торгов
    public boolean isTradeFinished() {
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
        while (!Objects.requireNonNull(activePlayers.peek()).isHasButton())
            activePlayers.add(activePlayers.poll());
    }

    public void showCombinations() {
        activePlayers.
            stream().
            map(x -> Map.entry(x, Combination.generate(x))).
            forEach(x -> System.out.println(x.getKey().getName() + ": "
                + x.getKey().showCards() + " -- " + x.getValue().getDesc().getName()));
    }

    public void whoIsWinner() {
        //noinspection OptionalGetWithoutIsPresent
        this.winner = activePlayers.
            stream().
            map(x -> Map.entry(x, Combination.generate(x))).
            max(Map.Entry.comparingByValue()).
            get().
            getKey();
    }

    private void addMoney() {
        players.stream().
            filter(x -> x.getStack() < betMngr.getBigBlind()).
            forEach(x -> x.setStack(2000));
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

    public List<Card> getTableCards() {
        return tableCards;
    }

    public List<Player> getPlayers() {
        return players;
    }
}