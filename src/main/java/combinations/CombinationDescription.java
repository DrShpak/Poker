package combinations;

import cards.Card;
import cards.Suit;
import player.PlayerCards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
public enum CombinationDescription {
    KICKER(1, "High card", "High card %s") {
        @Override
        public Map.Entry<List<Card>, List<Card>> provide(PlayerCards playerCards) {
            var combo = new ArrayList<Card>();
            return Map.entry(combo, playerCards.getKickers(combo, 5));
        }
    },
    ONEPAIR(2, "Pair", "A Pair of %s`s") {
        @Override
        public Map.Entry<List<Card>, List<Card>> provide(PlayerCards playerCards) {
            var pair = playerCards.getHigherGroup(List.of(Map.entry(2, 1)));
            return pair != null ?
                Map.entry(pair, playerCards.getKickers(pair, 5)) :
                null;
        }
    },
    TWOPAIR(3, "Two Pair", "Two Pair, %s`s and %s`s") {
        @Override
        public Map.Entry<List<Card>, List<Card>> provide(PlayerCards playerCards) {
            var twoPair = playerCards.getHigherGroup(List.of(Map.entry(2, 2)));
            return twoPair != null ?
                Map.entry(twoPair, playerCards.getKickers(twoPair, 5)) :
                null;
        }
    },
    THREEOFAKIND(4, "Set", "A Set of %s`s") {
        @Override
        public Map.Entry<List<Card>, List<Card>> provide(PlayerCards playerCards) {
            var set = playerCards.getHigherGroup(List.of(Map.entry(3, 1)));
            return set != null ?
                Map.entry(set, playerCards.getKickers(set, 5)) :
                null;
        }
    },
    STRAIGHT(5, "Straight", "A Straight to %s") {
        @Override
        public Map.Entry<List<Card>, List<Card>> provide(PlayerCards playerCards) {
            var straights = playerCards.getSequentialCards(5);
            return straights != null && !straights.isEmpty() ?
                Map.entry(straights.get(0), playerCards.getKickers(straights.get(0), 5)) :
                null;
        }
    },
    FLUSH(6, "Flush", "A Flush to %s") {
        @Override
        public Map.Entry<List<Card>, List<Card>> provide(PlayerCards playerCards) {
            var flushs = playerCards.
                getSimilarCards(Arrays.stream(Suit.values()).map(x -> (z) -> z.getSuit() == x), 5);
            return flushs != null ?
                Map.entry(flushs.get(0), playerCards.getKickers(flushs.get(0), 5)) :
                null;
        }
    },
    FULLHOUSE(7, "Full house", "A Full house, %s`s full of %s`s") {
        @Override
        public Map.Entry<List<Card>, List<Card>> provide(PlayerCards playerCards) {
            var fullHouse = playerCards.getHigherGroup(List.of(Map.entry(3, 1), Map.entry(2, 1)));
            return fullHouse != null ?
                Map.entry(fullHouse, playerCards.getKickers(fullHouse, 5)) :
                null;
        }
    },
    FOUROFAKIND(8, "Square", "A Square of %s") {
        @Override
        public Map.Entry<List<Card>, List<Card>> provide(PlayerCards playerCards) {
            var square = playerCards.getHigherGroup(List.of(Map.entry(4, 1)));
            return square != null ?
                Map.entry(square, playerCards.getKickers(square, 5)) :
                null;
        }
    },
    STRAIGHTFLUSH(9, "Straight Flush", "Straight Flush to %s") {
        @Override
        public Map.Entry<List<Card>, List<Card>> provide(PlayerCards playerCards) {
            var straights = playerCards.getSequentialCards(5);
            var flushs = playerCards.getSimilarCards(Arrays.stream(Suit.values()).map(x -> (z) -> z.getSuit() == x), 5);
            if (flushs == null || straights == null) {
                return null;
            }
            var straigthFlash = flushs.
                stream().
                filter(x -> straights.
                        stream().anyMatch(y -> y.
                        stream().anyMatch(z -> x.
                        stream().
                        filter(w -> w.equals(z)).
                        count() == 5
                    )
                    )
                ).
                findFirst().
                orElse(null);
            return straigthFlash != null ?
                Map.entry(straigthFlash, playerCards.getKickers(straigthFlash, 5)) :
                null;
        }
    };

    final int value;
    final String name;
    final String format;

    CombinationDescription(int value, String name, String format) {
        this.value = value;
        this.name = name;
        this.format = format;
    }

    public abstract Map.Entry<List<Card>, List<Card>> provide(PlayerCards playerCards);

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}

