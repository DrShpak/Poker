package combinations;

import cards.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CombinationUtils {

    /*
     алгоритм подсчета колчиества сочетаний без повторений
     он генерирует какую-т опоследовательность из n чисел по k чисел
     например для 5-карточных комбинаций он бвдет выдавать:
     0 1 2 3 4 (это ИНДЕКСЫ)
     1 2 3 4 5
     2 5 6 0 1
     и тд
     */
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

    // получаем всевозможные комбинации из 7 карт
    public static ArrayList<ArrayList<Card>> generateCombinations(int k, int n, List<Card> playerCards, List<Card> tableCards) {
        var cards = mergeCards(playerCards, tableCards);
        var combinationsOfCards = new ArrayList<ArrayList<Card>>(); // список со всеми возможными комбинациями
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
