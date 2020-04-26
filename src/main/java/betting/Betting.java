package betting;

import player.Player;

public abstract class Betting {
    protected Player player;
    protected BetManager mngr;

    public Betting(Player player, BetManager mngr) {
        this.player = player;
        this.mngr = mngr;
    }

    public Betting() {
    }

    public abstract boolean bet();

    protected void printMessageerror(String act) {
        System.out.println("You cannot make \"" + act + "\"");
        System.out.print("Make another choice: ");
    }

    protected void takeMoney(Player player, int betSize) {
        player.setStack(player.getStack() - betSize);
        player.setCurrBet(player.getCurrBet() + betSize);
    }

    protected void printGap() {
        System.out.println("\n\n");
    }

    protected void increasePot(int betSize) {
        mngr.setPot(mngr.getPot() + betSize);
    }
}
