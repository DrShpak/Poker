package betting;

import player.Player;
import table.CardTableBase;

import java.util.Objects;

public class Fold extends Betting {
    public Fold(Player player, BetManager mngr) {
        super(player, mngr);
    }

    public Fold() {
    }

    @Override
    public boolean bet() {
        if (isAvailable) {
            printMadeBet("fold");
            printGap();
            player.setInGame(false);
            if (player.isHasButton())
                // передаем кнопку далее сидящему
                Objects.requireNonNull(CardTableBase.getActivePlayers().peek()).setHasButton(true);
            return true;
        }
        printMessageError("fold");
        return false;
    }

    @Override
    public boolean isAvailable() {
        return !(mngr.canBet || (mngr.lastBet instanceof Check));
    }
}
