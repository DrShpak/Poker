package combinations;

import cards.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CombinationUtils {

    private static int[] generateIndices(int k, int n, int[] arr) {
        if (arr == null)
        {
            arr = new int[k];
            for (int i = 0; i < k; i++)
                arr[i] = i + 1;
            return arr;
        }
        for (int i = k - 1; i >= 0; i--)
            if (arr[i] < n - k + i + 1)
            {
                arr[i]++;
                for (int j = i; j < k - 1; j++)
                    arr[j + 1] = arr[j] + 1;
                return arr;
            }
        return null;
    }

    public static ArrayList<ArrayList<Card>> generateCombinations(int k, int n, List<Card> playerCards, List<Card> tableCards) {
        var cards = mergeCards(playerCards, tableCards);
        var combinationsOfCards = new ArrayList<ArrayList<Card>>();
        int[] arr = null;
        while ((arr = generateIndices(k, n, arr)) != null)
            combinationsOfCards.add(
                new ArrayList<>(Arrays.stream(arr)
                    .mapToObj(x -> cards.get(x - 1))
                    .collect(Collectors.toList())));
        return combinationsOfCards;
    }

    private static ArrayList<Card> mergeCards(List<Card> playerCards, List<Card> tableCards) {
        var allCards = new ArrayList<>(playerCards);
        allCards.addAll(tableCards);
        return allCards;
    }
}
