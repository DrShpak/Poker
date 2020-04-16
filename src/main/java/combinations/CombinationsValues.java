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

    CombinationsValues(int value) {
        this.value = value;
    }

    public static CombinationsValues getCombination(List<Card> playerCards, List<Card> tableCards) {
        return null;
    }



    private static CombinationsValues isOnePair(List<Card> playerCards, List<Card> tableCards) {
//         var allCards = CombinationUtils.mergeCards(playerCards, tableCards);
         return null;
    }

    private static boolean isStraight(List<Card> playerCards, List<Card> tableCards) {
        var cardsCombinations = CombinationUtils.generateCombinations(5, 7, playerCards, tableCards);
        var straights = new ArrayList<>();
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

        return straights.size() != 0;
    }

    private static CombinationsValues isTwoPair(List<Card> playerCards, List<Card> tableCards) {
//        var allCards = CombinationUtils.mergeCards(playerCards, tableCards);
        return null;
    }
}
