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
        if (isAvailable) {
            var betSize = mngr.currBet - player.getCurrBet();
            increasePot(betSize);
            takeMoney(player, betSize);
            printMadeBet("call");
            System.out.println(betSize + "$");
            printGap();
            return true;
        } else {
            printMessageerror("call");
            return false;
        }
    }

    @Override
    public boolean isAvailable() {
        return mngr.isPotEnough(player, mngr.currBet - player.getCurrBet()) && !mngr.canBet;
    }
}
