package betting;

import player.Player;

public class Fold extends Betting {
    public Fold(Player player, BetManager mngr) {
        super(player, mngr);
    }

    @Override
    public boolean bet() {
        return true;
    }
}
