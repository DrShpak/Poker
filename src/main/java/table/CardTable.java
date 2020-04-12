package table;

import cards.Card;
import cards.CardValues;
import cards.Suit;
import player.Player;
import table.betting.Betting;

import java.util.*;

public class CardTable extends CardTableBase {
    private List<List<Card>> combinations;
//    private Queue<Card> deck;
    private Map<Player, List<Card>> playersCombinations;
    private Queue<Player> players;
    private List<Card> tableCards;

    private int highestBet = 0;
    private int smallBlind = 25;
    private int bigBlind = 50;

    public CardTable() {
//        players = new ArrayDeque<>();
        super();
        tableCards = new ArrayList<>();
//        players.add(new Player("Антон Заварка", 1000));
//        players.add(new Player("Гей Турчинский", 1000));
//        players.add(new Player("Михаил Елдаков", 1000));
//        initDeck();
    }

    public void smallBlind() {

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

    //console version
    public void tradeRound() {
        var currentBet = 0;
        var checkCounts = 0;
        while (checkCounts != players.size()) {
            var canCheck = true;

            for (Player player : players) {
                System.out.println("Player " + player.getName() + " is making a bet...");
                printListOfActs();
                var input = new Scanner(System.in);
                var flag = true;
                while (flag) {
                    switch (input.nextLine()) {
                        case "bet" -> {
                            if (highestBet != 0) {
                                System.out.println("You cannot make a bet! Bet has already made.");
                            } else {
                                System.out.print("Input your bet: ");
                                highestBet = Integer.parseInt(input.nextLine());
                                Betting.BET.bet(highestBet, player);
                                flag = false;
                                canCheck = false;
                                checkCounts = 0;
                                printGap();
                            }
                        }
                        case "call" -> {
                            if (highestBet == 0) {
                                printMessageerror("call");
                            } else {
                                Betting.CALL.bet(highestBet, player);
                                flag = false;
                                canCheck = false;
                                checkCounts = 0;
                                printGap();
                            }
                        }
                        case "raise" -> {
                            if (highestBet == 0) {
                                printMessageerror("raise");
                            } else {
                                System.out.print("Input your bet: ");
                                highestBet = Integer.parseInt(input.nextLine());
                                Betting.RAISE.bet(highestBet, player);
                                flag = false;
                                canCheck = false;
                                checkCounts = 0;
                                printGap();
                            }
                        }
                        case "check" -> {
                            if (canCheck && highestBet != 0) {
                                Betting.CHECK.bet(0, player);
                                checkCounts++;
                                flag = false;
                                printGap();
                            } else
                                printMessageerror("check");
                        }
                        case "fold" -> {
                            if (highestBet == 0) {
                                printMessageerror("fold");
                            } else {
                                Betting.FOLD.bet(player.getCurrentBet(), player);
                                flag = false;
                                checkCounts = 0;
                                printGap();
                            }
                        }
                    }
                }
            }
        }
    }

//    void initDeck() {
//        super.initDeck();
//    }

    private void printMessageerror(String act) {
        if (act.equals("check"))
            System.out.println("You cannot make \"" + act + "\"!\n"
            + "Other players has already made bets or min bet hasn't made yet.");
        else
            System.out.println("You cannot make \"" + act
                + "\"! Bet hasn't made yet.\n");
        System.out.print("Make another choice: ");
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

    private void printGap() {
        System.out.print("\n\n");
    }
}
