package betting;

import player.Player;

import java.util.Random;
import java.util.Scanner;

public class Raise extends Betting {
    private int betSize;

    public Raise(Player player, BetManager mngr) {
        super(player, mngr);
    }

    public Raise() {
    }

    @Override
    public boolean bet() {
        if (isAvailable) {
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
        if (player.getThread() == null) {
            System.out.print("Input your bet: ");
            betSize = Integer.parseInt(new Scanner(System.in).nextLine());
        } else {
            betSize = new Random().nextInt(player.getStack());
        }
        return mngr.isPotEnough(player, betSize - player.getCurrBet()) && betSize >= mngr.currBet && !mngr.canBet;
    }
}
