package table;

import betting.BetManager;
import cards.Card;
import player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class CardTable extends CardTableBase {
    private List<List<Card>> combinations;
    private Map<Player, List<Card>> playersCombinations;
    private List<Card> tableCards;
    private BetManager betMngr;

    public CardTable() {
        super();
        tableCards = new ArrayList<>();
        betMngr = new BetManager();
    }

    public void preFlop() {
        players.forEach(x -> x.setCards(this.deck.poll(), this.deck.poll()));
    }

    public void flop() {
        deck.stream().
            limit(3).
            forEach(x -> {
                tableCards.add(x);
                deck.remove(x);
            });
        printCardsOnTheTable();
    }

    public void turn() {
        tableCards.add(deck.poll());
        printCardsOnTheTable();
    }

    public void river() {
        tableCards.add(deck.poll());
        printCardsOnTheTable();
    }

    public void tradeRound() {
        betMngr.setCanBet(true);
        betMngr.resetCurrBet(); // сбрасываем текущую ставку (ну то есть каждый раунд торговли новая текущая ставка)
        betMngr.resetPlayersCurrBets(this); // у игроков тоже сбарсываем их чтавки за прошлый раунд торговли

        do {
            for (Player player : players) {
                System.out.println("Player " + player.getName() + " is making a bet...");
                printListOfActs();
                betMngr.bet(player, this);
                this.players = players.stream().filter(Player::isInGame) // выыбрасываем тех, кто сделал фолд
                    .collect(Collectors.toCollection(ArrayDeque::new));

            }
        } while (!isTradeFinished());
    }

    private void printCardsOnTheTable() {
        System.out.println("Карты на столе:");
        tableCards.forEach(x -> System.out.print(x.getCardValue() + " " + x.getSuit() + "; "));
        System.out.println("\n\n");
    }

    private void printListOfActs() {
        System.out.println("List of actions: ");
        System.out.println("\"bet\" - make min bet (access only for first player)");
        System.out.println("\"check\"");
        System.out.println("\"call\"");
        System.out.println("\"raise\"");
        System.out.println("\"fold\"");
        System.out.print("Input your choice by words: ");
    }

    public BetManager getBetMngr() {
        return betMngr;
    }
}