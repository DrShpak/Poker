package betting;

import player.Player;

import java.util.Scanner;

public class Bet extends Betting {
    public Bet(Player player, BetManager mngr) {
        super(player, mngr);
    }

    @Override
    public boolean bet() {
        if (mngr.canBet) {
            System.out.print("Input your bet: ");
            var betSize = Integer.parseInt(new Scanner(System.in).nextLine());
            if (mngr.isPotEnough(player, betSize) && betSize >= mngr.minBet) {
                takeMoney(player, betSize);
                mngr.currBet = betSize;
                increasePot(betSize);
                printGap();
                mngr.canBet = false; //больше ставить нельзя (только колл, рейз, фолд)
                return true;
            } else {
                System.out.println("Insufficient funds or your bet is smaller than min");
                return false;
            }
        }
        printMessageerror("bet");
        return false;
    }
}