package betting;

import player.Player;
import table.CardTableBase;

public class Check extends Betting {
    public Check(Player player, BetManager mngr) {
        super(player, mngr);
    }

    public Check() {
    }

    @Override
    public boolean bet() {
        if (mngr.getPot() != 0 && ((mngr.lastBet instanceof Check) || (mngr.lastBet == null))) {
            printGap();
            return true;
        }
        printMessageerror("check");
        return false;
    }
}
