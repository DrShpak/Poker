package betting;

import player.Player;

public class AllIn extends Betting {

    public AllIn(Player player, BetManager mngr) {
        super(player, mngr);
    }

    public AllIn() {
    }

    @Override
    public boolean bet() {
        var betSize = player.getStack();
        takeMoney(player, betSize);
        if (betSize < mngr.currBet) {
            player.setCurrBet(mngr.currBet);
        } else {
            mngr.currBet = betSize;
        }
        increasePot(betSize);
        printMadeBet("all-in");
        mngr.setIsAllIn(true);
        mngr.canBet = false;
        System.out.println(betSize + "$");
        printGap();
        return true;
    }

    @Override
    public boolean isAvailable() {
        return player.getStack() > 0 && !mngr.canBet;
    }
}
