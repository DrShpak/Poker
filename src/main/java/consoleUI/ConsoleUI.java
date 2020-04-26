package consoleUI;

import table.CardTable;

import java.util.Scanner;

public class ConsoleUI {
    private CardTable table;
    private Scanner input;

    public ConsoleUI() {
        this.table = new CardTable();
        this.input = new Scanner(System.in);
    }

    public void startGame() {
        do {
            hand();
            System.out.print("Do you want to continue? y/n: ");
        } while (!input.nextLine().equals("n"));
    }

    private void hand() {
        table.initHand();
        table.setDealer();
        table.getBetMngr().makeSmallBlind(table);
        table.getBetMngr().makeBigBlind(table);
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
//        table.turn();
        table.tradeRound();
        if (table.getWinner() == null)
            table.river();
        else {
            assert false;
            System.out.println("Winner is " + table.getWinner().getName());
            return;
        }
//        table.river();
        table.tradeRound();
        table.whoIsWinner();
    }
}