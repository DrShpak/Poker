package combinations;

import cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum CombinationsValues {
    KICKER(1),
    ONEPAIR(2),
    TWOPAIR(3),
    THREEOFAKIND(4),
    STRAIGHT(5),
    FLUSH(6),
    FULLHOUSE(7),
    FOUROFAKIND(8),
    STRAIGHTFLUSH(9),
    ROYALFLUSH(10);

    int value;
    int kicker;

    CombinationsValues(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getKicker() {
        return kicker;
    }

    public static CombinationsValues getCombination(List<Card> playerCards, List<Card> tableCards) {
        if (!isStraight(playerCards, tableCards).isEmpty())
            return getStraight(playerCards, tableCards);
        if (isOnePair(playerCards, tableCards).size() == 1)
            return getOnePair(playerCards, tableCards);
        return null;
    }


    private static ArrayList<ArrayList<Card>> isOnePair(List<Card> playerCards, List<Card> tableCards) {
        var cardsCombinations = CombinationUtils.generateCombinations(2, 7, playerCards, tableCards);
        var pairs = new ArrayList<ArrayList<Card>>();
        for (ArrayList<Card> combination : cardsCombinations) {
            if (combination.get(0).getCardValue().getValue() == combination.get(1).getCardValue().getValue())
                pairs.add(combination);
        }
        return pairs;
    }

    private static CombinationsValues getOnePair(List<Card> playerCards, List<Card> tableCards) {
        var pair = isOnePair(playerCards, tableCards).get(0);
        ONEPAIR.kicker = pair.get(0).getCardValue().getValue();
        return ONEPAIR;
    }

    private static ArrayList<ArrayList<Card>> isStraight(List<Card> playerCards, List<Card> tableCards) {
        var cardsCombinations = CombinationUtils.generateCombinations(5, 7, playerCards, tableCards);
        var straights = new ArrayList<ArrayList<Card>>();
        int count;
        for (ArrayList<Card> combination : cardsCombinations) {
            Collections.sort(combination);
            count = 0;
            for (int i = 0; i < combination.size() - 1; i++) {
                if (combination.get(i + 1).getCardValue().getValue()
                    - combination.get(i).getCardValue().getValue() > 1)
                    break;
                count++;
            }
            if (count == combination.size() - 1)
                straights.add(combination);
        }
        return straights;
    }

    private static CombinationsValues getStraight(List<Card> playerCards, List<Card> tableCards) {
        var straights = isStraight(playerCards, tableCards);
        if (straights.size() > 1) {
            var maxCombination = straights.get(0);
            for (ArrayList<Card> straight : straights) {
                if (maxCombination.get(maxCombination.size() - 1).getCardValue().getValue()
                    < straight.get(maxCombination.size() - 1).getCardValue().getValue()) {
                    maxCombination = straight;
                }
            }
            STRAIGHT.kicker = maxCombination.get(maxCombination.size() - 1).getCardValue().getValue();
        } else {
            STRAIGHT.kicker = straights.get(0).get(4).getCardValue().getValue();
        }
        return STRAIGHT;
    }

    private static CombinationsValues isTwoPair(List<Card> playerCards, List<Card> tableCards) {
//        var allCards = CombinationUtils.mergeCards(playerCards, tableCards);
        return null;
    }
}
