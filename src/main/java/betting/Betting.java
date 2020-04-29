package betting;

import player.Player;

//суперкласс для всех видов ставки
public abstract class Betting {
    protected Player player;
    protected BetManager mngr;
    protected boolean isAvailable;

    public Betting(Player player, BetManager mngr) {
        this.player = player;
        this.mngr = mngr;
        isAvailable = isAvailable();
    }

    public Betting() {
    }

    @SuppressWarnings("unused")
    public abstract boolean bet();

    @SuppressWarnings("unused")
    public abstract boolean isAvailable();

    protected void printMessageError(String act) {
        System.out.println("You cannot make \"" + act + "\"");
        System.out.print("Make another choice: ");
    }

    protected void printMadeBet(String bet) {
        System.out.print(player.getName() + " made " + bet + " ");
    }

    protected void takeMoney(Player player, int betSize) {
        player.setStack(player.getStack() - betSize);
        player.setCurrBet(player.getCurrBet() + betSize);
    }

    protected void printGap() {
        //noinspection TextBlockMigration
        System.out.println("\n\n");
    }

    protected void increasePot(int betSize) {
        mngr.setPot(mngr.getPot() + betSize);
    }
}
