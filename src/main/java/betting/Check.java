package betting;

import player.Player;

public class Check extends Betting {
    public Check(Player player, BetManager mngr) {
        super(player, mngr);
    }

    public Check() {
    }

    @Override
    public boolean bet() {
        if (isAvailable) {
            printMadeBet("check");
            printGap();
            return true;
        }
        printMessageerror("check");
        return false;
    }

    @Override
    public boolean isAvailable() {
        return mngr.getPot() != mngr.getSmallBlind() + mngr.getBigBlind() && ((mngr.lastBet instanceof Check) || (mngr.lastBet == null));
    }
}
