package player;

import cards.Card;
import com.google.common.collect.Streams;
import table.CardTableBase;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class PlayerCards {
    private final Player owner;
    private final CardTableBase board;

    public PlayerCards(Player owner, CardTableBase board) {
        this.owner = owner;
        this.board = board;
    }

    private List<Card> getPlayerCards() {
        return Stream.concat(
            this.owner.getCards().stream(),
            this.board.getTableCards().stream()
        ).
            sorted(Comparator.reverseOrder()).
            collect(Collectors.toList());
    }

    public List<Card> getKickers(List<Card> comboCards, int comboLength) {
        return this.getPlayerCards().
            stream().
            filter(x -> comboCards.
                stream().
                noneMatch(y -> y.equals(x))
            ).
            limit(comboLength - comboCards.size()).
            collect(Collectors.toList());
    }

    public List<Card> getHigherGroup(List<Map.Entry<Integer, Integer>> shapes) {
        var cards = this.getPlayerCards().
            stream().
            collect(Collectors.groupingBy(Card::getCardValue)).
            entrySet().
            stream().
            sorted((x, y) -> y.getValue().size() - x.getValue().size()).
            collect(Collectors.groupingBy(x -> x.getValue().size())).
            entrySet();

        var groups = shapes.
            stream().
            map(x -> cards.
                stream().
                filter(y -> y.getKey().equals(x.getKey())).
                findFirst().
                map(y -> y.
                    getValue().
                    stream().
                    sorted((a, b) -> b.getKey().compareTo(a.getKey())).
                    map(Map.Entry::getValue).
                    collect(Collectors.toList())
                ).
                map(y -> Map.entry(
                    x.getValue(),
                    y
                )).
                orElse(Map.entry(
                    x.getValue(),
                    new ArrayList<List<Card>>(new ArrayList<ArrayList<Card>>())
                ))
            ).
            collect(Collectors.toList());

        var finalGroups = StreamUtils.iterateByNext(
            groups,
            (curr, next) -> next.getValue().addAll(
                curr.
                    getValue().
                    stream().
                    skip(curr.getKey()).
                    collect(Collectors.toList())
            )
        ).map(x ->  x.
            getValue().
            stream().
            limit(x.getKey()).
            collect(Collectors.toList())
        ).collect(Collectors.toList());

        //noinspection UnstableApiUsage
        finalGroups = Streams.
            zip(
                shapes.stream(),
                finalGroups.stream(),
                (shape, group) -> Map.entry(
                    group.
                        stream().
                        map(y -> y.
                            stream().
                            limit(shape.getKey()).
                            collect(Collectors.toList())
                        ).
                        filter(y -> y.size() == shape.getKey()).
                        limit(shape.getValue()).
                        collect(Collectors.toList()),
                    shape.getValue()
                )
            ).
            filter(y -> y.getKey().size() == y.getValue()).
            map(Map.Entry::getKey).
            collect(Collectors.toList());

        if (finalGroups.size() != shapes.size()) {
            return null;
        }

        return finalGroups.
            stream().
            flatMap(x -> x.stream().flatMap(Collection::stream)).collect(Collectors.toList());
    }

    public List<List<Card>> getSimilarCards(Stream<Predicate<Card>> features, int count) {
        var cards = this.getPlayerCards();
        var foundFeatures = features.filter(x -> cards.stream().filter(x).count() >= count).collect(Collectors.toList());
        if (foundFeatures.isEmpty()) {
            return null;
        }
        return foundFeatures.stream().map(x -> cards.stream().filter(x).collect(Collectors.toList())).
            collect(Collectors.toList());
    }

    public List<List<Card>> getSequentialCards(int length) {
        var cards = this.
            getPlayerCards().
            stream().
            collect(StreamUtils.distinctByKey(Card::getCardValue)).
            sorted(Comparator.reverseOrder()).
            collect(Collectors.toList());
        var n = cards.size();
        var notACard = new Object();
        //noinspection OptionalGetWithoutIsPresent
        var sequentialIndices = IntStream.range(0, n - length).map(
            x -> cards.
                stream().
                skip(x).
                limit(length).
                map(y -> (Object)y).
                reduce(
                    (previous, current) ->
                        previous != notACard ?
                            (((Card)previous).getCardValue().getValue() - ((Card)current).getCardValue().getValue() == 1 ?
                                current :
                                notACard) : notACard
                ).
                get().equals(notACard) ? -1 : x
        ).filter(x -> x != -1).toArray();
        return Arrays.stream(sequentialIndices).
            mapToObj(x -> cards.stream().skip(x).limit(length).collect(Collectors.toList())).
            collect(Collectors.toList());
    }
}