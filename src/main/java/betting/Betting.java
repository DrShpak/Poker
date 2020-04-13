package betting;

import player.Player;

public enum Betting {
    BET {
        @Override
        public void bet(int betSize, Player player) {
            player.setCurrentBet(betSize);
        }
    },
    CHECK {
        @Override
        public void bet(int betSize, Player player) {
            player.setCurrentBet(0);
        }
    },
    CALL {
        @Override
        public void bet(int betSize, Player player) {
            player.setCurrentBet(player.getCurrentBet() + betSize);
        }
    },
    RAISE {
        @Override
        public void bet(int betSize, Player player) {
            player.setCurrentBet(player.getCurrentBet() + betSize);
        }
    },
    FOLD {
        @Override
        public void bet(int betSize, Player player) {
            player.setCurrentBet(0);
            player.setOverallPot(player.getOverallPot() - betSize);
        }
    };

    public abstract void bet(int betSize, Player player);
}