package combinations;

import cards.Card;
import org.javatuples.Pair;
import player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Combination implements Comparable<Combination> {
    private final Map.Entry<List<Card>, List<Card>> cards;
    private final CombinationDescription desc;

    private Combination(Map.Entry<List<Card>, List<Card>> cards, CombinationDescription desc) {
        this.cards = cards;
        this.desc = desc;
    }

    @Override
    public int compareTo(Combination o) {
        if (this.desc.value != o.desc.value) {
            return this.desc.value - o.desc.value;
        }

        var combos = Arrays.compare(
            this.cards.getKey().stream().mapToInt(x -> x.getCardValue().getValue()).toArray(),
            o.cards.getKey().stream().mapToInt(x -> x.getCardValue().getValue()).toArray()
        );

        if (combos != 0) {
            return combos;
        }

        return Arrays.compare(
            this.cards.getValue().stream().mapToInt(x -> x.getCardValue().getValue()).toArray(),
            o.cards.getValue().stream().mapToInt(x -> x.getCardValue().getValue()).toArray()
        );
    }

    public static Combination generate(Player player) {
        var playerCards = player.getPlayerCards();
        //noinspection OptionalGetWithoutIsPresent
        var param = Arrays.stream(CombinationDescription.values()).
            sorted((x, y) -> y.value - x.value).
            map(x -> Pair.with(x.provide(playerCards), x)).
            filter(x -> x.getValue0() != null).
            findFirst().
            get();
        return new Combination(param.getValue0(), param.getValue1());
    }

    public CombinationDescription getDesc() {
        return desc;
    }


}