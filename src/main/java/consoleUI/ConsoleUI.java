package consoleUI;

import table.CardTable;

import java.util.Scanner;

public class ConsoleUI {
    private final CardTable table;
    private final Scanner input;

    public ConsoleUI() {
        this.table = new CardTable();
        this.input = new Scanner(System.in);
    }

    public void startGame() {
        do {
            hand();
            System.out.print("Do you want to continue? y/n: ");
        } while (!input.nextLine().equals("n"));
        table.getPlayers().forEach(x -> x.setGameContinue(false));
    }

    private void hand() {
        table.initHand();
        table.setDealer();
        table.getBetMngr().makeSmallBlind();
        table.getBetMngr().makeBigBlind();
        table.preFlop();
        table.tradeRound();
        if (table.getWinner() == null)
            table.flop();
        else {
            assert false;
            System.out.println("Winner is " + table.getWinner().getName());
            return;
        }
        table.tradeRound();
        if (table.getWinner() == null)
            table.turn();
        else {
            assert false;
            System.out.println("Winner is " + table.getWinner().getName());
            return;
        }
        table.tradeRound();
        if (table.getWinner() == null)
            table.river();
        else {
            assert false;
            System.out.println("Winner is " + table.getWinner().getName());
            return;
        }
        table.tradeRound();
        table.whoIsWinner();
        System.out.println("Winner is " + table.getWinner().getName());
        table.showCombinations();
    }
}