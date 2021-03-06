package betting;

import player.Player;

import java.util.Scanner;

public class Bet extends Betting {
    public Bet(Player player, BetManager mngr) {
        super(player, mngr);
    }

    public Bet() {
    }

    @Override
    public boolean bet() {
        if (isAvailable) {
            var betSize = 0;
            if (player.getThread() == null) {
                System.out.print("Input your bet: ");
                betSize = Integer.parseInt(new Scanner(System.in).nextLine());
            } else {
                betSize = (int) (Math.random() * (player.getStack() - mngr.minBet)) + mngr.minBet;
            }
            if (mngr.isPotEnough(player, betSize) && betSize >= mngr.minBet) {
                takeMoney(player, betSize);
                mngr.currBet = betSize;
                increasePot(betSize);
                printMadeBet("bet");
                System.out.println(betSize + "$");
                printGap();
                mngr.canBet = false; //больше ставить нельзя (только колл, рейз, фолд)
                return true;
            } else {
                System.out.println("Insufficient funds or your bet is smaller than min");
                return false;
            }
        }
        printMessageError("bet");
        return false;
    }

    @Override
    public boolean isAvailable() {
        if (player.getStack() < mngr.minBet) //для ботов
            return false;
        return mngr.canBet;
    }
}