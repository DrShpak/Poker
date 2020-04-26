package betting;

import player.Player;
import table.CardTableBase;

public class Check extends Betting {
    public Check(Player player, BetManager mngr) {
        super(player, mngr);
    }

    @Override
    public boolean bet() {
        if (mngr.getPot() != 0 && CardTableBase.isTradeFinished() && !mngr.canBet) {
            printGap();
            return true;
        }
        printMessageerror("check");
        return false;
    }
}
