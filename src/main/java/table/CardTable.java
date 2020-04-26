package table;

import betting.BetManager;
import betting.Check;
import cards.Card;
import player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class CardTable extends CardTableBase {
    private List<List<Card>> combinations;
    private Map<Player, List<Card>> playersCombinations;
    private BetManager betMngr;

    public CardTable() {
        super();
        betMngr = new BetManager();
    }

    public void preFlop() {
        activePlayers.forEach(x -> x.setCards(this.deck.poll(), this.deck.poll()));
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
        Player currPlayer;
        var count = 0; // костыльное гавно, которое дополняет проверку в случае чеков

        do {
            System.out.println(String.format("Current bet = %d$ ", betMngr.getCurrBet()));
            System.out.println(String.format("Overall pot = %d$ ", betMngr.getPot()));
            currPlayer = activePlayers.poll();
            assert currPlayer != null;
            System.out.println("Player " + currPlayer.getName() + " is making a bet...");
            System.out.println(String.format("Current bet %s's = %d$", currPlayer.getName(), currPlayer.getCurrBet()));
            printListOfActs();
            betMngr.bet(currPlayer);
            count++;

            activePlayers = activePlayers.stream().filter(Player::isInGame) // выыбрасываем тех, кто сделал фолд
                .collect(Collectors.toCollection(ArrayDeque::new));
            if (currPlayer.isInGame())
                activePlayers.add(currPlayer);
        } while (!isTradeFinished() || (betMngr.getLastBet() instanceof Check && count < activePlayers.size()));

        betMngr.resetPlayersCurrBets(this); // у игроков тоже сбарсываем их ставки за прошлый раунд торговли
        if (activePlayers.size() == 1)
            winner = activePlayers.poll();
        betMngr.setLastBet(null);
        /*
          восстанавливаем правильный порядок. когда все уровнялись, очередь закончилась на том
          кто зарейзил
         */
        setOrder();
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