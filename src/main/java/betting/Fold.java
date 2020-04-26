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
        if (mngr.canBet || (mngr.lastBet instanceof Check)) {
            printMessageerror("fold");
            return false;
        }
        printGap();
        return true;
    }
}
