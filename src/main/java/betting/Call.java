package betting;

import player.Player;

public class Call extends Betting {
    public Call(Player player, BetManager mngr) {
        super(player, mngr);
    }

    public Call() {
    }

    @Override
    public boolean bet() {
        if (mngr.isPotEnough(player, mngr.currBet - player.getCurrBet()) && !mngr.canBet) {
            increasePot(mngr.currBet - player.getCurrBet());
            takeMoney(player, mngr.currBet - player.getCurrBet());
            printGap();
            return true;
        } else {
            printMessageerror("call");
            return false;
        }
    }
}
