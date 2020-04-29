package betting;

import player.Player;

public class Fold extends Betting {
    public Fold(Player player, BetManager mngr) {
        super(player, mngr);
    }

    public Fold() {
    }

    @Override
    public boolean bet() {
        if (isAvailable) {
            printMadeBet("fold");
            printGap();
            player.setInGame(false);
            return true;
        }
        printMessageerror("fold");
        return false;
    }

    @Override
    public boolean isAvailable() {
        return !(mngr.canBet || (mngr.lastBet instanceof Check));
    }
}
