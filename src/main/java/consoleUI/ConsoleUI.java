package consoleUI;

import table.CardTable;

public class ConsoleUI {
    private CardTable cardTable;

    public ConsoleUI() {
        this.cardTable = new CardTable();
    }

    public void startGame() {
        while (true) {
            cardTable.setDealer();
            cardTable.makeSmallBlind();
            cardTable.makeBigBlind();
            cardTable.preFlop();
            cardTable.tradeRound();
            cardTable.flop();
            cardTable.tradeRound();
            cardTable.turn();
            cardTable.tradeRound();
            cardTable.river();
            cardTable.tradeRound();
        }
    }
}
