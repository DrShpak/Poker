package betting;

import player.Player;

import java.util.Scanner;

public class Raise extends Betting {

    public Raise(Player player, BetManager mngr) {
        super(player, mngr);
    }

    @Override
    public boolean bet() {
        System.out.print("Input your bet: ");
        var betSize = Integer.parseInt(new Scanner(System.in).nextLine());
        if (mngr.isPotEnough(player, betSize) && betSize >= mngr.currBet && !mngr.canBet) {
            System.out.print("Input your bet: ");
            increasePot(betSize);
            takeMoney(player, betSize);
            mngr.currBet = betSize;
            printGap();
            return true;
        } else {
            printMessageerror("raise");
            return false;
        }
    }
}
