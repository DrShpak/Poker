package betting;

import player.Player;

import java.util.Scanner;

public class Raise extends Betting {

    public Raise(Player player, BetManager mngr) {
        super(player, mngr);
    }

    public Raise() {
    }

    @Override
    public boolean bet() {
        if (isAvailable) {
            int betSize;
            if (player.getThread() == null) {
                System.out.print("Input your bet: ");
                betSize = Integer.parseInt(new Scanner(System.in).nextLine());
                if (betSize <= mngr.currBet) {
                    System.out.println("You must bet more than " + mngr.currBet + "$");
                    return false;
                }
            } else {
                betSize = (int) (Math.random() * (player.getStack() - mngr.currBet)) + mngr.currBet;
            }
            increasePot(betSize - player.getCurrBet());
            takeMoney(player, betSize - player.getCurrBet());
            mngr.currBet = betSize;
            printMadeBet("raise");
            System.out.println(betSize + "$");
            printGap();
            return true;
        } else {
            printMessageerror("raise");
            return false;
        }
    }

    @Override
    public boolean isAvailable() {
        return !mngr.canBet && player.getStack() > mngr.currBet;
    }
}
